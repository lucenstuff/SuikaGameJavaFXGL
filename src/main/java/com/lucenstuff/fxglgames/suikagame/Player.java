package com.lucenstuff.fxglgames.suikagame;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.CollidableComponent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

public class Player {

    private final double wallHeight;

    public Player (double wallHeight) {
        this.wallHeight = wallHeight;
    }

    public Entity createPlayer() {
        Entity rectangle = FXGL.entityBuilder()
                .at(400, 60)
                .viewWithBBox(new Rectangle(100, 50, Color.TRANSPARENT))
                .with(new CollidableComponent(true))
                .buildAndAttach();

        Entity line = FXGL.entityBuilder()
                .at(rectangle.getX() + rectangle.getWidth(), rectangle.getY() + rectangle.getHeight())
                .view(new Line(0, 0, 0, wallHeight + 25) {{
                    setStroke(Color.WHITE);
                }})
                .buildAndAttach();

        return rectangle;
    }
}