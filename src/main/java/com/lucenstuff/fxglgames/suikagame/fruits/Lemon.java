package com.lucenstuff.fxglgames.suikagame.fruits;

import com.almasb.fxgl.entity.Entity;
import com.lucenstuff.fxglgames.suikagame.FruitType;
import javafx.geometry.Point2D;
public class Lemon extends Fruit {

    public Lemon(Point2D position) {
        super("lemon_view.png", 35, FruitType.LEMON);
        this.position = position;
    }

    @Override
    public Entity buildFruit() {
        return super.buildFruit();
    }
}
