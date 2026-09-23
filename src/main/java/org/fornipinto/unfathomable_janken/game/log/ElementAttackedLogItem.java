package org.fornipinto.unfathomable_janken.game.log;

import org.fornipinto.unfathomable_janken.game.element.Element;
import org.fornipinto.unfathomable_janken.game.player.Player;

import java.util.Objects;

/**
 * A log registry for the element attack event.
 */
public final class ElementAttackedLogItem implements LogItem {
    private final Player attacker;
    private final Player defender;
    private final Element attackerElement;
    private final Element defenderElement;
    private final int damage;

    /**
     * Constructs a new {@link ElementAttackedLogItem} with the specified attacker, attacker element, and defender element.
     *
     * @param attacker        The player who initiated the attack.
     * @param defender        The player who was attacked.
     * @param attackerElement The element used by the attacker in the attack.
     * @param defenderElement The element that was attacked.
     * @param damage          The damage dealt by the attack.
     */
    public ElementAttackedLogItem(Player attacker, Player defender, Element attackerElement, Element defenderElement, int damage) {
        this.attacker = Objects.requireNonNull(attacker);
        this.defender = Objects.requireNonNull(defender);
        this.attackerElement = Objects.requireNonNull(attackerElement);
        this.defenderElement = Objects.requireNonNull(defenderElement);
        this.damage = damage;
    }

    @Override
    public <R> R accept(LogItemVisitor<R> visitor) {
        return visitor.visit(this);
    }

    /**
     * Returns the player who initiated the attack.
     *
     * @return The {@link Player} instance.
     */
    public Player getAttacker() {
        return attacker;
    }

    /**
     * Returns the player who was attacked.
     *
     * @return The {@link Player} instance.
     */
    public Player getDefender() {
        return defender;
    }

    /**
     * Returns the element used by the attacker in the attack.
     *
     * @return The {@link Element} instance.
     */
    public Element getAttackerElement() {
        return attackerElement;
    }

    /**
     * Returns the element that was attacked.
     *
     * @return The {@link Element} instance.
     */
    public Element getDefenderElement() {
        return defenderElement;
    }

    /**
     * Returns the damage dealt by the attack.
     *
     * @return The damage.
     */
    public int getDamage() {
        return damage;
    }
}
