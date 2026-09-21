package org.fornipinto.unfathomable_janken.game.scenes;

import org.fornipinto.unfathomable_janken.engine.Key;
import org.fornipinto.unfathomable_janken.engine.Scene;
import org.fornipinto.unfathomable_janken.ui.components.Align;
import org.fornipinto.unfathomable_janken.ui.components.Text;
import org.fornipinto.unfathomable_janken.ui.core.Alignment;
import org.fornipinto.unfathomable_janken.ui.core.Component;

public class EmptyScene implements Scene {
    @Override
    public Component build() {
        return new Align(
            Alignment.CENTER,
            new Text("EMPTY SCENE")
        );
    }

    @Override
    public void onKeyPress(org.fornipinto.unfathomable_janken.engine.Key key) {
        if (key == Key.ESC) {
            org.fornipinto.unfathomable_janken.engine.Engine.context().sceneManager().pop();
        }
    }
}
