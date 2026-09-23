package org.fornipinto.unfathomable_janken.game.log;

import org.fornipinto.unfathomable_janken.game.element.Element;
import org.fornipinto.unfathomable_janken.game.player.Player;

import java.util.Objects;

/**
 * A log item in the game log.
 */
public sealed interface LogItem permits ElementAttackedLogItem, ElementSelectedLogItem, GameOverLogItem, GameStartedLogItem {
    <R> R accept(LogItemVisitor<R> visitor);
}

