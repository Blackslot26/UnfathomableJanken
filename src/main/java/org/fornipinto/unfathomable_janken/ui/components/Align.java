package org.fornipinto.unfathomable_janken.ui.components;

import org.fornipinto.unfathomable_janken.core.Offset;
import org.fornipinto.unfathomable_janken.core.Size;
import org.fornipinto.unfathomable_janken.ui.core.Alignment;
import org.fornipinto.unfathomable_janken.ui.core.Canvas;
import org.fornipinto.unfathomable_janken.ui.core.Component;
import org.fornipinto.unfathomable_janken.ui.core.Constraints;

import java.util.Objects;

/**
 * A component that aligns its child within itself according to the specified alignment.
 */
public final class Align extends Component {
    private final Alignment alignment;
    private final Component child;

    /**
     * Constructs an Align component with the specified alignment and child.
     *
     * @param alignment The alignment to use for positioning the child.
     * @param child     The child component to be aligned.
     */
    public Align(Alignment alignment, Component child) {
        this.alignment = alignment;
        this.child = Objects.requireNonNull(child);
    }

    @Override
    public void layout(Constraints constraints) {
        final Constraints childConstraints = constraints.loosen();
        child.layout(childConstraints);

        final int width = constraints.hasBoundedWidth()
            ? constraints.maxWidth()
            : child.size().width();
        final int height = constraints.hasBoundedHeight()
            ? constraints.maxHeight()
            : child.size().height();

        size = constraints.constrain(new Size(width, height));
    }

    @Override
    public void draw(Canvas canvas) {
        final Offset selfCenter = alignment.alongSize(size());
        final Offset childCenter = alignment.alongSize(child.size());
        canvas.draw(child, selfCenter.subtract(childCenter));
    }
}
