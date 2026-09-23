package org.fornipinto.unfathomable_janken.game.scenes;

import org.fornipinto.unfathomable_janken.engine.Scene;
import org.fornipinto.unfathomable_janken.game.Game;
import org.fornipinto.unfathomable_janken.game.GameManager;
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
public class GameScene implements Scene, GameManager {
    private final Game game;

    private GameSceneState state;
    private Integer selectedElementIndex = 0;

    /**
     * Creates a new GameScene.
     *
     * @param game The game object to render.
     */
    public GameScene(Game game) {
        this.game = Objects.requireNonNull(game);
        game.setManager(this);
        this.state = GameSceneState.SWITCH_ELEMENT;
    }

    @Override
    public Component build() {
        final Component selectElementBox;

        if (state == GameSceneState.SWITCH_ELEMENT) {
            selectElementBox = new Box(
                Border.SINGLE,
                new Text("Select an element to switch to:")
            );
        } else {
            selectElementBox = new SizedBox();
        }

        return new Box(
            new Row(
                new Flexible(
                    new Column(
                        playerCard(game.getEnemyPlayer()),
                        new Flexible(
                            1,
                            new Align(
                                Alignment.CENTER,
                                selectElementBox
                            )
                        ),
                        playerCard(game.getMainPlayer())
                    )
                ),
                new Box(
                    Border.SINGLE,
                    new Column(
                        new Text("Game Log:"),
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
        if (state == GameSceneState.SWITCH_ELEMENT) {
            if (event.getArrow() == KeyEvent.Arrow.Right) {
                selectedElementIndex = (selectedElementIndex + 1) % game.getMainPlayer().getElements().size();
            } else if (event.getArrow() == KeyEvent.Arrow.Left) {
                selectedElementIndex = (selectedElementIndex - 1 + game.getMainPlayer().getElements().size()) % game.getMainPlayer().getActiveElements().size();
            } else if (event.getSpecial() == KeyEvent.Special.Enter) {
                final var selectedElement = getSelectedElementIndex();
                if (selectedElement != null && selectedElement.isActive()) {
                    game.getMainPlayer().setCurrentElement(selectedElement);
                    selectedElementIndex = null;
                    state = GameSceneState.RUNNING;
                }
            }
        } else if (event.getSpecial() == KeyEvent.Special.Enter) {
            game.processTurn();
        }
    }

    private Component playerCard(Player player) {
        return new Box(
            Border.SINGLE,
            new Row(
                new Column(
                    new Text(player.getName(), Paint.BOLD),
                    new Text("Energy: " + player.getEnergy() + " / " + player.getTotalEnergy())
                ),
                new VerticalDivider(),
                new Row(
                    player.getElements().stream().map(this::elementCard).toArray(Component[]::new)
                )
            )
        );
    }

    private Component elementCard(Element element) {
        final var type = element.getType();
        final var icon = type.accept(new ElementTypeIconVisitor());
        final var name = type.accept(new ElementTypeNameVisitor());
        final var paint = type.accept(new ElementTypePaintVisitor());
        final Paint boxPaint;

        if (selectedElementIndex != null && selectedElementIndex.equals(game.getMainPlayer().getElements().indexOf(element))) {
            boxPaint = new Paint().withBold(true).withForegroundColor(ColorPalette.BANANA);
        } else {
            boxPaint = new Paint();
        }

        return new Box(
            Border.SINGLE,
            new Column(
                new Text(icon + " " + name, paint),
                new Text(element.getEnergy() + " / 100")
            )
        ).withPaint(boxPaint);
    }

    private Element getSelectedElementIndex() {
        if (selectedElementIndex == null) {
            return null;
        } else {
            return game.getMainPlayer().getElements().get(selectedElementIndex);
        }
    }

    @Override
    public void requireElementSelection(Player player) {
        state = GameSceneState.SWITCH_ELEMENT;
    }

    private enum GameSceneState {
        RUNNING,
        SWITCH_ELEMENT,
        GAME_OVER,
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