package helpers;

/**
 * A Class created to help other classes get random numbers
 */
public class RandomNumberHelper {

    /**
     * Create a random int between two ints
     * @param start - the min for the random number
     * @param end - the max for the random number
     * @return A random int between the start and end
     */
    public static int randBetween(int start, int end) {

        return start + (int)Math.round(Math.random() * (end - start));
    }

}
