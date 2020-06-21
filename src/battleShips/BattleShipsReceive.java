package battleShips;

import java.io.IOException;

public interface BattleShipsReceive {
    /**
     * erlaubt im Zustand START. führt zu Activ oder passiv
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
    void receiveCoordinate(int line, int column) throws IOException, StatusException, BattleShipsException;

    /**
     * allowed in status CONFIRMR, goes to SINKS or END
     * @param hit
     * @throws IOException
     */
    void receiveConfirm(int hit) throws IOException, StatusException;
}
