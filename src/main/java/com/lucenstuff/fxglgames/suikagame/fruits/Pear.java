package com.lucenstuff.fxglgames.suikagame.fruits;

import com.almasb.fxgl.entity.Entity;
import com.lucenstuff.fxglgames.suikagame.EntityType;
import javafx.geometry.Point2D;
public class Pear extends Fruit {

    public Pear(Point2D position) {
        super("pear_view.png", 56, EntityType.PEAR);
        this.position = position;
    }

    @Override
    public Entity buildFruit() {
        return super.buildFruit();
    }
}
