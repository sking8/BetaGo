import java.util.Scanner;

public class Wyatt implements Player{

    private int myColor;

    public Wyatt(int color) {
        myColor = color;
    }

    @Override
    public Coordinate move(Map map) {
        Scanner s = new Scanner(System.in);
        return new Coordinate(s.next().charAt(0), s.next().charAt(0));
    }

    @Override
    public String getName() {
        return "Wyatt";
    }
}
