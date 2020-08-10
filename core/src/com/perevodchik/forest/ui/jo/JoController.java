package com.perevodchik.forest.ui.jo;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.perevodchik.forest.entity.Player;

public class JoController extends InputListener {
    private final Jo jo;

    public JoController(Jo jo) {
        this.jo = jo;
    }

    @Override
    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
        jo.joTouch();
        jo.changeCursor(x, y);
        return true;
    }

    @Override
    public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
        jo.joUnTouch();
//        Player.getPlayer().setVelocity(0, 0);
    }

    @Override
    public void touchDragged (InputEvent event, float x, float y, int pointer) {
        jo.changeCursor(x, y);
        if(jo.isTouch()) {
            jo.handle(x, y);
        }
    }

}
