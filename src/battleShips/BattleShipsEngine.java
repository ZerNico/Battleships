package battleShips;

import java.io.IOException;
import java.util.Random;

public class BattleShipsEngine implements BattleShipsReceiver, BattleShipsUsage {
    public static final int UNDEFINED_DICE = -1;
    private final BattleShipsSender sender;

    private BattleShipsStatus status;
    private int sentDice = UNDEFINED_DICE;
    private int receivedRandom;


    public BattleShipsEngine(BattleShipsSender sender) {
        this.status = BattleShipsStatus.START;
        this.sender = sender;
    }

    @Override
    public void receiveDice(int random) throws IOException, StatusException {
        if (this.status != BattleShipsStatus.START && this.status != BattleShipsStatus.DICE_SENT) {
            throw new StatusException();
        }

        this.receivedRandom = random;

        // higher value -> SINKR, lower -> SINKS, same -> send dice again
        if(this.status == BattleShipsStatus.DICE_SENT) {
            this.decideWhoStarts();
        } else {
            this.status = BattleShipsStatus.DICE_RECEIVED;
        }
    }

    @Override
    public void receiveCoordinate(int line, int column) throws IOException, StatusException {
        if (this.status != BattleShipsStatus.SINKR) {
            throw new StatusException();
        }
    }

    @Override
    public void recieveConfirm(int hit) throws IOException, StatusException {
        if (this.status != BattleShipsStatus.CONFIRMR) {
            throw new StatusException();
        }
    }

    /*
    @Override
    public void sendCoordinate(int line, int column) throws IOException, StatusException {
        if (this.status != BattleShipsStatus.SINKS) {
            throw new StatusException();
        }
    }

    @Override
    public void sendConfirm(int hit) throws IOException, StatusException {
        if (this.status != BattleShipsStatus.CONFIRMS) {
            throw new StatusException();
        }
    }
    */

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                   remote engine support                                    //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void doDice() throws StatusException, IOException {
        if(this.status != BattleShipsStatus.START
                && this.status != BattleShipsStatus.DICE_RECEIVED) {
            throw new StatusException();
        }
        if (this.status != BattleShipsStatus.START) {
            throw new StatusException();
        }

        Random r = new Random();
        this.sentDice = r.nextInt();

        // send value via sender
        this.sender.sendDice(this.sentDice);

        this.status = BattleShipsStatus.DICE_SENT;
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


    @Override
    public BattleShipsStatus status() {
        return this.status;
    }

    @Override
    public void coordinate(int line, int column) throws BattleShipsException, StatusException {

    }
}
