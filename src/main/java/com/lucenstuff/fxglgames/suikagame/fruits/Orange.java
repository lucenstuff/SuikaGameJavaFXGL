package com.lucenstuff.fxglgames.suikagame.fruits;

import com.almasb.fxgl.entity.Entity;
import javafx.geometry.Point2D;
public class Orange extends Fruit {

    public Orange(Point2D position) {
        super("orange.png", 34);
        this.position = position;
    }

    @Override
    public Entity buildFruit() {
       return super.buildFruit();
    }
}
