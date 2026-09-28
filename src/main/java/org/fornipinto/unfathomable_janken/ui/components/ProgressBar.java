package org.fornipinto.unfathomable_janken.ui.components;

import org.fornipinto.unfathomable_janken.ui.core.ColorPalette;
import org.fornipinto.unfathomable_janken.ui.core.Component;
import org.fornipinto.unfathomable_janken.ui.core.Composent;
import org.fornipinto.unfathomable_janken.ui.core.Paint;

/**
 * A ProgressBar is a component that visually represents a value between 0 and 1 as a horizontal bar.
 */
public class ProgressBar extends Composent {
    private final double value;
    private final Paint paint;
    private static final Paint DEFAULT_PAINT = new Paint().withBackgroundColor(ColorPalette.GUNMETAL);

    /**
     * Creates a new ProgressBar with the given value.
     *
     * @param value A double between 0 and 1 representing the progress to be displayed.
     */
    public ProgressBar(double value) {
        this(value, DEFAULT_PAINT);
    }

    /**
     * Creates a new ProgressBar with the given value and paint.
     *
     * @param value A double between 0 and 1 representing the progress to be displayed.
     * @param paint The paint to use for the progress bar.
     */
    public ProgressBar(double value, Paint paint) {
        assert value >= 0.0 && value <= 1.0 : "Value must be between 0.0 and 1.0. Got " + value;
        this.value = Math.clamp(value, 0.0, 1.0);
        this.paint = paint;
    }

    @Override
    public Component build() {
        return new LayoutBuilder(
            constraints -> {
                final var maxWidth = constraints.hasBoundedWidth()
                    ? constraints.maxWidth()
                    : constraints.minWidth();
                final var fullCharsCount = getFullCharsCount(maxWidth);
                final var emptyCharsCount = getEmptyCharsCount(maxWidth);
                final var inProgressChar = getInProgressChar(maxWidth);

                final var progressBarString =
                    "█".repeat(Math.max(0, fullCharsCount)) +
                        (fullCharsCount < maxWidth ? inProgressChar : "") +
                        " ".repeat(Math.max(0, emptyCharsCount));

                return new Text(progressBarString, paint);
            }
        );
    }

    private int getFullCharsCount(int maxWidth) {
        return (int) (value * maxWidth);
    }

    private int getEmptyCharsCount(int maxWidth) {
        return maxWidth - getFullCharsCount(maxWidth) - 1;
    }

    private char getInProgressChar(int maxWidth) {
        final var fullValue = value * maxWidth;
        final var integerPart = (int) fullValue;
        final var decimalPart = fullValue - integerPart;

        if (decimalPart >= 0.875) {
            return '▉';
        } else if (decimalPart >= 0.75) {
            return '▊';
        } else if (decimalPart >= 0.625) {
            return '▋';
        } else if (decimalPart >= 0.5) {
            return '▌';
        } else if (decimalPart >= 0.375) {
            return '▍';
        } else if (decimalPart >= 0.25) {
            return '▎';
        } else if (decimalPart >= 0.125) {
            return '▏';
        } else {
            return ' ';
        }
    }
}
