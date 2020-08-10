package com.perevodchik.forest.ui.inventory;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;

public class AttributeList extends List<String> {
    float itemHeight;
    private float prefWidth, prefHeight;

    public AttributeList(ListStyle style) {
        super(style);
    }

    @Override
    public void layout() {
        BitmapFont font = getStyle().font;
        Drawable selectedDrawable = getStyle().selection;

        itemHeight = font.getCapHeight() - font.getDescent() * 2;

        prefWidth = 0;
        Pool<GlyphLayout> layoutPool = Pools.get(GlyphLayout.class);
        GlyphLayout layout = layoutPool.obtain();
        for (int i = 0; i < getItems().size; i++) {
            layout.setText(font, toString(getItems().get(i)));
            prefWidth = Math.max(layout.width, prefWidth);
        }
        layoutPool.free(layout);
        if(selectedDrawable != null)
            prefWidth += selectedDrawable.getLeftWidth() + selectedDrawable.getRightWidth();
        prefHeight = getItems().size * itemHeight;

        Drawable background = getStyle().background;
        if (background != null) {
            prefWidth += background.getLeftWidth() + background.getRightWidth();
            prefHeight += background.getTopHeight() + background.getBottomHeight();
        }
    }
}
