package cn.com.warlock.emitter;

/**
 * 参数校验
 */
public class ParameterUtil {
    public static void assertParameterWithinBounds(String name, long lower, long upper,
                                                   long parameter) {
        if (parameter < lower || parameter > upper) {
            throw new IllegalArgumentException(String.format(
                "Invalid %s: %d (expected: %d <= n < %d)", name, parameter, lower, upper + 1));
        }
    }

    public static void assertNotNullEightBytes(byte[] bytes) {
        if (bytes == null) {
            throw new IllegalArgumentException("Expected 8 bytes, but got null.");
        }
        if (bytes.length != 8) {
            throw new IllegalArgumentException(
                String.format("Expected 8 bytes, but got: %d bytes.", bytes.length));
        }
    }
}
