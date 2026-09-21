package org.fornipinto.unfathomable_janken.ui.components;

import org.fornipinto.unfathomable_janken.core.Offset;
import org.fornipinto.unfathomable_janken.core.Size;
import org.fornipinto.unfathomable_janken.ui.core.Canvas;
import org.fornipinto.unfathomable_janken.ui.core.Component;
import org.fornipinto.unfathomable_janken.ui.core.Constraints;

/**
 * A component that arranges its child components in a depth stack.
 * <p>
 * Children added later are drawn on top of earlier children.
 */
public class Stack extends Component {
    final Component[] children;

    /**
     * Creates a new Stack component with the given children.
     *
     * @param children The child components to be stacked.
     */
    public Stack(Component... children) {
        this.children = children;
    }

    @Override
    public void layout(Constraints constraints) {
        final Constraints childrenConstraints = constraints.loosen();
        int maxWidth = 0;
        int maxHeight = 0;

        for (Component child : children) {
            child.layout(childrenConstraints);
            maxWidth = Math.max(maxWidth, child.size().width());
            maxHeight = Math.max(maxHeight, child.size().height());
        }

        final Size biggestSize = new Size(maxWidth, maxHeight);
        size = constraints.constrain(biggestSize);
    }

    @Override
    public void draw(Canvas canvas) {
        for (Component child : children) {
            canvas.draw(child, Offset.ZERO);
        }
    }
}
