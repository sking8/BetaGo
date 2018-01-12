import java.util.Scanner;

public class Sam implements Player{

    private int myColor;

    public Sam(int color) {
        myColor = color;
    }

    @Override
    public Coordinate move(Map map) {
        Scanner s = new Scanner(System.in);
        return new Coordinate(s.next().charAt(0), s.next().charAt(0));
    }

    @Override
    public String getName() {
        return "Sam";
    }
}
