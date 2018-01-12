public class Main implements StandardRule {

    public static void main(String[] args) {

        Chessboard board = new ChessboardImpl();
        Map map = new Map(board);

        // Define players HERE:
        Player p1 = new AI(BLACK);
        Player p2 = new AI2(WHITE);

        int currentPlayer = BLACK;
        boolean handlingException = false;

        while (board.judgeLastStep() == 0) {
            if (!handlingException) {
                board.printMap();
                System.out.printf("It's %s's turn.\n",
                        currentPlayer == BLACK ? p1.getName() : p2.getName());
            }
            try {
                if (currentPlayer == BLACK) {
                    board.set(p1.move(map), currentPlayer);
                    currentPlayer = WHITE;
                } else {
                    board.set(p2.move(map), currentPlayer);
                    currentPlayer = BLACK;
                }
                System.out.println();
                handlingException = false;
            } catch (IllegalArgumentException e) {
                System.out.println("Illegal move, please try again.");
                handlingException = true;
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
        System.out.println("\n===============================\n");
    }
}