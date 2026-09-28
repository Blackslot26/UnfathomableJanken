package org.fornipinto.unfathomable_janken.ui.components;

import org.fornipinto.unfathomable_janken.ui.core.Border;
import org.fornipinto.unfathomable_janken.ui.core.Canvas;
import org.fornipinto.unfathomable_janken.ui.core.Constraints;
import org.fornipinto.unfathomable_janken.ui.core.MainAxisSize;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LinearLayoutTest {

    @Test
    void testNestedColumnWithDefaultMaxDoesNotOverflowParentColumn() {
        // Outer column with height 24:
        // Top: Box with inner Column (containing 2 texts and a row with elements)
        // Middle: Flexible(1, SizedBox(1, 1))
        // Bottom: Box with inner Column (same structure)
        final Column topCard = new Column(
            new Text("Player 1"),
            new Text("Energy: 10 / 10"),
            new Row(
                new Text("Elements: "),
                new Row(new Text("💧"), new Text("🔥"))
            )
        );
        final Box topBox = new Box(Border.SINGLE, topCard);

        final Column bottomCard = new Column(
            new Text("Player 2"),
            new Text("Energy: 8 / 10"),
            new Row(
                new Text("Elements: "),
                new Row(new Text("🌱"))
            )
        );
        final Box bottomBox = new Box(Border.SINGLE, bottomCard);

        final Flexible spacer = new Flexible(1, new SizedBox(1, 1));

        final Column outerColumn = new Column(topBox, spacer, bottomBox);
        final Constraints screenConstraints = Constraints.tight(80, 24);

        outerColumn.layout(screenConstraints);

        assertEquals(80, outerColumn.size().width());
        assertEquals(24, outerColumn.size().height());

        // Top card content is 3 lines high + 2 border lines = 5 total height
        assertEquals(5, topBox.size().height());
        // Bottom card content is 3 lines high + 2 border lines = 5 total height
        assertEquals(5, bottomBox.size().height());
        // Spacer takes 24 - 5 - 5 = 14 height
        assertEquals(14, spacer.size().height());

        // Verify drawing on canvas causes no OverflowException
        final Canvas canvas = new Canvas(outerColumn);
        assertDoesNotThrow(() -> outerColumn.draw(canvas));
    }

    @Test
    void testColumnWithUnboundedMainAxisFallsBackToContentSizeEvenWithMax() {
        final Column column = new Column(
            new Text("Line 1"),
            new Text("Line 2"),
            new Text("Line 3")
        );
        assertEquals(MainAxisSize.MAX, MainAxisSize.MAX);

        // Unbounded height: minHeight = 0, maxHeight = UNBOUNDED
        final Constraints unboundedHeight = new Constraints(0, 80, 0, null);
        column.layout(unboundedHeight);

        // Height must be wrap-content (3 lines), NOT Integer.MAX_VALUE
        assertEquals(3, column.size().height());
    }

    @Test
    void testFlexibleInUnboundedColumnThrowsException() {
        final Column column = new Column(
            new Text("Line 1"),
            new Flexible(1, new SizedBox(1, 1))
        );

        final Constraints unboundedHeight = new Constraints(0, 80, 0, null);
        assertThrows(IllegalStateException.class, () -> column.layout(unboundedHeight));
    }

    @Test
    void testRowWithDefaultMaxSizesToContentWhenUnbounded() {
        final Row row = new Row(
            new Text("A"),
            new Text("B"),
            new Text("C")
        );

        // Unbounded width
        final Constraints unboundedWidth = new Constraints(0, null, 0, 10);
        row.layout(unboundedWidth);

        // Content width is 1 + 1 + 1 = 3
        assertEquals(3, row.size().width());
        assertEquals(1, row.size().height());
    }



    @Test
    void testVerticalDividerStretchesToRowHeightInUnboundedRow() {
        final Row row = new Row(
            new Text("Line 1\nLine 2\nLine 3"),
            new VerticalDivider(),
            new Text("Single line")
        );

        final Constraints unboundedConstraints = new Constraints(0, 80, 0, null);
        row.layout(unboundedConstraints);

        assertEquals(3, row.size().height());
        final Canvas canvas = new Canvas(row);
        assertDoesNotThrow(() -> row.draw(canvas));
    }

    @Test
    void testSpacerInRowExpandsToFillRemainingWidth() {
        final Text left = new Text("Left"); // width 4
        final Spacer spacer = new Spacer();
        final Text right = new Text("Right"); // width 5

        final Row row = new Row(left, spacer, right);
        final Constraints constraints = new Constraints(0, 50, 0, 10);
        row.layout(constraints);

        assertEquals(50, row.size().width());
        assertEquals(4, left.size().width());
        assertEquals(5, right.size().width());
        assertEquals(50 - 4 - 5, spacer.size().width());

        final Canvas canvas = new Canvas(row);
        assertDoesNotThrow(() -> row.draw(canvas));
    }

    @Test
    void testProgressBarInColumnInsideRowMatchesSiblingWidthWithStretch() {
        final var card = new Box(Border.SINGLE, new Text("💧 Water")); // width = 8 + 2 = 10, height = 1 + 2 = 3
        final var progressBar = new ProgressBar(0.5);
        final var column = new Column(card, progressBar).crossAxisAlignment(
            org.fornipinto.unfathomable_janken.ui.core.CrossAxisAlignment.STRETCH
        );
        final var row = new Row(column);

        row.layout(new Constraints(0, 60, 0, null));

        assertEquals(10, card.size().width());
        assertEquals(3, card.size().height());
        assertEquals(10, progressBar.size().width());
        assertEquals(1, progressBar.size().height());
        assertEquals(10, column.size().width());
        assertEquals(4, column.size().height());

        final var canvas = new Canvas(row);
        assertDoesNotThrow(() -> row.draw(canvas));
    }
}
