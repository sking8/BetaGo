import org.junit.Assert;
import org.junit.Test;

public class AITest implements StandardRule {

    @Test
    public void test1() {
        Player p = new AI(WHITE);
        Chessboard c = new ChessboardImpl();

        c.set(new Coordinate(7, 7), BLACK);
        c.set(new Coordinate(8, 6), WHITE);
        c.set(new Coordinate(7, 6), BLACK);
        c.set(new Coordinate(7, 5), WHITE);
        c.set(new Coordinate(6, 7), BLACK);
        c.set(new Coordinate(8, 5), WHITE);
        c.set(new Coordinate(8, 7), BLACK);
        c.set(new Coordinate(5, 7), WHITE);
        c.set(new Coordinate(9, 7), BLACK);

        Assert.assertTrue(equals(c.lastStep(), new Coordinate(9, 7)));

        c.printMap();

        p.move(new Map(c));
    }


    private boolean equals(Coordinate a, Coordinate b) {
        return a.getXInt() == b.getXInt() && a.getYInt() == b.getYInt();
    }
}
