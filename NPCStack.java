import java.util.*;
import java.io.*;

/**
 * NPCStack class.
 *
 * NP complete.
 */
public class NPCStack {

    public static void main(String[] args) {
        try {
            if (args.length == 3) {
                String file = args[0];
                int temp = Integer.parseInt(args[1]);
                double rate = Double.parseDouble(args[2]);

                if (temp < 0 || rate < 0.1 || rate > temp) {
                    throw new IllegalArgumentException("Invalid parameters: " + temp + "," + rate);
                }

                List<Box> boxes = getBoxes(file);
                List<Box> orientations = rotateBoxes(boxes);

                int m = boxes.size();
                int n = orientations.size();

                /*
                 * Chooses algorithm based on set size.
                 *
                 * If the size is smaller than 10, then use DP to solve,
                 * otherwise use simulated annealing.
                 */
/*                if (m < 10) {
                    List<Box> dpStack = DP.stack(orientations, m);
                    print(dpStack, boxes.size(), "DP Stack");
                } else {
                    List<Box> saStack = SA.stack(orientations, temp, rate);
                    print(saStack, boxes.size(), "SA Stack");
                }*/
/*                List<Box> dpStack = DP.stack(orientations, m);
                print(dpStack, boxes.size(), "DP Stack");*/

                List<Box> saStack = SA.stack(orientations, temp, rate);
                print(saStack, boxes.size(), "SA Stack");

            } else {
                System.out.println("Usage: java NPCStack <file> <temp> <rate>");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    /**
     * @param   file    The name of the file containing box dimensions.
     * @return  The list of type Box extracted from the file data.
     * @throws  IOException when an issue with the file occurs.
     */
    private static List<Box> getBoxes(String file) throws IOException {
        try (
            BufferedReader reader = new BufferedReader(
                new FileReader(file));
        ) {
            List<Box> boxes = new ArrayList<>();
            int id = 0; /* First box ID is 0 */
            int n = 0;

            String line;
            while ((line = reader.readLine()) != null) {
                /*
                 * Assuming an entry is a line with 3 space-seperated values.
                 * These are the initial dimensions of a box, where each
                 * value (a, box or c) could be its width, length, or height.
                 *
                 * Note:    All invalid entries are ignored but exceptions are
                 *          also printed as an error.
                 */
                String[] dimension = line.split(" ");
                if (dimension.length == 3) {
                    try {
                        int a = Integer.parseInt(dimension[0].trim());
                        int b = Integer.parseInt(dimension[1].trim());
                        int c = Integer.parseInt(dimension[2].trim());
                        if (a > 0 && b > 0 && c > 0) {
                            boxes.add(new Box(id++, a, b, c));
                        }
                        n++;
                    } catch (NumberFormatException e) {
                        System.err.printf("%s (%d): %s", e.getMessage(), n, line);
                    }
                }
            }
            return boxes;
        }
    }


    /**
     * @param   boxes    The list boxes to generate possible rotations.
     * @return  The list of type Box containing rotated versions of boxes.
     */
    public static List<Box> rotateBoxes(List<Box> boxes) {
        List<Box> orientations = new ArrayList<>();
        for (Box box : boxes) {
            /*
             * A version for each possible orientation of a box is
             * added to the list, where the same box ID is used for
             * all three versions of the box, so to avoid breaking
             * the "single use" rule.
             */
            int id = box.ID;
            int a = box.height;
            int b = box.width;
            int c = box.length;

            /*
             * Possible orientations are created by alternating
             * its possible heights (a, box, then c), then setting
             * its width as the largest of the two other values
             * and finally its length as the remaining.
             */
            orientations.add(new Box(id, Math.max(b, c), Math.min(b, c), a));
            orientations.add(new Box(id, Math.max(a, c), Math.min(a, c), b));
            orientations.add(new Box(id, Math.max(a, b), Math.min(a, b), c));
        }

        Collections.sort(orientations);

        return orientations;
    }


    /**
     * Standard print.
     * @param   boxes   The list of Boxes to print their dimensions.
     */
    public static void print(List<Box> stack) {
        System.out.printf("w  l  h  i\n");
        int n = stack.size();
        while (n > 0) {
            System.out.println(stack.get(--n).toString());
        }
    }


    /**
     * Debug print.
     * @param   boxes   The list of Boxes to print their dimensions.
     */
    public static void print(List<Box> stack, int boxCount, String label) {
        //System.out.printf("w  l  h  i\n");
/*        int n = stack.size();
        while (n > 0) {
            System.out.println(stack.get(--n).toString());
        }*/
        System.out.printf("%s (%d/%d boxes, height %d)\n", label, stack.size(), boxCount, measure(stack));
    }


    /**
     * @param   stack   The list of Boxes in a stack to measure total height.
     * @return  The total height of a stack as an integer.
     */
    public static int measure(List<Box> stack) {
        int sum = 0;
        for (Box box : stack) {
            sum += box.height;
        }
        return sum;
    }
}
