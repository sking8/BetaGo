public class Map {
    private Chessboard board;

    public Map(Chessboard board) {
        this.board = board;
    }

    public int get(int x, int y) {
        return board.get(x, y);
    }

    public Coordinate lastStep() {
        return board.lastStep();
    }
}
