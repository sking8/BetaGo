public interface Chessboard extends StandardRule {

    // Return 0 if the game is still in process.
    // Return -1 if the game is in tie.
    // Return the winner otherwise.
    int judge();
    int judgeLastStep();

    int get(int x, int y);

    // Set a move based on the position and the color.
    // Record it on the log.
    void set(Coordinate c, int color);

    // Return the coordinate of last move.
    Coordinate lastStep();
    int gameLength();

    void printMap();
}
