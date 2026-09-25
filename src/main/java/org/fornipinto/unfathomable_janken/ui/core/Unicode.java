package org.fornipinto.unfathomable_janken.ui.core;

import org.jline.utils.WCWidth;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Utility class for Unicode code point width calculations and glyph iteration.
 */
public final class Unicode {
    private static final Map<Integer, Integer> CHAR_WIDTH_CACHE = new ConcurrentHashMap<>();

    private Unicode() {
    }

    /**
     * Returns the terminal column width of a Unicode code point.
     * <p>
     * Uses an O(1) fast-path for standard printable ASCII characters and memoizes
     * non-ASCII lookups (such as box-drawing characters and emojis).
     *
     * @param codePoint The Unicode code point.
     * @return The number of terminal columns occupied by the character (0, 1, or 2).
     */
    public static int charWidth(int codePoint) {
        if (codePoint >= 32 && codePoint < 127) {
            return 1;
        }

        return CHAR_WIDTH_CACHE.computeIfAbsent(
            codePoint,
            key -> Math.max(0, WCWidth.wcwidth(key))
        );
    }

    /**
     * Iterates over each Unicode code point in the sequence until the predicate returns {@code false}
     * or the end of the sequence is reached.
     *
     * @param sequence  The character sequence to iterate over.
     * @param predicate Optional callback invoked for each visible glyph (width > 0). Returning
     *                  {@code false} stops the iteration immediately.
     * @return The total display width in terminal columns accumulated before stopping.
     */
    public static int forEachGlyphUntil(CharSequence sequence, GlyphPredicate predicate) {
        var columnOffset = 0;
        var i = 0;

        while (i < sequence.length()) {
            final var codePoint = Character.codePointAt(sequence, i);
            final var charWidth = charWidth(codePoint);

            if (charWidth > 0 && predicate != null) {
                if (!predicate.test(codePoint, columnOffset, charWidth)) {
                    break;
                }
            }

            columnOffset = columnOffset + charWidth;
            i = i + Character.charCount(codePoint);
        }

        return columnOffset;
    }

    /**
     * Iterates over each Unicode code point in the sequence, computing its visual column offset
     * and width.
     *
     * @param sequence The character sequence to iterate over.
     * @param consumer Optional callback invoked for each visible glyph (width > 0).
     */
    public static void forEachGlyph(CharSequence sequence, GlyphConsumer consumer) {
        forEachGlyphUntil(sequence, (codePoint, columnOffset, charWidth) -> {
            if (consumer != null) {
                consumer.accept(codePoint, columnOffset, charWidth);
            }

            return true;
        });
    }

    /**
     * Functional interface for consuming Unicode glyphs along with their visual column position and width.
     */
    @FunctionalInterface
    public interface GlyphConsumer {
        /**
         * Called for each visible Unicode code point in a sequence.
         *
         * @param codePoint    The Unicode code point.
         * @param columnOffset The 0-based visual column offset from the start of the sequence.
         * @param charWidth    The visual width of the code point in columns (1 or 2).
         */
        void accept(int codePoint, int columnOffset, int charWidth);
    }

    /**
     * Functional interface for testing/consuming Unicode glyphs with early termination support.
     */
    @FunctionalInterface
    public interface GlyphPredicate {
        /**
         * Called for each visible Unicode code point in a sequence.
         *
         * @param codePoint    The Unicode code point.
         * @param columnOffset The 0-based visual column offset from the start of the sequence.
         * @param charWidth    The visual width of the code point in columns (1 or 2).
         * @return {@code true} to continue iterating, or {@code false} to stop immediately.
         */
        boolean test(int codePoint, int columnOffset, int charWidth);
    }
}
