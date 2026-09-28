import java.util.*;

/*
 * DP stack class.
 * 
 * Demonstrates DP algorithm for box stacking problem.
 * 
 * Only suitable for sets of around 15 items (source: https://usaco.guide/gold/dp-bitmasks?lang=cpp)
 * Satisfies touching faces and single-use rule with bitmasking.
 */
public class DP {

    /**
     * @param   orientations    The list of all possible orientations for a set of boxes.
     * @param   count           The count of unique boxes in a set.
     * @return  The tallest stack of boxes, the proposed optimal solution.
     */
    public static List<Box> stack(List<Box> orientations, int count) {
        /*
         * Each subset can be represented by a unique bitmask
         * where each bit corresponds to a box.
         *
         * For example, if we have 3 boxes,
         * then we have possible states where:
         *
         *      mask '0b00' means no box was used,
         *      mask '0b01' means box 0 was used,
         *      mask '0b10' means box 1 was used,
         *      mask '0b11' means both boxes were used.
         *      (note: I used bitwise so m could be int and not double)
         */
        int n = orientations.size();
        int m = 1 << count;

        /*
         * Finding the optimal solution means exploring all of them,
         * which means building stacks that satisfy the constraints
         * then comparing them to find the tallest.
         *
         * To keep track of states we can use 2D arrays:
         *
         *      best[n][m] records the achieved height at some [n]
         *      prev[n][m] records the the rotation that led to best[n][m],
         *      used[n][m] records the previous mask for prev[n][m].
         */
        int[][] best = new int[n][m];
        int[][] prev = new int[n][m];
        int[][] used = new int[n][m];

        /* Initialise all as unvisited */
        for (int[] row : best) Arrays.fill(row, -1);
        for (int[] row : prev) Arrays.fill(row, -1);
        for (int[] row : used) Arrays.fill(row, -1);

        /*
         * This sets up the base case for all possible stacks.
         *
         * Each orientation at [i] gives a starting point, i.e. the first box
         * in a potential stack. Its height is recorded and its ID gives the
         * bitmask (boxes used) for this state.
         */
        for (int i = 0; i < n; i++) {
            int mask = 1 << orientations.get(i).ID;
            best[i][mask] = orientations.get(i).height;
        }

        /*
         * This is where we use DP to build all possible stacks.
         *
         * We're looking for best[i][mask] where [mask] is a set of used boxes
         * for a valid stack with orientation [i] as its top box.
         *
         * If there is a stack at this state then we can try to place another box
         * on top by considering all the possible orientations again.
         */
        for (int mask = 0; mask < m; mask++) {
            for (int i = 0; i < n; i++) {

                if (best[i][mask] != -1) {
                    Box curr = orientations.get(i);

                    for (int j = 0; j < n; j++) {
                        Box next = orientations.get(j);
                        int nextBit = 1 << next.ID;

                        /*
                         * If the box is unused and that it fits, then we
                         * can place it on top and see if it improves the stack.
                         *
                         * In other words: if the new stack is taller than the
                         * current best for this combination, then we record it
                         * by updating the table with the rotation that led to
                         * this state and the mask for the boxes that formed it.
                         */
                        if ((mask & nextBit) == 0 && next.fitsOn(curr)) {
                            int nextMask = mask | nextBit;
                            int nextHeight = best[i][mask] + next.height;

                            if (nextHeight > best[j][nextMask]) {
                                best[j][nextMask] = nextHeight;
                                prev[j][nextMask] = i;
                                used[j][nextMask] = mask;
                            }
                        }
                    }
                }
            }
        }


        /*
         * Then find the tallest stack and set variables we'll use to recreate
         * proposed optimal solution.
         */
        int bestHeight = -1;
        int bestIndex = -1;
        int bestMask = -1;
        for (int mask = 0; mask < m; mask++) {
            for (int i = 0; i < n; i++) {
                if (best[i][mask] > bestHeight) {
                    bestHeight = best[i][mask];
                    bestIndex = i;
                    bestMask = mask;
                }
            }
        }

        /*
         * The above algorithm helped us decide on the way to get the tallest stack,
         * and so we need to use back-tracking (with a sequence of previous states)
         * to actually construct it.
         */
        List<Box> stack = new ArrayList<>();
        int currIndex = bestIndex;
        int currMask = bestMask;
        while (currIndex != -1 && currMask != 0) {
            stack.add(orientations.get(currIndex));

            int nextIndex = prev[currIndex][currMask];
            int nextMask = used[currIndex][currMask];

            currIndex = nextIndex;
            currMask = nextMask;
        }

        Collections.reverse(stack); /* TODO: Is this the right way up? */
        return stack;
    }

}