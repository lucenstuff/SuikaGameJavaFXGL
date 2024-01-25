package com.lucenstuff.fxglgames.suikagame.fruits;

import com.almasb.fxgl.entity.Entity;
import com.lucenstuff.fxglgames.suikagame.EntityType;
import javafx.geometry.Point2D;
public class Orange extends Fruit {

    public Orange(Point2D position) {
        super("orange_view.png", 42, EntityType.ORANGE);
        this.position = position;
    }

    @Override
    public Entity buildFruit() {
       return super.buildFruit();
    }
}
