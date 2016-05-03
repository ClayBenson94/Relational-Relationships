package helpers;

public class RandomNumberHelper {
    public static int randBetween(int start, int end) {
        return start + (int)Math.round(Math.random() * (end - start));
    }

    public static long randBetween(long start, long end) {
        return start + (int)Math.round(Math.random() * (end - start));
    }
}
