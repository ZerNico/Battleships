package battleShips;

import java.io.IOException;
import java.util.Random;

public class BattleShipsEngine implements BattleShipsReceive, BattleShipsSender {
    public static final int UNDEFINED_DICE = -1;

    private BattleShipsStatus status;
    private int sentDice = UNDEFINED_DICE;

    public BattleShipsEngine() {
        this.status = BattleShipsStatus.START;
    }

    @Override
    public void receiveDice(int random) throws IOException, StatusException {
        if (this.status != BattleShipsStatus.START && this.status != BattleShipsStatus.DICE_SENT) {
            throw new StatusException();
        }

        // higher value -> SINKR, lower -> SINKS, same -> send dice again
        if (this.status == BattleShipsStatus.DICE_SENT) {
            if (random == this.sentDice) {
                this.status = BattleShipsStatus.START;
            } else if (random > this.sentDice) {
                this.status = BattleShipsStatus.SINKR;
            } else {
                this.status = BattleShipsStatus.SINKS;
            }
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

    @Override
    public void sendDice(int random) throws IOException, StatusException {
        if (this.status != BattleShipsStatus.START) {
            throw new StatusException();
        }

        Random r = new Random();
        this.sentDice = r.nextInt();

        // send value over TCP

        this.status = BattleShipsStatus.DICE_SENT;
    }

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
}
