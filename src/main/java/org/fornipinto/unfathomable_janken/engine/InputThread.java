package org.fornipinto.unfathomable_janken.engine;

import org.jline.keymap.BindingReader;
import org.jline.keymap.KeyMap;
import org.jline.terminal.KeyEvent;
import org.jline.terminal.KeyParser;
import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp.Capability;
import org.jline.utils.NonBlockingReader;

import java.io.IOError;
import java.util.Objects;
import java.util.Queue;

/**
 * Reads input from the NonBlockingReader and puts {@link KeyEvent} instances into a queue.
 */
final class InputThread extends Thread {
    private final BindingReader bindingReader;
    private final Queue<KeyEvent> keyQueue;
    private final KeyMap<String> keyMap;

    /**
     * Creates an InputThread.
     *
     * @param terminal The terminal to read input from.
     * @param keyQueue The queue to store parsed key events.
     */
    InputThread(Terminal terminal, Queue<KeyEvent> keyQueue) {
        Objects.requireNonNull(terminal);
        final NonBlockingReader reader = terminal.reader();
        this.bindingReader = new BindingReader(reader);
        this.keyQueue = Objects.requireNonNull(keyQueue);
        this.keyMap = createKeyMap(terminal);
    }

    static KeyMap<String> createKeyMap(Terminal terminal) {
        final KeyMap<String> map = new KeyMap<>();
        map.setAmbiguousTimeout(50);

        // Bind terminfo capabilities for functional keys (arrows, F-keys, etc.)
        for (Capability capability : Capability.values()) {
            if (capability.name().startsWith("key_")) {
                final String seq = KeyMap.key(terminal, capability);
                if (seq != null) {
                    map.bind(seq, seq);
                }
            }
        }

        // Common ANSI / VT escape sequences to ensure support across all terminals
        final String[] commonSequences = {
            "\033[A", "\033[B", "\033[C", "\033[D",                 // Standard arrows
            "\033OA", "\033OB", "\033OC", "\033OD",                 // SS3 arrows
            "\033[H", "\033[F",                                     // Home, End
            "\033[1~", "\033[2~", "\033[3~", "\033[4~", "\033[5~", "\033[6~", // Home, Insert, Delete, End, PgUp, PgDn
            "\033OP", "\033OQ", "\033OR", "\033OS",                 // F1-F4
            "\033[11~", "\033[12~", "\033[13~", "\033[14~", "\033[15~", // F1-F5
            "\033[17~", "\033[18~", "\033[19~", "\033[20~", "\033[21~", "\033[24~", // F6-F12
            "\033", "\r", "\n", "\t", "\b", "\177"
        };

        for (String sequence : commonSequences) {
            map.bindIfNotBound(sequence, sequence);
        }

        // Fallbacks for any single ASCII character or Unicode sequence
        map.setNomatch("NOMATCH");
        map.setUnicode("UNICODE");

        return map;
    }

    static KeyEvent parseKeyEvent(String raw) {
        if (raw == null) {
            return KeyParser.parse(null);
        }
        // Workaround for JLine 4.4.5: KeyParser.parseSS3Sequence does not map SS3 arrows
        // (\033OA - \033OD) or Home/End (\033OH, \033OF). We normalize them to ANSI CSI (\033[...).
        if (raw.startsWith("\033O") && raw.length() == 3) {
            final char c = raw.charAt(2);
            if ((c >= 'A' && c <= 'D') || c == 'H' || c == 'F') {
                raw = "\033[" + c;
            }
        }
        return KeyParser.parse(raw);
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                final String matched = bindingReader.readBinding(keyMap, null, true);

                if (matched != null) {
                    final String raw = bindingReader.getLastBinding();
                    final KeyEvent event = parseKeyEvent(raw);
                    keyQueue.add(event);
                    IO.println("Key pressed: " + event);
                }
            } catch (IOError | Exception e) {
                break;
            }
        }
    }
}