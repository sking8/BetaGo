public class Tournament implements StandardRule {

    int[][][] tournamentRank = new int[100][100][50];

    public static void main(String[] args) {
        Chessboard board = new ChessboardImpl();
        Map map = new Map(board);

        // Define players HERE:
        Terminator p2 = new Terminator("HANDSOME", WHITE, 5, 50, 35);
        Terminator p1 = new Terminator("FOOTSOME", BLACK, 5, 50, 35);

        Terminator currentPlayer = p1;

        while (board.judgeLastStep() == 0) {
            try {
                if (currentPlayer == p1) {
                    board.set(p1.move(map), currentPlayer.getColor());
                    currentPlayer = p2;
                } else {
                    board.set(p2.move(map), currentPlayer.getColor());
                    currentPlayer = p1;
                }
                // System.out.println();
            } catch (IllegalArgumentException e) {
                System.out.println("Illegal move, please try again.");
            }
        }

        board.printMap();

        System.out.println("\n===============================\n");
        switch (board.judge()) {
            case -1: {
                System.out.println("Woo-hoo! There's a tie!");
                break;
            }
            case BLACK: {
                System.out.printf("The winner is %s!\n", p1.getName());
                break;
            }
            case WHITE: {
                System.out.printf("The winner is %s!\n", p2.getName());
                break;
            }
        }
        System.out.printf("The length of game is %d\n", board.gameLength());
        System.out.println("\n===============================\n");
    }
}
