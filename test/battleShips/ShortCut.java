package battleShips;

import java.io.IOException;

public class ShortCut implements BattleShipsSender {
    private BattleShipsReceive receiver;

    public void setReceiver(BattleShipsReceive receiver) {
        this.receiver = receiver;
    }

    @Override
    public void sendDice(int random) throws IOException, StatusException {
        this.receiver.receiveDice(random);
    }

    @Override
    public void sendCoordinate(int line, int column) throws IOException, StatusException, BattleShipsException {
        this.receiver.receiveCoordinate(line, column);
    }

    @Override
    public void sendConfirm(int hit) throws IOException, StatusException {

    }
}
