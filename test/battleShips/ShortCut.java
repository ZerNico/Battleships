package battleShips;

import java.io.IOException;

public class ShortCut implements BattleShipsSender {

    private BattleShipsReceiver receiver;

    public void setReceiver(BattleShipsReceiver receiver) {
        this.receiver = receiver;
    }

    @Override
    public void sendDice(int random) throws IOException, StatusException {
        this.receiver.receiveDice(random);
    }

    @Override
    public void sendCoordinate(int line, int column) throws IOException, StatusException {
        this.receiver.receiveCoordinate(line, column);
    }

    @Override
    public void sendConfirm(int hit) throws IOException, StatusException {
        this.receiver.recieveConfirm(hit);
    }
}
