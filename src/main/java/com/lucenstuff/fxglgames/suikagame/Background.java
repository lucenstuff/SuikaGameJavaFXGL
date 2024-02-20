package com.lucenstuff.fxglgames.suikagame;

import com.almasb.fxgl.dsl.FXGL;

public class Background {
    private void createBackground() {
        FXGL.entityBuilder()
                .view("background_view.png")
                .buildAndAttach();
    }
}
