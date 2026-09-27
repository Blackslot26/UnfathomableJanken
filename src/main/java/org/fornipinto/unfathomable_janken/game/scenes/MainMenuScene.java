package org.fornipinto.unfathomable_janken.game.scenes;

import org.fornipinto.unfathomable_janken.game.ai.*;
import org.fornipinto.unfathomable_janken.game.player.AIPlayer;
import org.fornipinto.unfathomable_janken.ui.core.*;
import org.jline.terminal.KeyEvent;
import org.fornipinto.unfathomable_janken.engine.Engine;
import org.fornipinto.unfathomable_janken.engine.Scene;
import org.fornipinto.unfathomable_janken.game.Game;
import org.fornipinto.unfathomable_janken.game.player.HumanPlayer;
import org.fornipinto.unfathomable_janken.ui.components.*;

import java.util.List;

/**
 * A scene that renders the main menu.
 */
public class MainMenuScene implements Scene {

    private static final String TITLE = """
        ╻ ╻┏┓╻┏━╸┏━┓╺┳╸╻ ╻┏━┓┏┳┓┏━┓┏┓ ╻  ┏━╸
        ┃ ┃┃┗┫┣╸ ┣━┫ ┃ ┣━┫┃ ┃┃┃┃┣━┫┣┻┓┃  ┣╸\s
        ┗━┛╹ ╹╹  ╹ ╹ ╹ ╹ ╹┗━┛╹ ╹╹ ╹┗━┛┗━╸┗━╸
                  ┏┓┏━┓┏┓╻╻┏ ┏━╸┏┓╻        \s
                   ┃┣━┫┃┗┫┣┻┓┣╸ ┃┗┫        \s
                 ┗━┛╹ ╹╹ ╹╹ ╹┗━╸╹ ╹        \s
        """;

    private String playerName = "";
    private int selectedAIIndex = 0;

    private AI getSelectedAI() {
        return possibleAIs.get(selectedAIIndex);
    }

    private void nextAI() {
        selectedAIIndex = Math.min(selectedAIIndex + 1, possibleAIs.size() - 1);
    }

    private void previousAI() {
        selectedAIIndex = Math.max(selectedAIIndex - 1, 0);
    }

    static final private List<AI> possibleAIs = List.of(
        new RandomAI(),
        new StrategicAI(),
        new SuperAI()
    );

    @Override
    public Component build() {
        return new Align(
            Alignment.CENTER,
            new Column(
                new Text(TITLE),
                new SizedBox(0, 1),
                new ConstrainedBox(
                    Constraints.tightWidth(30),
                    new Box(
                        Border.SINGLE,
                        new Padding(
                            EdgeInsets.all(1),
                            new Column(
                                new Text("Enter your name: "),
                                new SizedBox(0, 1),
                                new TextInput(playerName, true),
                                new SizedBox(0, 1),
                                new Text("Use the arrow keys to change the difficulty: "),
                                new SizedBox(0, 1),
                                new Selector<>(
                                    possibleAIs,
                                    getSelectedAI(),
                                    ai -> ai.accept(new AINameVisitor())
                                ),
                                new SizedBox(0, 1),
                                new Text("Press ENTER to start.")
                            ).mainAxisSize(MainAxisSize.MIN)
                                .crossAxisAlignment(CrossAxisAlignment.CENTER)
                        )
                    )
                )
            ).mainAxisSize(MainAxisSize.MIN).crossAxisAlignment(CrossAxisAlignment.CENTER)
        );
    }

    @Override
    public void onKeyPress(KeyEvent event) {
        if (event.getSpecial() == KeyEvent.Special.Escape) {
            if (playerName.isEmpty()) {
                Engine.context().sceneManager().pop();
            } else {
                playerName = "";
            }
        } else if (event.getSpecial() == KeyEvent.Special.Backspace) {
            if (!playerName.isEmpty()) {
                playerName = playerName.substring(0, playerName.length() - 1);
            }
        } else if (event.getSpecial() == KeyEvent.Special.Enter) {
            if (playerName.isEmpty()) {
                return;
            }

            final var mainPlayer = new HumanPlayer(playerName);
            final var enemyPlayer = new AIPlayer(getSelectedAI());
            final var game = new Game(mainPlayer, enemyPlayer);
            final var gameScene = new GameScene(game);

            Engine.context().sceneManager().push(gameScene);
        } else if (event.getType() == KeyEvent.Type.Character && !Character.isISOControl(event.getCharacter())) {
            playerName += event.getCharacter();
        } else if (event.getArrow() == KeyEvent.Arrow.Left) {
            previousAI();
        } else if (event.getArrow() == KeyEvent.Arrow.Right) {
            nextAI();
        }
    }
}

final class AINameVisitor implements AIVisitor<String> {
    @Override
    public String visit(RandomAI ai) {
        return "Easy";
    }

    @Override
    public String visit(StrategicAI ai) {
        return "Medium";
    }

    @Override
    public String visit(SuperAI ai) {
        return "Hard";
    }
}