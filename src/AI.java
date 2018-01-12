//    Should be able to throw an exception when asked to move
//      while there're no available spaces on the board.

public class AI implements Player {
    private int myColor;

    public AI(int color) {
        myColor = color;
    }

    @Override
    public String getName() {
        return "AI";
    }

    private int O() {
        return myColor == BLACK ? WHITE : BLACK;
    }
    private int M() {
        return myColor;
    }

    /**
     *  Followings are the constants needed for the evaluation process.
     */

    private static final int INF = (int)2e9;

    private static final int L_4 = (int)1e8;  // four in a line
    private static final int L_3 = (int)1e5;  // three in a line
    private static final int L_2 = (int)1e3;  // two in a line
    private static final int L_1 = (int)1e1;  // one in a line

    // This parameter shouldn't exceed 10.
    private static final double OPEN_BONUS = 5;

    // This parameter varies between 0 to 1.
    // A division penalty greater than 0.1 is depreciated.
    private static final double DIVISION_PENALTY = 0.01;

    // weights should vary between 0 to 1.
    private static final double OFFENSE_WEIGHT = 0.7;
    private static final double DEFENSE_WEIGHT = 1.0;

    /**
     *  Constant fields end.
     */

    // Recording scores of the map
    private double[][] scoreDefense = new double[L][L];
    private double[][] scoreOffense = new double[L][L];
    private double[][] scoreFinal = new double[L][L];

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

        for (int i = 0; i < L; i++) {
            for (int j = 0; j < L; j++) {
                scoreFinal[i][j] = scoreDefense[i][j] + scoreOffense[i][j];
            }
        }

        Coordinate m = max(scoreFinal);
        System.out.printf("%d %d\n", m.getXInt(), m.getYInt());
        return m;
    }

    // Evaluate the entire map
    private void evaluateMap(double[][] score, int color, double weight) {
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
    // This method is super redundant and definitely needs rework.
    private double evaluate(int x, int y, int color) {
        double score = 0;

        // calculate in-a-line value
        for (int dir = 0; dir < 4; dir++) {
            int cx_1 = x + dx[dir], cy_1 = y + dy[dir];
            int cx_2 = x - dx[dir], cy_2 = y - dy[dir];
            int count_1 = count(cx_1, cy_1, dir,   color);
            int count_2 = count(cx_2, cy_2, dir+4, color);


            // Add DIRECT value
            score += basicScore(count_1 + count_2);

            if (isOpen(cx_1, cy_1, dir,   color) &&
                isOpen(cx_2, cy_2, dir+4, color)) {
                score += OPEN_BONUS * basicScore(count_1 + count_2);
            } else if (isOpen(cx_1, cy_1, dir,   color)) {
                score += OPEN_BONUS * basicScore(count_1);
            } else if (isOpen(cx_2, cy_2, dir+4, color)) {
                score += OPEN_BONUS * basicScore(count_2);
            }

            /*
            if (count_1 > 0 && count_2 > 0) {
                // Add DIRECT value
                score += basicScore(count_1 + count_2);

                if (isOpen(cx_1, cy_1, dir,   color) &&
                    isOpen(cx_2, cy_2, dir+4, color)) {
                    score += OPEN_BONUS * basicScore(count_1 + count_2);
                } else if (isOpen(cx_1, cy_1, dir,   color)) {
                    score += OPEN_BONUS * basicScore(count_1);
                } else if (isOpen(cx_2, cy_2, dir+4, color)) {
                    score += OPEN_BONUS * basicScore(count_2);
                }
            } else if (count_1 > 0) {
                // Add DIRECT value
                score += basicScore(count_1);
                if (isOpen(cx_1, cy_1, dir, color)) {
                    score += OPEN_BONUS * basicScore(count_1);
                }

                cx_2 = x - 2*dx[dir];
                cy_2 = y - 2*dy[dir];
                count_2 = count(cx_2, cy_2, dir+4, color);
                if (count_2 > 0) {
                    score += DIVISION_PENALTY * basicScore(count_1 + count_2);
                    if (isOpen(cx_1, cy_1, dir,   color) &&
                        isOpen(cx_2, cy_2, dir+4, color)) {
                        score += DIVISION_PENALTY * OPEN_BONUS * basicScore(count_1 + count_2);
                    } else if (isOpen(cx_1, cy_1, dir,   color)) {
                        score += DIVISION_PENALTY * OPEN_BONUS * basicScore(count_1);
                    } else if (isOpen(cx_2, cy_2, dir+4, color)) {
                        score += DIVISION_PENALTY * OPEN_BONUS * basicScore(count_2);
                    }
                }
            } else if (count_2 > 0) {
                // Add DIRECT value
                score += basicScore(count_2);
                if (isOpen(cx_2, cy_2, dir+4, color)) {
                    score += OPEN_BONUS * basicScore(count_2);
                }

                cx_1 = x + 2*dx[dir];
                cy_1 = y + 2*dy[dir];
                count_1 = count(cx_1, cy_1, dir, color);
                if (count_1 > 0) {
                    score += DIVISION_PENALTY * basicScore(count_1 + count_2);
                    if (isOpen(cx_1, cy_1, dir,   color) &&
                        isOpen(cx_2, cy_2, dir+4, color)) {
                        score += DIVISION_PENALTY * OPEN_BONUS * basicScore(count_1 + count_2);
                    } else if (isOpen(cx_1, cy_1, dir,   color)) {
                        score += DIVISION_PENALTY * OPEN_BONUS * basicScore(count_1);
                    } else if (isOpen(cx_2, cy_2, dir+4, color)) {
                        score += DIVISION_PENALTY * OPEN_BONUS * basicScore(count_2);
                    }
                }
            } else {
                cx_1 = x + 2*dx[dir];
                cy_1 = y + 2*dy[dir];
                count_1 = count(cx_1, cy_1, dir,   color);
                score += DIVISION_PENALTY * basicScore(count_1);
                if (isOpen(cx_1, cy_1, dir, color)) {
                    score += DIVISION_PENALTY * OPEN_BONUS * basicScore(count_1);
                }

                cx_2 = x - 2*dx[dir];
                cy_2 = y - 2*dy[dir];
                count_2 = count(cx_2, cy_2, dir+4, color);
                score += DIVISION_PENALTY * basicScore(count_2);
                if (isOpen(cx_2, cy_2, dir+4, color)) {
                    score += DIVISION_PENALTY * OPEN_BONUS * basicScore(count_2);
                }

                if (count_1 > 0 && count_2 > 0) {
                    score += DIVISION_PENALTY * DIVISION_PENALTY * basicScore(count_1 + count_2);
                    if (isOpen(cx_1, cy_1, dir,   color) &&
                                isOpen(cx_2, cy_2, dir+4, color)) {
                        score += DIVISION_PENALTY * DIVISION_PENALTY * OPEN_BONUS * basicScore(count_1 + count_2);
                    } else if (isOpen(cx_1, cy_1, dir,   color)) {
                        score += DIVISION_PENALTY * DIVISION_PENALTY * OPEN_BONUS * basicScore(count_1);
                    } else if (isOpen(cx_2, cy_2, dir+4, color)) {
                        score += DIVISION_PENALTY * DIVISION_PENALTY * OPEN_BONUS * basicScore(count_2);
                    }
                }
            }
            */
        }

        // calculate double-three/double-four/four-three value
        for (int dir_1 = 0; dir_1 < 8; dir_1++) {
            for (int dir_2 = dir_1 + 1; dir_2 < 8; dir_2++) {
                int cx_1 = x + dx[dir_1], cy_1 = y + dy[dir_1];
                int cx_2 = x + dx[dir_2], cy_2 = y + dy[dir_2];
                int count_1 = count(cx_1, cy_1, dir_1, color);
                int count_2 = count(cx_2, cy_2, dir_2, color);
                score += basicScore(count_1 + count_2);

                if (isOpen(cx_1, cy_1, dir_1, color) &&
                    isOpen(cx_2, cy_2, dir_2, color)) {
                    score += OPEN_BONUS * basicScore(count_1 + count_2);
                }
            }
        }

        return score;
    }

    // check whether the other end of a consecutive line of pieces
    private boolean isOpen(int x, int y, int dir, int color) {
        int cx = x;
        int cy = y;
        while (isLegal(cx, cy) && map.get(cx, cy) == color) {
            cx += dx[dir];
            cy += dy[dir];
        }
        // first ensure (cx, cy) is legal to avoid causing exceptions
        if (isLegal(cx, cy)) {
            if (map.get(cx, cy) == SPACE) {
                return true;
            }
        }
        return false;
    }

    // calculate the basic score based on count.
    private int basicScore(int count) {
        if (count >= 4) {
            return L_4;
        } else if (count >= 3) {
            return L_3;
        } else if (count >= 2) {
            return L_2;
        } else if (count >= 1) {
            return L_1;
        } else {
            return 0;
        }
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
    private int count(int x, int y, int dir, int color) {
        int count = 0;
        int cx = x;
        int cy = y;
        while (isLegal(cx, cy) && map.get(cx, cy) == color) {
            count++;
            cx += dx[dir];
            cy += dy[dir];
        }
        return count;
    }
    private int count(Coordinate c, int dir, int color) {
        return count(c.getXInt(), c.getYInt(), dir, color);
    }

    // find the spot in the entire map with largest score
    private Coordinate max(double[][] score) {
        double max = -INF;
        int x = -1;
        int y = -1;
        for (int i = 0; i < L; i++) {
            for (int j = 0; j < L; j++) {
                if (score[i][j] > max) {
                    max = score[i][j];
                    x = i;
                    y = j;
                }
            }
        }
        return new Coordinate(x, y);
    }
}
