package battleShips.protocolBinding;

import battleShips.StatusException;
import battleShips.BattleShipsException;
import battleShips.BattleShipsReceive;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamBindingReceiver extends Thread {
    private final DataInputStream dis;
    private final BattleShipsReceive receiver;

    public StreamBindingReceiver(InputStream is, BattleShipsReceive receiver) {
        this.dis = new DataInputStream(is);
        this.receiver = receiver;
    }

    public void readDice() throws IOException, StatusException {
        int random = this.dis.readInt();
        this.receiver.receiveDice(random);
    }

    public void readCoordinate() throws IOException, StatusException {
        int line = this.dis.readInt();
        int column = this.dis.readInt();
        try {
            this.receiver.receiveCoordinate(line, column);
        } catch (BattleShipsException e) {
            System.err.println("cannot execute coordinate - don't inform sender - error not part of protocol: "
                    + e.getLocalizedMessage());
        }
    }

    public void readConfirm() throws IOException, StatusException {
        int hit = this.dis.readInt();
        this.receiver.receiveConfirm(hit);
    }

    public void run() {
        boolean again = true;
        while(again) {
            try {
                int cmd = this.dis.readInt();

                switch (cmd) {
                    case StreamBinding.DICE : this.readDice(); break;
                    case StreamBinding.COORDINATE : this.readCoordinate(); break;
                    case StreamBinding.CONFIRM : this.readConfirm(); break;
                    default: again = false; System.err.println("unknown command code: " + cmd);
                }

            } catch (IOException e) {
                System.err.println("IOException: " + e.getLocalizedMessage());
                again = false;
            } catch (StatusException e) {
                System.err.println("Status Exception: " + e.getLocalizedMessage());
                again = false;
            }
        }
    }
}
