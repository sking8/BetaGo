//    The coordinate object store data using integers, but
//      provides constructors and getters for both int and
//      char types.

public class Coordinate implements StandardRule {

    private int x;
    private int y;

//    Coordinates are checked before being constructed.
    public Coordinate(int x, int y) {
        if (!isLegal(x, y)) {
            throw new IllegalArgumentException("Illegal coordinate.");
        }
        this.x = x;
        this.y = y;
    }
    public Coordinate(char x, char y) {
        if (!isLegal(x, y)) {
            throw new IllegalArgumentException("Illegal coordinate.");
        }
        this.x = toInt(x);
        this.y = toInt(y);
    }

    public int getXInt() {
        return x;
    }
    public int getYInt() {
        return y;
    }

    public char getXChar() {
        return toChar(x);
    }
    public char getYChar() {
        return toChar(y);
    }

    public static char toChar(int i) {
        if (i >= 0 && i <= 9) {
            return (char)(i + '0');
        } else {
            return (char)(i + 'A' - 10);
        }
    }
    public static int toInt(char c) {
        if (c >= '0' && c <= '9') {
            return c - '0';
        } else if (c >= 'A' && c <= 'Z') {
            return c - 'A' + 10;
        } else {
            return c - 'a' + 10;
        }
    }
}
