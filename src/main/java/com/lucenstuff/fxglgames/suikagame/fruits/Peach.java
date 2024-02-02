package com.lucenstuff.fxglgames.suikagame.fruits;

import com.almasb.fxgl.entity.Entity;
import com.lucenstuff.fxglgames.suikagame.FruitType;
import javafx.geometry.Point2D;
public class Peach extends Fruit {

    public Peach(Point2D position) {
        super("peach_view.png", 62.5, FruitType.PEACH);
        this.position = position;
    }

    @Override
    public Entity buildFruit() {
        return super.buildFruit();
    }
}
