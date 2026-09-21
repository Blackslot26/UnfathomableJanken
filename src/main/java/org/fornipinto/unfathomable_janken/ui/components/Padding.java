package org.fornipinto.unfathomable_janken.ui.components;

import org.fornipinto.unfathomable_janken.ui.core.Canvas;
import org.fornipinto.unfathomable_janken.ui.core.Component;
import org.fornipinto.unfathomable_janken.ui.core.Constraints;
import org.fornipinto.unfathomable_janken.ui.core.EdgeInsets;

/**
 * A component that adds padding around its child component.
 */
public class Padding extends Component {
    private final EdgeInsets edgeInsets;
    private final Component child;

    /**
     * Constructs a Padding component with the given edge insets and child component.
     *
     * @param edgeInsets the edge insets defining the padding
     * @param child      the child component to be padded
     */
    public Padding(EdgeInsets edgeInsets, Component child) {
        this.edgeInsets = edgeInsets;
        this.child = child;
    }

    @Override
    public void layout(Constraints constraints) {
        final Constraints paddedConstraints = constraints.deflate(edgeInsets);
        child.layout(paddedConstraints);

        final Constraints childConstraints = Constraints.tight(child.size());
        final Constraints childConstraintsWithPadding = childConstraints.inflate(edgeInsets).tighten();

        size = constraints.enforce(childConstraintsWithPadding).biggest();
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.draw(child, edgeInsets.left(), edgeInsets.top());
    }
}
