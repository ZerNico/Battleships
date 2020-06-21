package battleShips;

import java.io.IOException;
import java.util.Random;

public class BattleShipsEngine implements BattleShipsReceive, BattleShipsUsage {
    public static final int UNDEFINED_DICE = -1;
    private final BattleShipsSender sender;
    public static final int DIM = 10;

    BattleShipsBoardField[][] myBoard = new BattleShipsBoardField[DIM][DIM];
    BattleShipsBoardField[][] enemyBoard = new BattleShipsBoardField[DIM][DIM];

    private BattleShipsStatus status;
    private int sentDice = UNDEFINED_DICE;
    private int receivedRandom;

    public BattleShipsEngine(BattleShipsSender sender) {
        this.status = BattleShipsStatus.START;
        this.sender = sender;

        for(int i = 0; i < DIM; i++) {
            for(int j = 0; j < DIM; j++) {
                this.myBoard[i][j] = BattleShipsBoardField.SHIP;
                this.enemyBoard[i][j] = BattleShipsBoardField.UNKNOWN;
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                  remote engine support                                     //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void receiveDice(int random) throws IOException, StatusException {
        if( this.status != BattleShipsStatus.START
            && this.status != BattleShipsStatus.DICE_SENT
        ) {
            throw new StatusException();
        }

        this.receivedRandom = random;

        // höhere zahl - aktiv, kleinere -> passiv, gleiche zahl noch einmal.
        if(this.status == BattleShipsStatus.DICE_SENT) {
            this.decideWhoStarts();
        } else {
            this.status = BattleShipsStatus.DICE_RECEIVED;
        }
    }

    @Override
    public void receiveCoordinate(int line, int column) throws IOException, StatusException, BattleShipsException {
        if(this.status != BattleShipsStatus.SINKR) {
            throw new StatusException();
        }

        // set stone

        // check if allowed
        this.checkValidCoordinate(line, column);

        this.myBoard[line][column] = this.myBoard[line][column] == BattleShipsBoardField.SHIP ? BattleShipsBoardField.HITS : BattleShipsBoardField.HITW;

        this.checkGameOver();
    }

    @Override
    public void receiveConfirm(int hit) throws IOException, StatusException {

    }

    private void checkValidCoordinate(int line, int column) throws BattleShipsException {
        // coordinates correct
        if (line >= DIM || column >= DIM || line < 0 || column < 0) {
            throw new BattleShipsException("wrong parameters");
        }

        if (this.myBoard[line][column] == BattleShipsBoardField.HITS
                || this.myBoard[line][column] == BattleShipsBoardField.HITW ) {
            throw new BattleShipsException("position already shot");
        }
    }

    public void sendCoordinate(int line, int column) throws IOException, StatusException, BattleShipsException {
        if(this.status != BattleShipsStatus.SINKS) {
            throw new StatusException();
        }

        // send
        this.sender.sendCoordinate(line, column);

        // end?
        this.checkGameOver();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                  user interface support                                    //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void doDice() throws StatusException, IOException {
        if(this.status != BattleShipsStatus.START
            && this.status != BattleShipsStatus.DICE_RECEIVED) {
            throw new StatusException();
        }

        Random r = new Random();
        this.sentDice = r.nextInt();

        // sende den Wert über den Sender
        this.sender.sendDice(this.sentDice);

        if(this.status == BattleShipsStatus.DICE_RECEIVED) {
            this.decideWhoStarts();
        } else {
            this.status = BattleShipsStatus.DICE_SENT;
        }
    }

    private void decideWhoStarts() {
        if(this.receivedRandom == this.sentDice) {
            this.status = BattleShipsStatus.START;
        } else if(this.receivedRandom > this.sentDice) {
            this.status = BattleShipsStatus.SINKR;
        } else {
            this.status = BattleShipsStatus.SINKS;
        }
    }

    public BattleShipsStatus status() {
        return this.status;
    }

    @Override
    public void coordinate(int line, int column) throws BattleShipsException, StatusException, IOException {
        this.checkValidEnemyCoordinate(line, column);

        // valid data
        //this.board[line][column] = this.myStone;

        // send
        this.sendCoordinate(line, column);
    }

    @Override
    public void confirm(int hit) throws BattleShipsException, StatusException, IOException {

    }

    private void checkValidEnemyCoordinate(int line, int column) throws BattleShipsException {
        // coordinates correct
        if (line >= DIM || column >= DIM || line < 0 || column < 0) {
            throw new BattleShipsException("wrong parameters");
        }

        if (this.enemyBoard[line][column] != BattleShipsBoardField.UNKNOWN ) {
            throw new BattleShipsException("position already shot");
        }
    }

    private void checkGameOver() {
        // find three in a row

        int myHits = 0;
        int enemyHits = 0;

        for(int i = 0; i < DIM; i++) {
            for(int j = 0; j < DIM; j++) {
                if (enemyBoard[i][j] == BattleShipsBoardField.HITW) {
                    myHits++;
                }
                if (myBoard[i][j] == BattleShipsBoardField.HITW) {
                    enemyHits++;
                }
            }
        }

        if(enemyHits >= 30 || myHits >= 30) this.status = BattleShipsStatus.END;
    }
}
