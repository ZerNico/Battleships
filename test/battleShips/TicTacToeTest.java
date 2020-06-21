package battleShips;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class TicTacToeTest {

    @Test
    public void usageTest() throws BattleShipsException, StatusException, IOException {
        ShortCut sender1 = new ShortCut();
        BattleShipsEngine game1 = new BattleShipsEngine(sender1);

        ShortCut sender2 = new ShortCut();
        BattleShipsEngine game2 = new BattleShipsEngine(sender2);

        // connect both games
        sender1.setReceiver(game2);
        sender2.setReceiver(game1);

        // test methods
        game1.doDice();
        game2.doDice();

        BattleShipsUsage activeGame = game1.status() == BattleShipsStatus.SINKS ? game1 : game2;

        activeGame.coordinate(0,0);
        Assert.assertNotSame(activeGame.status(), BattleShipsStatus.CONFIRMR);
    }
}
