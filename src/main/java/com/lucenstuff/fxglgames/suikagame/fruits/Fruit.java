package com.lucenstuff.fxglgames.suikagame.fruits;

import com.almasb.fxgl.dsl.EntityBuilder;
import com.almasb.fxgl.entity.Entity;

import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;
import com.almasb.fxgl.texture.Texture;
import com.lucenstuff.fxglgames.suikagame.SuikaGame;
import javafx.geometry.Point2D;

public abstract class Fruit extends Entity {
    public Fruit(String textureName, Point2D position, double hitboxRadius) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);

        FixtureDef fd = new FixtureDef();
        fd.setDensity(0.03f);
        physics.setFixtureDef(fd);

        Texture texture = getTexture(textureName);

        Entity fruitEntity = new EntityBuilder()
                .type(SuikaGame.EntityType.FRUIT)
                .at(position.subtract(20, 20))
                .viewWithBBox(texture)
                .bbox(new HitBox(BoundingShape.circle(hitboxRadius)))
                .with(physics)
                .build();

        this.addComponent(physics);
        this.getViewComponent().addChild(texture);
    }

    protected abstract Texture getTexture(String textureName);
}