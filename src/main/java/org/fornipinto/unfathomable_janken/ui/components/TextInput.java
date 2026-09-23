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
        final var children = new ArrayList<Component>() {{
            add(new Text(text, backgroundPaint));

            if (active) {
                add(new Text("_", backgroundPaint.withBlink(true)));
            }
        }}.toArray(Component[]::new);

        return new Box(
            new Row(
                children
            )
        ).withPaint(backgroundPaint);
    }
}
