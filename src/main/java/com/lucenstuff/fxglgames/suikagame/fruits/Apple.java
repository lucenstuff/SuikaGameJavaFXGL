package com.lucenstuff.fxglgames.suikagame.fruits;

import com.almasb.fxgl.entity.Entity;
import com.lucenstuff.fxglgames.suikagame.EntityType;
import javafx.geometry.Point2D;
public class Apple extends Fruit {

    public Apple(Point2D position) {
        super("apple_view.png", 47.5, EntityType.APPLE);
        this.position = position;
    }

    @Override
    public Entity buildFruit() {
        return super.buildFruit();
    }
}
