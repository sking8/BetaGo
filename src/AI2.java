//    Should be able to throw an exception when asked to move
//      while there're no available spaces on the board.

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AI2 implements Player {
    private int myColor;

    public AI2(int color) {
        myColor = color;
    }

    @Override
    public String getName() {
        return "AI2";
    }

    private int O() {
        return myColor == BLACK ? WHITE : BLACK;
    }
    private int M() {
        return myColor;
    }

    // Following are the constant fields needed for the evaluation precess

    private static final int INF = (int)2e9;

    // weights should vary between 0 to 1.
    private static final int OFFENSE_WEIGHT = 1;
    private static final int DEFENSE_WEIGHT = 1;

    private static final int BLANK = 2;
    private static final int CONNECTED_PIECE = 8;
    private static final int SEPARATED_PIECE = 5;

    // Constant fields end.

    // Recording scores of the map
    private long[][] scoreDefense = new long[L][L];
    private long[][] scoreOffense = new long[L][L];
    private long[][] scoreFinal = new long[L][L];

    // Store the distance from a space to nearest piece;
    private int[][] distance = new int[L][L];

    private Map map;

    @Override
    public Coordinate move(Map map) {
        this.map = map;

        if (map.lastStep() == null) {
            return new Coordinate(L/2, L/2);
        }

        updateDistance(map.lastStep());

        evaluateMap(scoreDefense, O(), DEFENSE_WEIGHT);
        evaluateMap(scoreOffense, M(), OFFENSE_WEIGHT);

        // printScore_1();
        // printScore_2();

        for (int i = 0; i < L; i++) {
            for (int j = 0; j < L; j++) {
                scoreFinal[i][j] = scoreDefense[i][j] + scoreOffense[i][j];
            }
        }

        Coordinate m = max(scoreFinal);
        System.out.printf("%c %c\n", m.getXChar(), m.getYChar());
        return m;
    }

    // Evaluate the entire map
    private void evaluateMap(long[][] score, int color, int weight) {
        // immediately next to the space being evaluated.
        for (int i = 0; i < L; i++) for (int j = 0; j < L; j++) {
            score[i][j] = 0;

            if (map.get(i, j) != SPACE || distance[i][j] > 3) {
                continue;
            }
            score[i][j] = weight * evaluate(i, j, color);
        }
    }

    // Evaluate a space's offense value to "color".
    private long evaluate(int x, int y, int color) {
        long score = 1;

        if (x == 6 && y == 6) {
            score = 1;
        }

        // calculate in-a-line value
        for (int dir = 0; dir < 4; dir++) {
            score += count(x + dx[dir], y + dy[dir], dir,   color) *
                     count(x - dx[dir], y - dy[dir], dir+4, color);
        }
        return score;
    }

    // Update all distances based on last move
    private void updateDistance(int x, int y) {
        for (int i = 0; i < L; i++) for (int j = 0; j < L; j++) {
            distance[i][j] = Math.min(distance[i][j],
                    Math.max(Math.abs(i - x), Math.abs(j - y)));
        }
    }
    private void updateDistance(Coordinate c) {
        updateDistance(c.getXInt(), c.getYInt());
    }

    // Count the number of consecutive pieces of "color"
    // at (x, y) in direction "dir".
    private long count(int x, int y, int dir, int color) {
        long count = 1;
        boolean separated = false;
        for (int i = 0; i < 4; i++) {
            int cx = x + i * dx[dir];
            int cy = y + i * dy[dir];
            if (!isLegal(cx, cy)) {
                break;
            }
            if (map.get(cx, cy) == SPACE) {
                count *= BLANK;
                separated = true;
            } else if (map.get(cx, cy) == color) {
                count *= separated ? SEPARATED_PIECE : CONNECTED_PIECE;
            } else {
                break;
            }
        }
        return count;
    }
    private long count(Coordinate c, int dir, int color) {
        return count(c.getXInt(), c.getYInt(), dir, color);
    }

    List<Coordinate> maxList = new ArrayList<>();
    Random random = new Random();
    // find the spot in the entire map with largest score
    private Coordinate max(long[][] score) {
        long max = -INF;
        for (int i = 0; i < L; i++) {
            for (int j = 0; j < L; j++) {
                if (score[i][j] > max) {
                    maxList.clear();
                    max = score[i][j];
                    maxList.add(new Coordinate(i, j));
                } else if (score[i][j] == max) {
                    maxList.add(new Coordinate(i, j));
                }
            }
        }
        return maxList.get(0);
    }
}
