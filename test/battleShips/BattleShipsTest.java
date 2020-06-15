package battleShips;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class BattleShipsTest {

    @Test
    public void usageTest() throws IOException, StatusException, BattleShipsException {
        ShortCut sender1 = new ShortCut();
        BattleShipsEngine game1 = new BattleShipsEngine(sender1);

        ShortCut sender2 = new ShortCut();
        BattleShipsEngine game2 = new BattleShipsEngine(sender2);

        // connect both games
        sender1.setReceiver(game2);
        sender1.setReceiver(game1);

        // test methods
        game1.doDice();
        game2.doDice();

        BattleShipsUsage shootingGame = game1.status() == BattleShipsStatus.SINKS ? game1 : game2;

        shootingGame.coordinate(0, 0);
        Assert.assertSame(game1.status(), BattleShipsStatus.CONFIRMR);

    }
}
