package org.fornipinto.unfathomable_janken.ui.components;

import org.fornipinto.unfathomable_janken.ui.core.ColorPalette;
import org.fornipinto.unfathomable_janken.ui.core.Component;
import org.fornipinto.unfathomable_janken.ui.core.Composent;
import org.fornipinto.unfathomable_janken.ui.core.Paint;

import java.util.ArrayList;

/**
 * A component that represents a text input field.
 * <p>
 * The component displays the specified text and an optional blinking cursor if the input field is active.
 * The background color of the input field is set to apricot.
 */
public class TextInput extends Composent {
    static private final Paint backgroundPaint = new Paint().withBackgroundColor(ColorPalette.GUNMETAL);

    private final String text;
    private final boolean active;

    /**
     * Constructs a new {@link TextInput} component with the specified text and active state.
     *
     * @param text   The text to display in the input field.
     * @param active A boolean indicating whether the input field is active (true) or inactive (false).
     */
    public TextInput(String text, boolean active) {
        this.text = text;
        this.active = active;
    }

    @Override
    public Component build() {
        return new LayoutBuilder(constraints -> {
            final var maxWidth = constraints.maxWidth();

            final var children = new ArrayList<Component>() {{
                final var maxTextLength = maxWidth - 1;
                final var isTextTooLong = text.length() > maxTextLength;

                if (isTextTooLong) {
                    add(new Text("◀", backgroundPaint));
                }

                final var textStartIndex = Math.max(0, text.length() - maxTextLength + (isTextTooLong ? 1 : 0));
                final var textEndIndex = text.length();
                final var textTail = text.substring(textStartIndex, textEndIndex);

                add(new Text(textTail, backgroundPaint));

                if (active) {
                    add(new Text("_", backgroundPaint.withBlink(true)));
                }
            }}.toArray(Component[]::new);

            return new Box(
                new Row(children)
            ).withPaint(backgroundPaint);
        });
    }
}
