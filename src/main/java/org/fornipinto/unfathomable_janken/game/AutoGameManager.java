package org.fornipinto.unfathomable_janken.game;

import org.fornipinto.unfathomable_janken.game.element.Element;
import org.fornipinto.unfathomable_janken.game.player.Player;

public class AutoGameManager implements GameManager {
    @Override
    public Element requireElementSelection(Player player) {
        return player.getActiveElements().getFirst();
    }
}
