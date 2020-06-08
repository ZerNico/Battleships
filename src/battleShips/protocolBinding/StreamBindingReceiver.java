package battleShips.protocolBinding;

import battleShips.BattleShipsReceiver;
import battleShips.StatusException;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamBindingReceiver extends Thread {
    private final DataInputStream dis;
    private final BattleShipsReceiver receiver;


    public StreamBindingReceiver(InputStream is, BattleShipsReceiver receiver) {
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
        this.receiver.receiveCoordinate(line, column);
    }

    public void readConfirm() throws IOException, StatusException {
        int hit = this.dis.readInt();
        this.receiver.recieveConfirm(hit);
    }

    public void run() {
        boolean running = true;
        while(running) {
            try {
                int cmd = this.dis.readInt();

                switch (cmd) {
                    case StreamBinding.DICE: this.readDice(); break;
                    case StreamBinding.COORDINATE: this.readCoordinate(); break;
                    case StreamBinding.CONFIRM: this.readConfirm(); break;
                    default: running = false; System.err.println("unknown command code: " + cmd);
                }

            } catch (IOException e) {
                System.err.println("IOException: " + e.getLocalizedMessage());
                running = false;
            } catch (StatusException e) {
                System.err.println("Status Exception: " + e.getLocalizedMessage());
                running = false;
            }
        }
    }

}
