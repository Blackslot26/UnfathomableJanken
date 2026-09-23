package org.fornipinto.unfathomable_janken.game.log;

import org.fornipinto.unfathomable_janken.game.element.Element;
import org.fornipinto.unfathomable_janken.game.player.Player;

import java.util.Objects; /**
 * A log registry for the element selection event.
 */
public final class ElementSelectedLogItem implements LogItem {
    private final Player player;
    private final Element element;

    /**
     * Constructs a new {@link ElementSelectedLogItem} with the specified player and element.
     *
     * @param player  The player who selected the element.
     * @param element The element that was selected.
     */
    public ElementSelectedLogItem(Player player, Element element) {
        this.player = Objects.requireNonNull(player);
        this.element = Objects.requireNonNull(element);
    }

    @Override
    public <R> R accept(LogItemVisitor<R> visitor) {
        return visitor.visit(this);
    }

    /**
     * Returns the player who selected the element.
     *
     * @return The {@link Player} instance.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Returns the element that was selected.
     *
     * @return The {@link Element} instance.
     */
    public Element getElement() {
        return element;
    }
}
