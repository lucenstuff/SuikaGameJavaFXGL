package com.lucenstuff.fxglgames.suikagame.fruits;

import com.almasb.fxgl.entity.Entity;
import com.lucenstuff.fxglgames.suikagame.EntityType;
import javafx.geometry.Point2D;
public class Watermelon extends Fruit {

    public Watermelon(Point2D position) {
        super("peach_view.png", 62.5, EntityType.WATERMELON);
        this.position = position;
    }

    @Override
    public Entity buildFruit() {
        return super.buildFruit();
    }
}
