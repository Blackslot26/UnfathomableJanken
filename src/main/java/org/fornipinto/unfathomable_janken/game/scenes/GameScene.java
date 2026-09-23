package org.fornipinto.unfathomable_janken.game.scenes;

import org.fornipinto.unfathomable_janken.engine.Scene;
import org.fornipinto.unfathomable_janken.game.Game;
import org.fornipinto.unfathomable_janken.game.element.*;
import org.fornipinto.unfathomable_janken.game.log.*;
import org.fornipinto.unfathomable_janken.game.player.Player;
import org.fornipinto.unfathomable_janken.ui.components.*;
import org.fornipinto.unfathomable_janken.ui.core.*;
import org.jline.terminal.KeyEvent;

import java.util.Objects;

/**
 * A scene that renders the game.
 */
public class GameScene implements Scene {
    private final Game game;

    private int selectedElementIndex = 0;

    /**
     * Creates a new GameScene.
     *
     * @param game The game object to render.
     */
    public GameScene(Game game) {
        this.game = Objects.requireNonNull(game);
    }

    @Override
    public Component build() {
        final Component selectElementBox;

        if (game.getState() == Game.State.SELECTING_ELEMENT) {
            selectElementBox = new Box(
                Border.SINGLE,
                new Column(
                    new Text("Select an element to switch to."),
                    new SizedBox(0, 1),
                    new Text("Press ENTER to confirm."),
                    new SizedBox(0, 1),
                    new ElementCard(getSelectedElement(), false)
                ).crossAxisAlignment(CrossAxisAlignment.CENTER)
                    .mainAxisSize(MainAxisSize.MIN)
            );
        } else {
            selectElementBox = new SizedBox();
        }

        final var enemyHighlightedElement = game.getEnemyPlayer().getCurrentElement();
        final var playerHighlightedElement = game.getState() == Game.State.SELECTING_ELEMENT
            ? getSelectedElement()
            : game.getMainPlayer().getCurrentElement();

        return new Box(
            new Row(
                new Flexible(
                    new Column(
                        new PlayerCard(game.getEnemyPlayer(), enemyHighlightedElement),
                        new Flexible(
                            1,
                            new Align(
                                Alignment.CENTER,
                                selectElementBox
                            )
                        ),
                        new PlayerCard(game.getMainPlayer(), playerHighlightedElement)
                    )
                ),
                new ConstrainedBox(
                    Constraints.tightWidth(60),
                    new Box(
                        Border.SINGLE,
                        new Column(
                            new Text("GAME LOG", Paint.BOLD),
                            new SizedBox(0, 1),
                            new Flexible(1,
                                new Column(
                                    game.getLog()
                                        .stream()
                                        .map(logItem -> logItem.accept(new LogItemDescriptionVisitor()))
                                        .map(Text::new)
                                        .toArray(Component[]::new)
                                ).mainAxisSize(MainAxisSize.MIN)
                            )
                        )
                    )
                )
            )
        );
    }

    @Override
    public void onUpdate(long deltaTime) {
//        if (state == GameSceneState.RUNNING) {
//            game.processTurn();
//        }
    }

    @Override
    public void onKeyPress(KeyEvent event) {
        if (game.getState() == Game.State.SELECTING_ELEMENT) {
            if (event.getArrow() == KeyEvent.Arrow.Right) {
                selectedElementIndex = (selectedElementIndex + 1) % game.getMainPlayer().getElements().size();
            } else if (event.getArrow() == KeyEvent.Arrow.Left) {
                selectedElementIndex = (selectedElementIndex - 1 + game.getMainPlayer().getElements().size()) % game.getMainPlayer().getElements().size();
            } else if (event.getSpecial() == KeyEvent.Special.Enter) {
                final var selectedElement = getSelectedElement();
                if (selectedElement != null && selectedElement.isActive()) {
                    game.selectElement(game.getMainPlayer(), selectedElement);
                }
            }
        } else if (event.getSpecial() == KeyEvent.Special.Enter) {
            game.processTurn();
        }
    }

    private Element getSelectedElement() {
        return game.getMainPlayer().getElements().get(selectedElementIndex);
    }
}

class ElementTypeIconVisitor implements ElementTypeVisitor<String> {

    @Override
    public String visit(WaterElement water) {
        return "💧";
    }

    @Override
    public String visit(FireElement fire) {
        return "🔥";
    }

    @Override
    public String visit(EarthElement earth) {
        return "🌱";
    }
}

class ElementTypeNameVisitor implements ElementTypeVisitor<String> {
    @Override
    public String visit(WaterElement water) {
        return "Water";
    }

    @Override
    public String visit(FireElement fire) {
        return "Fire";
    }

    @Override
    public String visit(EarthElement earth) {
        return "Earth";
    }
}

class ElementTypePaintVisitor implements ElementTypeVisitor<Paint> {
    @Override
    public Paint visit(WaterElement water) {
        return new Paint().withForegroundColor(ColorPalette.AZURE);
    }

    @Override
    public Paint visit(FireElement fire) {
        return new Paint().withForegroundColor(ColorPalette.VERMILION);
    }

    @Override
    public Paint visit(EarthElement earth) {
        return new Paint().withForegroundColor(ColorPalette.MINT_GREEN);
    }
}

class LogItemDescriptionVisitor implements LogItemVisitor<String> {

    @Override
    public String visit(GameStartedLogItem item) {
        return "Game started.";
    }

    @Override
    public String visit(ElementSelectedLogItem item) {
        return item.getPlayer().getName() + " selected element " + item.getElement().getType().accept(new ElementTypeNameVisitor()) + ".";
    }

    @Override
    public String visit(ElementAttackedLogItem item) {
        final var attackerElement = item.getAttackerElement().getType().accept(new ElementTypeNameVisitor());
        final var defenderElement = item.getDefenderElement().getType().accept(new ElementTypeNameVisitor());
        return item.getAttacker().getName() + "'s " + attackerElement + " attacked " + item.getDefender().getName() + "'s " + defenderElement + " for " + item.getDamage() + " damage.";
    }

    @Override
    public String visit(GameOverLogItem item) {
        return "Game over. " + item.getWinner().getName() + " won the game.";
    }
}

final class PlayerCard extends Composent {
    private final Player player;
    private final Element highlightedElement;

    public PlayerCard(Player player, Element highlightedElement) {
        this.player = Objects.requireNonNull(player);
        this.highlightedElement = highlightedElement;
    }

    @Override
    public Component build() {
        return new Box(
            Border.SINGLE,
            new Row(
                new Column(
                    new Text(player.getName(), Paint.BOLD),
                    new Text("Energy: " + player.getEnergy() + " / " + player.getTotalEnergy())
                ),
                new VerticalDivider(),
                new Row(
                    player
                        .getElements()
                        .stream()
                        .map(
                            element -> new ElementCard(
                                element,
                                highlightedElement == element
                            )
                        )
                        .toArray(Component[]::new)
                )
            )
        );

    }
}

final class ElementCard extends Composent {
    private final Element element;
    private final boolean isSelected;

    ElementCard(Element element, boolean isSelected) {
        this.element = Objects.requireNonNull(element);
        this.isSelected = isSelected;
    }

    @Override
    public Component build() {
        final var disabledPaint = new Paint().withForegroundColor(ColorPalette.COOL_GRAY);
        final var type = element.getType();
        final var icon = element.isActive() ? type.accept(new ElementTypeIconVisitor()) : "💀";
        final var name = type.accept(new ElementTypeNameVisitor());
        final var elementNamePaint = element.isActive() ? type.accept(new ElementTypePaintVisitor()) : disabledPaint;
        final var energyPaint = element.isActive() ? new Paint() : disabledPaint;

        final Paint boxPaint;
        if (isSelected) {
            boxPaint = new Paint().withBold(true).withForegroundColor(ColorPalette.BANANA);
        } else if (element.isActive()) {
            boxPaint = new Paint();
        } else {
            boxPaint = disabledPaint;
        }

        return new Box(
            Border.SINGLE,
            new Column(
                new Text(icon + " " + name, elementNamePaint),
                new Text(element.getEnergy() + " / 100", energyPaint)
            )
        ).withPaint(boxPaint);
    }
}