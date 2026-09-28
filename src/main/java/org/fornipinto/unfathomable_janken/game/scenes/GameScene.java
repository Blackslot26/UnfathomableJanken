package org.fornipinto.unfathomable_janken.game.scenes;

import org.fornipinto.unfathomable_janken.animation.Animation;
import org.fornipinto.unfathomable_janken.animation.curves.CubicCurve;
import org.fornipinto.unfathomable_janken.engine.Engine;
import org.fornipinto.unfathomable_janken.engine.Scene;
import org.fornipinto.unfathomable_janken.game.Game;
import org.fornipinto.unfathomable_janken.game.element.*;
import org.fornipinto.unfathomable_janken.game.log.*;
import org.fornipinto.unfathomable_janken.game.player.Player;
import org.fornipinto.unfathomable_janken.ui.components.*;
import org.fornipinto.unfathomable_janken.ui.core.*;
import org.jline.terminal.KeyEvent;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Objects;

/**
 * A scene that renders the game.
 */
public class GameScene implements Scene {
    private final Animation progressBarAnimation;
    private final Animation damageAnimation;

    private final Game game;

    private int selectedElementIndex = 0;

    /**
     * Creates a new GameScene.
     *
     * @param game The game object to render.
     */
    public GameScene(Game game) {
        this.game = Objects.requireNonNull(game);
        progressBarAnimation = new Animation(Duration.ofSeconds(1));
        damageAnimation = new Animation(Duration.ofMillis(150));
        damageAnimation.setProgress(1.0); // Start at 1.0 to keep player white until first attack
    }

    @Override
    public Component build() {
        final Component mainAreaComponent;

        if (game.getState() == Game.State.SELECTING_ELEMENT) {
            mainAreaComponent = new Box(
                Border.SINGLE,
                new Padding(
                    EdgeInsets.all(1),
                    new Column(
                        new Text("Select an element to switch to."),
                        new SizedBox(0, 1),
                        new Text("Press ENTER to confirm."),
                        new SizedBox(0, 1),
                        new ElementCard(getSelectedElement(), false, true, null)
                    ).crossAxisAlignment(CrossAxisAlignment.CENTER)
                        .mainAxisSize(MainAxisSize.MIN)
                )
            );
        } else if (game.getState() == Game.State.READY_TO_ATTACK) {
            final var playerProgressBarAnimation = game.isMainPlayerTurn() ? progressBarAnimation : null;
            final var enemyProgressBarAnimation = game.isEnemyPlayerTurn() ? progressBarAnimation : null;
            final var playerDamageAnimation = game.isMainPlayerTurn() ? damageAnimation : null;
            final var enemyDamageAnimation = game.isEnemyPlayerTurn() ? damageAnimation : null;

            mainAreaComponent = new BattleArea(
                playerProgressBarAnimation,
                enemyProgressBarAnimation,
                playerDamageAnimation,
                enemyDamageAnimation,
                game.getMainPlayer().getCurrentElement(),
                game.getEnemyPlayer().getCurrentElement()
            );
        } else {
            mainAreaComponent = new SizedBox();
        }

        final var enemyHighlightedElement = game.getEnemyPlayer().getCurrentElement();
        final var playerHighlightedElement = game.getState() == Game.State.SELECTING_ELEMENT
            ? getSelectedElement()
            : game.getMainPlayer().getCurrentElement();

        return new Box(
            new Row(
                new Flexible(
                    new Column(
                        new PlayerPanel(game.getEnemyPlayer(), enemyHighlightedElement, false),
                        new Flexible(
                            1,
                            new Align(
                                Alignment.CENTER,
                                mainAreaComponent
                            )
                        ),
                        new PlayerPanel(game.getMainPlayer(), playerHighlightedElement, true)
                    )
                ),
                new ConstrainedBox(
                    Constraints.tightWidth(60),
                    new Box(
                        Border.SINGLE,
                        new Column(
                            new Text("GAME LOG", Paint.BOLD),
                            new SizedBox(0, 1),
                            new Flexible(
                                1,
                                new LayoutBuilder(constraints -> {
                                    final var log = game.getLog();
                                    final var visibleItems = new ArrayList<Component>();
                                    final var itemConstraints = new Constraints(0, constraints.maxWidth(), 0, null);
                                    final var visitor = new LogItemDescriptionVisitor();
                                    var usedHeight = 0;
                                    var index = log.size() - 1;

                                    while (index >= 0) {
                                        final var logItem = log.get(index);
                                        final var text = new Text("• " + logItem.accept(visitor));
                                        text.layout(itemConstraints);

                                        if (usedHeight + text.size().height() > constraints.maxHeight()) {
                                            break;
                                        }

                                        usedHeight = usedHeight + text.size().height();
                                        visibleItems.addFirst(text);
                                        index = index - 1;
                                    }

                                    return new Column(visibleItems.toArray(Component[]::new))
                                        .mainAxisSize(MainAxisSize.MIN);
                                })
                            )
                        )
                    )
                )
            )
        );
    }

    @Override
    public void onUpdate(long deltaTime) {
        if (!progressBarAnimation.playing()) {
            progressBarAnimation.stop();
            if (game.getState() == Game.State.READY_TO_ATTACK) {
                damageAnimation.play();
            }
            game.processTurn();
            progressBarAnimation.play();
        }

        if (game.getState() == Game.State.GAME_OVER) {
            final var gameOverScene = new GameOverScene(game.wasGameWon());
            Engine.context().sceneManager().push(gameOverScene);
        }
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
                    damageAnimation.stop();
                    damageAnimation.setProgress(1.0);
                }
            }
        }
    }

    @Override
    public void dispose() {
        damageAnimation.dispose();
        progressBarAnimation.dispose();
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

final class PlayerPanel extends Composent {
    private final Player player;
    private final Element highlightedElement;
    private final boolean isRevealed;

    public PlayerPanel(Player player, Element highlightedElement, boolean isRevealed) {
        this.player = Objects.requireNonNull(player);
        this.highlightedElement = highlightedElement;
        this.isRevealed = isRevealed;
    }

    @Override
    public Component build() {
        return new Row(
            new Box(
                Border.SINGLE,
                new Column(
                    new Text(player.getName(), Paint.BOLD),
                    new Text("Energy: " + player.getEnergy() + " / " + player.getTotalEnergy())
                )
            ),
            new Spacer(),
            new Row(
                player
                    .getElements()
                    .stream()
                    .map(
                        element -> new ElementCard(
                            element,
                            highlightedElement == element,
                            highlightedElement == element || !element.isActive() || isRevealed,
                            null
                        )
                    )
                    .toArray(Component[]::new)
            )
        ).crossAxisAlignment(CrossAxisAlignment.CENTER);
    }
}

final class BattleArea extends Composent {
    private final Animation playerProgressBarAnimation;
    private final Animation enemyProgressBarAnimation;
    private final Animation playerDamageAnimation;
    private final Animation enemyDamageAnimation;
    private final Element playerElement;
    private final Element enemyElement;

    BattleArea(Animation playerProgressBarAnimation, Animation enemyProgressBarAnimation, Animation playerDamageAnimation, Animation enemyDamageAnimation, Element playerElement, Element enemyElement) {
        this.playerProgressBarAnimation = playerProgressBarAnimation;
        this.enemyProgressBarAnimation = enemyProgressBarAnimation;
        this.playerDamageAnimation = playerDamageAnimation;
        this.enemyDamageAnimation = enemyDamageAnimation;
        this.playerElement = playerElement;
        this.enemyElement = enemyElement;
    }


    @Override
    public Component build() {
        if (playerElement == null || enemyElement == null) {
            return new SizedBox(0, 0);
        }

        final var playerProgress = CubicCurve.EASE_IN.transform(playerProgressBarAnimation == null ? 0.0 : playerProgressBarAnimation.progress());
        final var enemyProgress = CubicCurve.EASE_IN.transform(enemyProgressBarAnimation == null ? 0.0 : enemyProgressBarAnimation.progress());

        return new Row(
            new Column(
                new ElementCard(playerElement, false, true, playerDamageAnimation),
                new ProgressBar(playerProgress)
            ).mainAxisSize(MainAxisSize.MIN)
                .crossAxisAlignment(CrossAxisAlignment.STRETCH),
            new Text("    ✕    "),
            new Column(
                new ElementCard(enemyElement, false, true, enemyDamageAnimation),
                new ProgressBar(enemyProgress)
            ).mainAxisSize(MainAxisSize.MIN)
                .crossAxisAlignment(CrossAxisAlignment.STRETCH)
        )
            .mainAxisSize(MainAxisSize.MIN)
            .crossAxisAlignment(CrossAxisAlignment.CENTER);
    }
}

final class ElementCard extends Composent {
    static private final ElementTypeVisitor<String> iconVisitor = new ElementTypeIconVisitor();
    static private final ElementTypeVisitor<String> typeNameVisitor = new ElementTypeNameVisitor();

    private final Element element;
    private final boolean isSelected;
    private final boolean isRevealed;
    private final Animation damageAnimation;

    ElementCard(Element element, boolean isSelected, boolean isRevealed, Animation damageAnimation) {
        this.element = Objects.requireNonNull(element);
        this.isSelected = isSelected;
        this.isRevealed = isRevealed;
        this.damageAnimation = damageAnimation;
    }

    @Override
    public Component build() {
        final var disabledPaint = new Paint().withForegroundColor(ColorPalette.COOL_GRAY);
        final var icon = getElementIcon();
        final var name = getElementTypeName();
        final var elementNamePaint = getElementPaint();
        final var energyPaint = element.isActive() ? new Paint() : disabledPaint;

        final Color baseColor;
        if (isSelected) {
            baseColor = ColorPalette.BANANA;
        } else if (element.isActive()) {
            baseColor = ColorPalette.WHITE;
        } else {
            baseColor = ColorPalette.COOL_GRAY;
        }

        final Color finalColor;
        if (damageAnimation != null) {
            finalColor = Color.lerp(ColorPalette.VERMILION, baseColor, damageAnimation.progress());
        } else {
            finalColor = baseColor;
        }

        final var boxPaint = new Paint().withForegroundColor(finalColor);

        return new ConstrainedBox(
            Constraints.tightWidth(15),
            new Box(
                Border.SINGLE,
                new Padding(
                    EdgeInsets.all(1),
                    new Align(
                        Alignment.CENTER,
                        new Column(
                            new Text(icon + " " + name, elementNamePaint),
                            new Text(element.getEnergy() + " / 100", energyPaint)
                        ).crossAxisAlignment(CrossAxisAlignment.CENTER)
                    )
                )
            ).withPaint(boxPaint)
        );
    }

    String getElementIcon() {
        if (!isRevealed) {
            return "❓";
        } else if (!element.isActive()) {
            return "💀";
        } else {
            return element.getType().accept(iconVisitor);
        }
    }

    String getElementTypeName() {
        if (!isRevealed) {
            return "Unknown";
        } else {
            return element.getType().accept(typeNameVisitor);
        }
    }

    Paint getElementPaint() {
        if (!isRevealed) {
            return new Paint();
        } else if (!element.isActive()) {
            return new Paint().withForegroundColor(ColorPalette.COOL_GRAY);
        } else {
            return element.getType().accept(new ElementTypePaintVisitor());
        }
    }
}