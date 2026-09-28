package org.fornipinto.unfathomable_janken.engine;

import org.jline.keymap.BindingReader;
import org.jline.keymap.KeyMap;
import org.jline.terminal.KeyEvent;
import org.jline.terminal.KeyParser;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.NonBlockingReader;
import org.jline.utils.NonBlockingReaderImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

class KeyMapTest {

    private Terminal terminal;

    @BeforeEach
    void setUp() throws IOException {
        terminal = TerminalBuilder.builder()
            .streams(new ByteArrayInputStream(new byte[0]), new ByteArrayOutputStream())
            .dumb(true)
            .build();
    }

    @AfterEach
    void tearDown() throws IOException {
        if (terminal != null) {
            terminal.close();
        }
    }

    @Test
    void testCreateKeyMapBindsKeys() {
        final KeyMap<String> keyMap = InputThread.createKeyMap(terminal);
        assertNotNull(keyMap);

        // Verify direct sequences are bound
        assertEquals("\033", keyMap.getBound("\033"));
        assertEquals("\n", keyMap.getBound("\n"));
        assertEquals("\r", keyMap.getBound("\r"));
        assertEquals("\177", keyMap.getBound("\177"));
        assertEquals("\033[A", keyMap.getBound("\033[A"));
        assertEquals("\033[B", keyMap.getBound("\033[B"));
        assertNotNull(keyMap.getNomatch());
        assertNotNull(keyMap.getUnicode());
    }

    @Test
    void testBindingReaderDecodesAnyKey() {
        final KeyMap<String> keyMap = InputThread.createKeyMap(terminal);
        final String input = "i\033c\r\177\033[Aç";
        final NonBlockingReader reader =
            new NonBlockingReaderImpl("test-reader", new StringReader(input));
        final BindingReader bindingReader = new BindingReader(reader);

        // 'i'
        assertNotNull(bindingReader.readBinding(keyMap, null, true));
        KeyEvent event = InputThread.parseKeyEvent(bindingReader.getLastBinding());
        assertEquals(KeyEvent.Type.Character, event.getType());
        assertEquals('i', event.getCharacter());

        // Escape '\033'
        assertNotNull(bindingReader.readBinding(keyMap, null, true));
        event = InputThread.parseKeyEvent(bindingReader.getLastBinding());
        assertEquals(KeyEvent.Special.Escape, event.getSpecial());

        // 'c'
        assertNotNull(bindingReader.readBinding(keyMap, null, true));
        event = InputThread.parseKeyEvent(bindingReader.getLastBinding());
        assertEquals('c', event.getCharacter());

        // Enter '\r'
        assertNotNull(bindingReader.readBinding(keyMap, null, true));
        event = InputThread.parseKeyEvent(bindingReader.getLastBinding());
        assertEquals(KeyEvent.Special.Enter, event.getSpecial());

        // Backspace '\177'
        assertNotNull(bindingReader.readBinding(keyMap, null, true));
        event = InputThread.parseKeyEvent(bindingReader.getLastBinding());
        assertEquals(KeyEvent.Special.Backspace, event.getSpecial());

        // Up arrow '\033[A'
        assertNotNull(bindingReader.readBinding(keyMap, null, true));
        event = InputThread.parseKeyEvent(bindingReader.getLastBinding());
        assertEquals(KeyEvent.Arrow.Up, event.getArrow());

        // Unicode 'ç'
        assertNotNull(bindingReader.readBinding(keyMap, null, true));
        event = InputThread.parseKeyEvent(bindingReader.getLastBinding());
        assertEquals(KeyEvent.Type.Character, event.getType());
        assertEquals('ç', event.getCharacter());
    }

    @Test
    void testSS3ArrowKeysDecodedCorrectly() {
        final KeyMap<String> keyMap = InputThread.createKeyMap(terminal);
        // Test SS3 arrow keys: Up (\033OA), Down (\033OB), Right (\033OC), Left (\033OD)
        final String input = "\033OA\033OB\033OC\033OD";
        final NonBlockingReader reader =
            new NonBlockingReaderImpl("test-reader", new StringReader(input));
        final BindingReader bindingReader = new BindingReader(reader);

        // Up arrow '\033OA'
        assertNotNull(bindingReader.readBinding(keyMap, null, true));
        KeyEvent event = InputThread.parseKeyEvent(bindingReader.getLastBinding());
        assertEquals(KeyEvent.Arrow.Up, event.getArrow());

        // Down arrow '\033OB'
        assertNotNull(bindingReader.readBinding(keyMap, null, true));
        event = InputThread.parseKeyEvent(bindingReader.getLastBinding());
        assertEquals(KeyEvent.Arrow.Down, event.getArrow());

        // Right arrow '\033OC'
        assertNotNull(bindingReader.readBinding(keyMap, null, true));
        event = InputThread.parseKeyEvent(bindingReader.getLastBinding());
        assertEquals(KeyEvent.Arrow.Right, event.getArrow());

        // Left arrow '\033OD'
        assertNotNull(bindingReader.readBinding(keyMap, null, true));
        event = InputThread.parseKeyEvent(bindingReader.getLastBinding());
        assertEquals(KeyEvent.Arrow.Left, event.getArrow());
    }
}
