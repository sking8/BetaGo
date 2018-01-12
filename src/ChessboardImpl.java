import java.util.ArrayList;
import java.util.List;

public class ChessboardImpl implements Chessboard {
    private int[][] map;
    private List<Coordinate> log;

    public ChessboardImpl() {
        map = new int[L][L];
        log = new ArrayList<Coordinate>();
    }

    @Override
    public int judge() {
        boolean boardIsFull = true;

        for (int i = 0; i < L; i++) {
            for (int j = 0; j < L; j++) {
                if (get(i, j) == SPACE) {
                    boardIsFull = false;
                    continue;
                }
                for (int dir = 0; dir < 4; dir++) {
                    if (count(i, j, dir) >= 5) {
                        return get(i, j);
                    }
                }
            }
        }

        if (boardIsFull) {
            return -1;
        } else {
            return 0;
        }
    }

    @Override
    public int judgeLastStep() {
        if (log.isEmpty()) {
            return 0;
        }

        int x = log.get(log.size() - 1).getXInt();
        int y = log.get(log.size() - 1).getYInt();

        // Only valid when dx[dir] and dy[dir] are exactly
        // opposite to dx[dir + 4] and dy[dir + 4]. Check
        // StandardRule interface before utilizing this method.
        for (int dir = 0; dir < 4; dir++) {
            if (count(x, y, dir) + count(x, y, dir+4) - 1>= 5) {
                return get(x, y);
            }
        }

        if (log.size() == L * L) {
            return -1;
        } else {
            return 0;
        }
    }

    @Override
    public int get(int x, int y) {
        if (!isLegal(x, y)) {
            throw new IllegalArgumentException("Illegal inquiry coordinates.");
        }
        return map[x][y];
    }


//    After set the color at indicated coordinate, record its position in log.
    @Override
    public void set(Coordinate c, int color) {
        if (get(c.getXInt(), c.getYInt()) != SPACE) {
            throw new IllegalArgumentException("Illegal move: space already occupied.");
        }
        if (color != BLACK && color != WHITE)
        {
            throw new IllegalArgumentException("Illegal move: illegal color.");
        }
        map[c.getXInt()][c.getYInt()] = color;
        log.add(c);
    }

    @Override
    public Coordinate lastStep() {
        if (log.isEmpty()) {
            return null;
        } else {
            return log.get(log.size() - 1);
        }
    }

    @Override
    public int gameLength() {
        return log.size();
    }

    @Override
    public void printMap() {
        System.out.printf("& ");
        for (int i = 0; i < L; i++) {
            System.out.printf("%c ", Coordinate.toChar(i));
        }
        System.out.println();
        for (int i = 0; i < L; i++) {
            System.out.printf("%c ", Coordinate.toChar(i));
            for (int j = 0; j < L; j++) {
                switch (map[i][j]) {
                    case SPACE: {
                        System.out.printf("  ");
                        break;
                    }
                    case BLACK: {
                        System.out.printf("X ");
                        break;
                    }
                    case WHITE: {
                        System.out.printf("O ");
                        break;
                    }
                }
            }
            System.out.println();
        }
    }

//    Count the max number of same color at (x, y) in direction dir.
//      The direction arrays are defined in StandardRule interface.
    private int count(int x, int y, int dir) {
        if (get(x, y) == SPACE) {
            return 0;
        } else {
            int count = 0;
            int cx = x;
            int cy = y;
            while (isLegal(cx, cy) && get(cx, cy) == get(x, y)) {
                count++;
                cx += dx[dir];
                cy += dy[dir];
            }
            return count;
        }
    }
}
