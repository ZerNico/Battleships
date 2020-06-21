package battleShips.protocolBinding;

import battleShips.StatusException;
import battleShips.BattleShipsSender;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Encodes methods and parameter and sends via OutputStream
 */
public class StreamBindingSender implements BattleShipsSender {
    private final DataOutputStream dos;

    public StreamBindingSender(OutputStream os) {
        this.dos = new DataOutputStream(os);
    }

    @Override
    public void sendDice(int random) throws IOException, StatusException {
        this.dos.writeInt(StreamBinding.DICE);
        this.dos.writeInt(random);
    }

    @Override
    public void sendCoordinate(int line, int column) throws IOException, StatusException {
        this.dos.writeInt(StreamBinding.COORDINATE);
        this.dos.writeInt(line);
        this.dos.writeInt(column);
    }

    @Override
    public void sendConfirm(int hit) throws IOException, StatusException {
        this.dos.writeInt(StreamBinding.CONFIRM);
        this.dos.writeInt(hit);
    }
}
