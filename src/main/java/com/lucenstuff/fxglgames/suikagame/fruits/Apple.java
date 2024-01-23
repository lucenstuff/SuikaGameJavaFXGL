package com.lucenstuff.fxglgames.suikagame.fruits;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.texture.Texture;
import javafx.geometry.Point2D;

public class Apple extends Fruit{
    private static final double HITBOXRADIUS = 30;

    public Apple(Point2D position) {
        super("apple",position, HITBOXRADIUS);
    }

    @Override
    protected Texture getTexture(String textureName) {
        return FXGL.texture(textureName + ".png");
    }
}
