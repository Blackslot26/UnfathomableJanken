package org.fornipinto.unfathomable_janken.ui.components;

import org.fornipinto.unfathomable_janken.ui.core.Component;
import org.fornipinto.unfathomable_janken.ui.core.Composent;

/**
 * A Spacer is a component that creates a flexible empty space in the layout.
 */
public class Spacer extends Composent {
    final private int flex;

    /**
     * Creates a new Spacer with a default flex factor of 1.
     */
    public Spacer() {
        this(1);
    }

    /**
     * Creates a new Spacer with the given flex factor.
     *
     * @param flex The flex factor determining how much space this spacer should take relative to its siblings.
     */
    public Spacer(int flex) {
        this.flex = flex;
    }

    @Override
    public Component build() {
        return new Flexible(flex, new SizedBox());
    }
}
