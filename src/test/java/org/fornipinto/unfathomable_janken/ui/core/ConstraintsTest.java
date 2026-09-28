package org.fornipinto.unfathomable_janken.ui.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConstraintsTest {

    @Test
    void testBoundedness() {
        final Constraints bounded = new Constraints(0, 100, 0, 50);
        assertTrue(bounded.hasBoundedWidth());
        assertTrue(bounded.hasBoundedHeight());

        final Constraints unbounded = new Constraints(0, null, 0, null);
        assertFalse(unbounded.hasBoundedWidth());
        assertFalse(unbounded.hasBoundedHeight());
        assertEquals(Constraints.UNBOUNDED, unbounded.maxWidth());
        assertEquals(Constraints.UNBOUNDED, unbounded.maxHeight());

        final Constraints halfBounded = new Constraints(0, 80, 0, null);
        assertTrue(halfBounded.hasBoundedWidth());
        assertFalse(halfBounded.hasBoundedHeight());
    }

    @Test
    void testTightness() {
        final Constraints tight = Constraints.tight(50, 20);
        assertTrue(tight.isTight());
        assertTrue(tight.hasTightWidth());
        assertTrue(tight.hasTightHeight());

        final Constraints loose = Constraints.loose(50, 20);
        assertFalse(loose.isTight());
        assertFalse(loose.hasTightWidth());
        assertFalse(loose.hasTightHeight());

        final Constraints unbounded = new Constraints(null, null, null, null);
        assertFalse(unbounded.isTight());
        assertFalse(unbounded.hasTightWidth());
        assertFalse(unbounded.hasTightHeight());
    }

    @Test
    void testDeflatePreservesUnbounded() {
        final Constraints unboundedHeight = new Constraints(0, 100, 0, null);
        final Constraints deflated = unboundedHeight.deflate(10, 10);

        assertTrue(deflated.hasBoundedWidth());
        assertEquals(90, deflated.maxWidth());

        assertFalse(deflated.hasBoundedHeight());
        assertEquals(Constraints.UNBOUNDED, deflated.maxHeight());
    }

    @Test
    void testInflatePreservesUnboundedWithoutOverflow() {
        final Constraints unbounded = new Constraints(0, null, 0, null);
        final Constraints inflated = unbounded.inflate(10, 10);

        assertFalse(inflated.hasBoundedWidth());
        assertFalse(inflated.hasBoundedHeight());
        assertEquals(Constraints.UNBOUNDED, inflated.maxWidth());
        assertEquals(Constraints.UNBOUNDED, inflated.maxHeight());
    }
}
