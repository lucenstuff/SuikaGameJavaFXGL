package com.lucenstuff.fxglgames.suikagame.fruits;

import com.almasb.fxgl.entity.Entity;
import com.lucenstuff.fxglgames.suikagame.FruitType;
import javafx.geometry.Point2D;
public class Melon extends Fruit {

    public Melon(Point2D position) {
        super("melon_view.png", 92.5, FruitType.MELON);
        this.position = position;
    }

    @Override
    public Entity buildFruit() {
        return super.buildFruit();
    }
}
