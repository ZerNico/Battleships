package battleShips;

import java.io.IOException;

public interface BattleShipsUsage {
    /**
     * figure out who starts
     */
    void doDice() throws StatusException, IOException;

    /**
     *
     * @return true if active - player can set a game stone
     */
    BattleShipsStatus status();

    /**
     * shoot at position - line column
     * @param line 0..9
     * @param column 0..9
     */
    void coordinate(int line, int column) throws BattleShipsException, StatusException, IOException;

    /**
     * send status of shot
     * @param hit 0..3
     */
    void confirm(int hit) throws BattleShipsException, StatusException, IOException;
}

