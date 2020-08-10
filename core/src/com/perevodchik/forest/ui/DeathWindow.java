package com.perevodchik.forest.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.perevodchik.forest.ForestGameScreen;
import com.perevodchik.forest.utils.FontUtil;

import java.util.ArrayList;

public class DeathWindow extends Group {
    private Button[] buttons = new Button[2];
    private Label title = null;
    private ArrayList<Label> labels = new ArrayList<>();
    private float x, y;
    private float width, height;
    private Padding padding;
    private Group body;
    private Texture background = new Texture("floor.png");


    public DeathWindow(Padding padding, float x, float y, int width, int height) {
        this.padding = padding;
        body = new Group();
        setWidth(width);
        setHeight(height);
        setX(x);
        setY(y);

        body.setX(padding.getPadding(com.perevodchik.forest.enums.Padding.LEFT));
        body.setY(padding.getPadding(com.perevodchik.forest.enums.Padding.BOTTOM));
        body.setWidth(width - padding.getPadding(com.perevodchik.forest.enums.Padding.RIGHT) - padding.getPadding(com.perevodchik.forest.enums.Padding.LEFT));
        body.setHeight(height - padding.getPadding(com.perevodchik.forest.enums.Padding.TOP) - padding.getPadding(com.perevodchik.forest.enums.Padding.BOTTOM));
        this.x = body.getX();
        this.y = body.getY();
        this.width = body.getWidth();
        this.height = body.getHeight();

        addActor(body);
        debugAll();
    }

    public DeathWindow(Padding padding, int width, int height) {
        this.padding = padding;
        body = new Group();
        setWidth(width);
        setHeight(height);
        setX(ForestGameScreen.width / 2f - width / 2f);
        setY(ForestGameScreen.height / 2f - height / 2f);

        body.setX(padding.getPadding(com.perevodchik.forest.enums.Padding.LEFT));
        body.setY(padding.getPadding(com.perevodchik.forest.enums.Padding.BOTTOM));
        body.setWidth(width - padding.getPadding(com.perevodchik.forest.enums.Padding.RIGHT) - padding.getPadding(com.perevodchik.forest.enums.Padding.LEFT));
        body.setHeight(height - padding.getPadding(com.perevodchik.forest.enums.Padding.TOP) - padding.getPadding(com.perevodchik.forest.enums.Padding.BOTTOM));
        this.x = body.getX();
        this.y = body.getY();
        this.width = body.getWidth();
        this.height = body.getHeight();

        addActor(body);
        debugAll();
    }

    public void show() {
        this.setVisible(true);
    }

    public void hide() {
        this.setVisible(false);
    }

    public DeathWindow text(String text) {
        Label.LabelStyle style = new Label.LabelStyle();
        style.font = FontUtil.generate(40);
        Label label = new Label(text, style);
        label.setAlignment(Align.center);
        label.setWrap(true);
        labels.add(label);
        body.addActor(label);
        label.debug();
        validateTextCoordinates();
        return this;
    }

    public DeathWindow text(Label label) {
        label.setAlignment(Align.center);
        label.setWrap(true);
        labels.add(label);
        body.addActor(label);
        label.debug();
        validateTextCoordinates();
        return this;
    }

    private void validateTextCoordinates() {
        Label lastLabel = null;
        for(Label l: labels) {
            if(lastLabel == null) {
                l.setY(title.getY() - l.getHeight() - 25);
            } else {
                l.setY(lastLabel.getY() - l.getHeight() - 25);
            }
            l.setX(body.getWidth() / 2 - l.getWidth() / 2);
            lastLabel = l;
        }
    }

    public DeathWindow title(String label) {
        if(title != null)
            body.removeActor(title);
        Label.LabelStyle style = new Label.LabelStyle();
        style.font = FontUtil.generate(60);
        title = new Label(label, style);
        title.setAlignment(Align.center);
        title.setY(body.getHeight() - title.getHeight());
        title.setX(body.getWidth() / 2 - title.getWidth() / 2);
        body.addActor(title);
        title.debug();
        return this;
    }

    public DeathWindow title(Label label) {
        if(title != null)
            body.removeActor(title);
        title = label;
        title.setAlignment(Align.center);
        title.setY(body.getHeight() - title.getHeight());
        title.setX(body.getWidth() / 2 - title.getWidth() / 2);
        body.addActor(title);
        title.debug();
        return this;
    }

    public DeathWindow button(Button button, int buttonIndex) {
        if(buttonIndex > 2 || buttonIndex < 0)
            return this;
        if(buttons[buttonIndex] != null)
            body.removeActor(buttons[buttonIndex]);
        if(buttonIndex == 0) {
            button.setY(0);
            button.setX(0);
        }
        else if(buttonIndex == 1) {
            button.setY(0);
            button.setX(body.getWidth() - button.getWidth());
        }
        button.debug();
        buttons[buttonIndex] = button;
        body.addActor(buttons[buttonIndex]);

        return this;
    }
}
