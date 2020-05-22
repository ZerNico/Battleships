package battleShips;

import java.io.IOException;

public interface BattleShipsSender {
    /**
     * allowed in status START
     * @param random
     * @throws IOException
     */
    void sendDice(int random) throws IOException, StatusException;

    /**
     * allowed in status SINKS, goes to CONFIRMR
     * @param line
     * @param column
     * @throws IOException
     */
    void sendCoordinate(int line, int column) throws IOException, StatusException;

    /**
     * allowed in status CONFIRMS, goes to SINKR
     * @param hit
     * @throws IOException
     */
    void sendConfirm(int hit) throws IOException, StatusException;
}
