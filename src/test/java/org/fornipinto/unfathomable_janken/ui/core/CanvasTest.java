package org.fornipinto.unfathomable_janken.ui.core;

import org.fornipinto.unfathomable_janken.ui.components.Text;
import org.junit.jupiter.api.Test;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CanvasTest {

    @Test
    void testCanvasRenderPlainText() {
        final Text text = new Text("Hello");
        text.layout(Constraints.tight(5, 1));

        final Canvas canvas = new Canvas(text);
        text.draw(canvas);

        final List<AttributedString> lines = canvas.toAttributedStrings();
        assertEquals(1, lines.size());
        assertEquals("Hello", lines.getFirst().toString());
    }

    @Test
    void testCanvasRenderStyledText() {
        final Paint paint = new Paint()
            .withBold(true)
            .withForegroundColor(ColorPalette.VERMILION)
            .withBackgroundColor(ColorPalette.ROYAL_BLUE);

        final Text text = new Text("Hi", paint);
        text.layout(Constraints.tight(2, 1));

        final Canvas canvas = new Canvas(text);
        text.draw(canvas);

        final List<AttributedString> lines = canvas.toAttributedStrings();
        assertEquals(1, lines.size());

        final AttributedString line = lines.getFirst();
        assertEquals("Hi", line.toString());

        final AttributedStyle style = line.styleAt(0);
        assertTrue((style.getStyle() & AttributedStyle.BOLD.getStyle()) != 0, "Style should have BOLD attribute");
    }

    @Test
    void testCanvasMerge() {
        final Text text1 = new Text("World");
        text1.layout(Constraints.tight(10, 2));
        final Canvas baseCanvas = new Canvas(text1);

        final Text text2 = new Text("Hi");
        text2.layout(Constraints.tight(2, 1));
        final Canvas overlayCanvas = new Canvas(text2);
        text2.draw(overlayCanvas);

        baseCanvas.merge(overlayCanvas, 1, 0);

        final List<AttributedString> lines = baseCanvas.toAttributedStrings();
        assertEquals(2, lines.size());
        assertEquals(" Hi       ", lines.getFirst().toString());
    }

    @Test
    void testCanvasRejectsUnboundedDimensions() {
        final Component unboundedComponent = new Component() {
            @Override
            public void layout(Constraints constraints) {
                size = new org.fornipinto.unfathomable_janken.core.Size(10, Constraints.UNBOUNDED);
            }

            @Override
            public void draw(Canvas canvas) {}
        };
        unboundedComponent.layout(new Constraints(0, null, 0, null));

        final IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            () -> new Canvas(unboundedComponent)
        );
        assertTrue(exception.getMessage().contains("unbounded dimensions"));
    }

    @Test
    void testWideCharacterAndEmojiAlignment() {
        // BMP emoji '❓' (U+2753, 1 UTF-16 char, 2 terminal columns)
        final Text unknownText = new Text("❓ Unknown");
        unknownText.layout(new Constraints(0, null, 0, null));
        assertEquals(10, unknownText.size().width());

        final org.fornipinto.unfathomable_janken.ui.components.Box unknownBox =
            new org.fornipinto.unfathomable_janken.ui.components.Box(Border.SINGLE, unknownText);
        unknownBox.layout(new Constraints(0, null, 0, null));
        assertEquals(12, unknownBox.size().width());

        final Canvas unknownCanvas = new Canvas(unknownBox);
        unknownBox.draw(unknownCanvas);
        for (AttributedString line : unknownCanvas.toAttributedStrings()) {
            assertEquals(12, line.columnLength(), "Every line in Box with ❓ must have columnLength 12");
        }

        // Supplementary plane emoji '💧' (U+1F4A7, 2 UTF-16 chars, 2 terminal columns)
        final Text waterText = new Text("💧 Water");
        waterText.layout(new Constraints(0, null, 0, null));
        assertEquals(8, waterText.size().width());

        final org.fornipinto.unfathomable_janken.ui.components.Box waterBox =
            new org.fornipinto.unfathomable_janken.ui.components.Box(Border.SINGLE, waterText);
        waterBox.layout(new Constraints(0, null, 0, null));
        assertEquals(10, waterBox.size().width());

        final Canvas waterCanvas = new Canvas(waterBox);
        waterBox.draw(waterCanvas);
        for (AttributedString line : waterCanvas.toAttributedStrings()) {
            assertEquals(10, line.columnLength(), "Every line in Box with 💧 must have columnLength 10");
        }

        for (String icon : List.of("💧", "🔥", "🪨", "🪵", "🔩", "❓", "💀")) {
            assertEquals(1, icon.codePoints().count(), "Icon " + icon + " must be a single code point for Canvas tile storage");
            assertEquals(2, Unicode.charWidth(icon.codePointAt(0)), "Icon " + icon + " must have WCWidth of 2");

            final Text iconText = new Text(icon + " Test");
            final org.fornipinto.unfathomable_janken.ui.components.Box iconBox =
                new org.fornipinto.unfathomable_janken.ui.components.Box(Border.SINGLE, iconText);
            iconBox.layout(new Constraints(0, null, 0, null));

            final Canvas iconCanvas = new Canvas(iconBox);
            iconBox.draw(iconCanvas);
            for (AttributedString line : iconCanvas.toAttributedStrings()) {
                assertEquals(9, line.columnLength(), "Every line in Box with " + icon + " must have columnLength 9");
            }
        }
    }
}
