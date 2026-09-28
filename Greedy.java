import java.util.*;

/**
 * Greedy stack class.
 */
public class Greedy {

    /**
     * Stacks boxes according to constraints given all possible orientations.
     *
     * @param   orientations   The list of sorted possible boxes.
     * @return  The stack of boxes where each strictly fits on the previous.
     */
    public static List<Box> stack(List<Box> orientations) {
        Set<Integer> used = new HashSet<>();
        List<Box> stack = new ArrayList<>();
        for (Box next : orientations) {
            if (!used.contains(next.ID)) {
                if (stack.isEmpty() || next.fitsOn(stack.get(stack.size() - 1))) {
                    used.add(next.ID);
                    stack.add(next);
                }
            }
        }
        return stack;
    }
}