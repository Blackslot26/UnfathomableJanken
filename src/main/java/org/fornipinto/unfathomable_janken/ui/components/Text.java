package org.fornipinto.unfathomable_janken.ui.components;

import org.fornipinto.unfathomable_janken.core.Size;
import org.fornipinto.unfathomable_janken.ui.core.Canvas;
import org.fornipinto.unfathomable_janken.ui.core.Component;
import org.fornipinto.unfathomable_janken.ui.core.Constraints;
import org.fornipinto.unfathomable_janken.ui.core.Paint;
import org.fornipinto.unfathomable_janken.ui.core.Unicode;

import java.util.ArrayList;

/**
 * A component that displays text content.
 * <p>
 * The component automatically handles line wrapping based on the available width. If a word exceeds the maximum width,
 * it will be truncated and an ellipsis ("…") will be added at the end of the line.
 * <p>
 * The text is drawn using the specified {@link Paint} style.
 */
public class Text extends Component {
    private final String content;
    private final Paint paint;
    private final ArrayList<String> lines = new ArrayList<>();
    private int maximumLineWidth = 0;

    /**
     * Constructs a {@link Text} component with the given content.
     *
     * @param content The text content to display.
     */
    public Text(String content) {
        this.content = content;
        this.paint = new Paint();
    }

    /**
     * Constructs a {@link Text} component with the given content and paint style.
     *
     * @param content The text content to display.
     * @param paint   The paint style to use for rendering the text.
     */
    public Text(String content, Paint paint) {
        this.content = content;
        this.paint = paint;
    }

    @Override
    public void layout(Constraints constraints) {
        computeLines(content, constraints.maxWidth());
        size = constraints.constrain(
            new Size(maximumLineWidth, lines.size())
        );
    }

    private void computeLines(String content, int maxWidth) {
        lines.clear();
        maximumLineWidth = 0;

        if (maxWidth <= 0) return;

        if (content.isEmpty()) {
            lines.add("");
            return;
        }

        final var currentWord = new StringBuilder();
        final var currentLine = new StringBuilder();
        var currentWordWidth = 0;
        var currentLineWidth = 0;
        var index = 0;

        while (true) {
            final var codePoint = index < content.length() ? content.codePointAt(index) : -1;

            if (codePoint == ' ' || codePoint == '\n' || codePoint == -1) {
                // We reached the end of a word
                if (currentLineWidth + currentWordWidth <= maxWidth) {
                    // The word fits in the current line, so we add it
                    currentLine.append(currentWord);
                    currentLineWidth = currentLineWidth + currentWordWidth;
                } else if (currentWordWidth > maxWidth) {
                    // The word is too long to fit in a single line, so we use a line for the entire word and truncate it.
                    currentLineWidth = addLineIfNotEmpty(currentLine, currentLineWidth);
                    currentLineWidth = appendTruncatedWord(currentLine, currentWord, maxWidth);
                    currentLineWidth = addLineIfNotEmpty(currentLine, currentLineWidth);
                } else {
                    // Move to the next line
                    currentLineWidth = addLineIfNotEmpty(currentLine, currentLineWidth);
                    currentLine.append(currentWord);
                    currentLineWidth = currentWordWidth;
                }

                currentWord.setLength(0);
                currentWordWidth = 0;

                if (codePoint == ' ' && currentLineWidth < maxWidth) {
                    currentLine.append(' ');
                    currentLineWidth = currentLineWidth + 1;
                } else if (codePoint == '\n') {
                    addLine(currentLine, currentLineWidth);
                    currentLineWidth = 0;
                }

                if (codePoint == -1) {
                    break;
                }
            } else {
                // We are still in the middle of a word
                final var charWidth = Unicode.charWidth(codePoint);
                currentWord.appendCodePoint(codePoint);
                currentWordWidth = currentWordWidth + charWidth;

                if (currentLineWidth + currentWordWidth > maxWidth) {
                    // Move current word to next line
                    currentLineWidth = addLineIfNotEmpty(currentLine, currentLineWidth);
                }
            }

            index = index + Character.charCount(codePoint);
        }

        addLine(currentLine, currentLineWidth);
    }

    private int appendTruncatedWord(StringBuilder target, CharSequence word, int maxWidth) {
        final var width = Unicode.forEachGlyphUntil(word, (codePoint, columnOffset, charWidth) -> {
            if (columnOffset + charWidth > maxWidth - 1) {
                return false;
            }

            target.appendCodePoint(codePoint);
            return true;
        });

        target.append('…');
        return width + Unicode.charWidth('…');
    }

    private void addLine(StringBuilder currentLine, int currentLineWidth) {
        lines.add(currentLine.toString());
        maximumLineWidth = Math.max(maximumLineWidth, currentLineWidth);
        currentLine.setLength(0);
    }

    private int addLineIfNotEmpty(StringBuilder currentLine, int currentLineWidth) {
        if (!currentLine.toString().trim().isEmpty()) {
            addLine(currentLine, currentLineWidth);
            return 0;
        }

        return currentLineWidth;
    }

    @Override
    public void draw(Canvas canvas) {
        for (int y = 0; y < lines.size(); y++) {
            final var line = lines.get(y);
            final var currentY = y;

            Unicode.forEachGlyph(
                line,
                (codePoint, columnOffset, _) -> canvas.draw(codePoint, columnOffset, currentY, paint)
            );
        }
    }
}
