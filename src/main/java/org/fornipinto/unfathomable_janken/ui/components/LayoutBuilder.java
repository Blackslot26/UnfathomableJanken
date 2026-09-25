package org.fornipinto.unfathomable_janken.ui.components;


import org.fornipinto.unfathomable_janken.ui.core.Canvas;
import org.fornipinto.unfathomable_janken.ui.core.Component;
import org.fornipinto.unfathomable_janken.ui.core.Constraints;

import java.util.function.Function;

/**
 * A component that builds its child component dynamically based on the given constraints.
 */
public final class LayoutBuilder extends Component {
    final private Function<Constraints, Component> builder;
    private Component child;

    /**
     * Creates a new LayoutBuilder with the given builder function.
     *
     * @param builder A function that takes constraints and returns a component to be laid out and drawn.
     */
    public LayoutBuilder(Function<Constraints, Component> builder) {
        this.builder = builder;
    }

    @Override
    public void layout(Constraints constraints) {
        child = builder.apply(constraints);
        child.layout(constraints);
        size = child.size();
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.draw(child, 0, 0);
    }
}
