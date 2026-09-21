package org.fornipinto.unfathomable_janken.ui.components;

import org.fornipinto.unfathomable_janken.core.Size;
import org.fornipinto.unfathomable_janken.ui.core.Canvas;
import org.fornipinto.unfathomable_janken.ui.core.Component;
import org.fornipinto.unfathomable_janken.ui.core.Constraints;

/**
 * A vertical divider component that draws a vertical line.
 */
public final class VerticalDivider extends Component {
    @Override
    public void layout(Constraints constraints) {
        size = new Size(1, constraints.maxHeight());
    }

    @Override
    public void draw(Canvas canvas) {
        for (int y = 0; y < size.height(); y++) {
            canvas.draw('│', 0, y);
        }
    }
}
