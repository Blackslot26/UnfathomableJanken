package org.fornipinto.unfathomable_janken.ui.components;

import org.fornipinto.unfathomable_janken.ui.core.Alignment;
import org.fornipinto.unfathomable_janken.ui.core.Canvas;
import org.fornipinto.unfathomable_janken.ui.core.Constraints;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AlignTest {

    @Test
    void testAlignShrinkWrapsChildWhenDimensionsAreUnbounded() {
        final Text child = new Text("Hello");
        final Align align = new Align(Alignment.CENTER, child);

        // Unbounded width, bounded height 10
        final Constraints constraints = new Constraints(0, null, 0, 10);
        align.layout(constraints);

        // Width must shrink-wrap to child's width (5), NOT Integer.MAX_VALUE!
        assertEquals(5, align.size().width());
        // Height expands to bounded height (10)
        assertEquals(10, align.size().height());

        // Verify Canvas can be created and drawn without OutOfMemoryError
        final Canvas canvas = new Canvas(align);
        assertDoesNotThrow(() -> align.draw(canvas));
    }

    @Test
    void testAlignExpandsWhenBothDimensionsAreBounded() {
        final Text child = new Text("Hi");
        final Align align = new Align(Alignment.CENTER, child);

        final Constraints constraints = Constraints.tight(20, 10);
        align.layout(constraints);

        assertEquals(20, align.size().width());
        assertEquals(10, align.size().height());

        final Canvas canvas = new Canvas(align);
        assertDoesNotThrow(() -> align.draw(canvas));
    }
}
