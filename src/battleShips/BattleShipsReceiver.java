package battleShips;

import java.io.IOException;

public interface BattleShipsReceiver {
    /**
     * allowed in status START, goes to SINKS or SINKR
     * @param random
     * @throws IOException
     */
    void receiveDice(int random) throws IOException, StatusException;

    /**
     * allowed in status SINKR, goes to CONFIRMS
     * @param line
     * @param column
     * @throws IOException
     */
    void receiveCoordinate(int line, int column) throws IOException, StatusException;

    /**
     * allowed in status CONFIRMR, goes to SINKS or END
     * @param hit
     * @throws IOException
     */
    void recieveConfirm(int hit) throws IOException, StatusException;
}
