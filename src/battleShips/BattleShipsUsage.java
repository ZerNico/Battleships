package battleShips;

import java.io.IOException;

public interface BattleShipsUsage {

    /**
     * figure out who starts
     */
    void doDice() throws StatusException, IOException;

    /**
     *
     * @return status of current game
     */
    BattleShipsStatus status();

    /**
     * shoot at position - line column
     * @param line 0..9
     * @param column 0..9
     */
    void coordinate(int line, int column) throws BattleShipsException, StatusException;
}
