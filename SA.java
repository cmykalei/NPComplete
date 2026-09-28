import java.util.*;

/**
 * Simulated Annealing stack class.
 */
public class SA {

    /**
     * Stacks boxes according to constraints given all possible orientations.
     *
     * @param   orientations   The list of sorted possible boxes.
     * @return  The stack of boxes where each strictly fits on the previous.
     */
    static List<Box> stack(List<Box> orientations, int t, double r) {
        Random rand = new Random();
        List<Box> curr = new ArrayList<>(Greedy.stack(orientations));
        List<Box> best = new ArrayList<>(curr);
        int bestHeight = NPCStack.measure(best);
        double temp = (double)t;

        while (t > 0.1) {
            List<Box> next = stackNeighbor(curr, orientations, t);
            int nextHeight = NPCStack.measure(next);
            int currHeight = NPCStack.measure(curr);
            int diff = nextHeight - currHeight;
            double prob = Math.exp(diff / t);
            double random = rand.nextDouble();

            if (diff > 0 || prob > random) {
                curr = clean(next);
                NPCStack.print(best, best.size(), "Temp " + String.valueOf(t));
                if (nextHeight > bestHeight) {
                    best = new ArrayList<>(next);
                    bestHeight = nextHeight;
                } else {
                    curr = next;
                }
            }
            t -= r;
        }
        return best;
    }


    /**
     * Makes random changes to a stack to build a neighboring one.
     * @param   stack           The current stack of boxes.
     * @param   orientations    The possible orientations of all boxes in a set.
     * @return  The new stack, the neighboring stack that was built.
     */
    static List<Box> stackNeighbor(List<Box> stack, List<Box> orientations, int t) {
        List<Box> neighbor = new ArrayList<>(stack);

        int i = 0;
        Random rand = new Random();

        while (i < t) {
            switch (rand.nextInt(2)) {
                case 0:
                    addBox(neighbor, orientations);
                    break;
                case 1:
                    rotateBox(neighbor, orientations);
                    break;
            }
            i++;
        }
        return neighbor;
    }


    /**
     * @param   stack           The stack of boxes to add another box to.
     * @param   orientations    The possible orientations of all boxes in a set.
     */
    static void addBox(List<Box> stack, List<Box> orientations) {
        Set<Integer> used = new HashSet<>();
        for (Box box : stack) used.add(box.ID);

        Box top = null;
        if (!stack.isEmpty()) top = stack.get(stack.size() - 1);

        /*
         * Builds a list of possible candidates to add to the stack.
         * Here we check if they haven't been used and if they fit.
         */
        List<Box> candidates = new ArrayList<>();
        for (Box next : orientations) {
            if (used.contains(next.ID)) continue;
            if (top == null || next.fitsOn(top)) candidates.add(next);
        }

        if (!candidates.isEmpty()) {
            Random rand = new Random();
            Box box = candidates.get(rand.nextInt(candidates.size()));
            stack.add(box);
        }
    }


    /**
     * @param   stack           The stack of boxes to randomly rotate.
     * @param   orientations    The possible orientations of all boxes in a set.
     */
     static void rotateBox(List<Box> stack, List<Box> orientations) {
        if (!stack.isEmpty()) {
            Random rand = new Random();
            List<Box> alternatives = new ArrayList<>();

            int index = rand.nextInt(stack.size());
            Box curr = stack.get(index);

            for (Box box : orientations) {
                if (box.ID == curr.ID && !box.equals(curr)) {
                    alternatives.add(box);
                }
            }

            if (!alternatives.isEmpty()) {
                Box box = alternatives.get(rand.nextInt(alternatives.size()));
                stack.set(index, box);
            }
        }
    }

    /**
     * @param   stack   The stack of boxes to remove one at a random index.
     */
    static List<Box> removeBox(List<Box> stack, List<Box> orientations) {
        Random rand = new Random();
        if (!stack.isEmpty()) {
            stack.remove(rand.nextInt(stack.size()));
            return clean(stack);
        }
        stack.add(orientations.get(rand.nextInt(orientations.size())));
        return stack;
    }


    /**
     * Note:    Not used in this version of the algorithm.
     *
     *          The idea was to use this function at the end of either
     *          stackNeighbor() or stack(), so that in addBox() and
     *          rotateBox() we could avoid checking validity until the
     *          end of the run and perhaps improve solutions/efficiency.
     *
     * @param   stack   The stack of boxes to clean of invalid placements.
     * @return  The cleaned stack where its form satisfies the constraints.
     */
    static List<Box> clean(List<Box> stack) {
        List<Box> cleaned = new ArrayList<>();
        Box prev = null;
        Set<Integer> used = new HashSet<>();

        for (Box box : stack) {
            if (!used.contains(box.ID)) {
                if (prev == null || box.fitsOn(prev)) {
                    cleaned.add(box);
                    used.add(box.ID);
                    prev = box;
                }
            }
        }
        return cleaned;
    }
}
