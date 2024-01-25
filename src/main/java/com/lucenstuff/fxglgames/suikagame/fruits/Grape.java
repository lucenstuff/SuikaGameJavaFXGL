package com.lucenstuff.fxglgames.suikagame.fruits;

import com.almasb.fxgl.entity.Entity;
import com.lucenstuff.fxglgames.suikagame.EntityType;
import javafx.geometry.Point2D;
public class Grape extends Fruit {

    public Grape(Point2D position) {
        super("grape_view.png", 31.33, EntityType.GRAPE);
        this.position = position;
    }

    @Override
    public Entity buildFruit() {
        return super.buildFruit();
    }
}
