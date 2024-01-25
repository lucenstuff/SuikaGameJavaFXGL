package com.lucenstuff.fxglgames.suikagame.fruits;

import com.almasb.fxgl.entity.Entity;
import javafx.geometry.Point2D;
public class Lemon extends Fruit {

    public Lemon(Point2D position) {
        super("lemon.png", 34);
        this.position = position;
    }

    @Override
    public Entity buildFruit() {
        return super.buildFruit();
    }
}
