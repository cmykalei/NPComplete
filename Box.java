import java.util.Objects;

/**
 * Box class.
 */
public class Box implements Comparable<Box> {

    public final int ID;
    public final int width;
    public final int length;
    public final int height;
    public final int area;

    /**
     * @param   id      The unique ID of a box.
     * @param   width   The width of the box, the first value.
     * @param   length  The length of the box, the second value.
     * @param   height  The height of the box, the third value.
     */
    public Box(int id, int width, int length, int height) {
        this.ID = id;
        this.width = width;
        this.length = length;
        this.height = height;
        this.area = width * length; /* To quickly sort the initial stack */
    }


    /**
     * @param   other   The other box to compare its dimensions.
     * @return  True if this box fits on the next, False otherwise.
     */
    public boolean fitsOn(Box other) {
        return this.width < other.width && this.length < other.length;
    }

        /**
     * @param   other   The other box to compare its dimensions.
     * @return  True if this box fits on the next, False otherwise.
     */
    public boolean mightFitOn(Box other) {
        return this.area <= other.area;
    }


    /**
     * @param   other   The other box to compare its dimensions.
     * @return  +1 if this < other, 0 if this = other, or -1 if this > other.
     */
    @Override
    public int compareTo(Box other) {
        return Integer.compare(other.area, this.area); /* Descending */
    }


    /**
     * @param   o   The Object to check if it equals to this Box.
     * @return  True if the relevant conditions to be equal are satisfied, False otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof Box)) {
            return false;
        } else {
            Box other = (Box) o;
            return ID == other.ID && width == other.width && length == other.length && height == other.height;
        }
    }


    @Override
    public int hashCode() {
        return Objects.hash(ID, width, length, height);
    }


    /**
     * @return  Each dimension of this box seperated with a space.
     */
    @Override
    public String toString() {
        return String.format("%-2d %-2d %-2d %-2d", this.width, this.length, this.height, this.ID);
    }

}
