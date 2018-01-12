//    This interface provides basic rules for all other objects
//      in the project, including global color correspondence,
//      boundary of the chessboard, a movement array constant,
//      and any potential additions.

public interface StandardRule {

//    Keep L less than 36
    int L = 15;

    int SPACE = 0;
    int BLACK = 1;
    int WHITE = 2;

    int[] dx = {1, 1, 0, -1, -1, -1, 0, 1};
    int[] dy = {0, 1, 1, 1, 0, -1, -1, -1};

    default boolean isLegal(int i) {
        return i >= 0 && i < L;
    }
    default boolean isLegal(char c) {
        return (c >= '0' && c <= '9') ||
               (c >= 'A' && c < 'A' + L - 10) ||
               (c >= 'a' && c < 'a' + L - 10);
    }
    default boolean isLegal(int a, int b) {
        return isLegal(a) && isLegal(b);
    }
    default boolean isLegal(char a, char b) {
        return isLegal(a) && isLegal(b);
    }
}
