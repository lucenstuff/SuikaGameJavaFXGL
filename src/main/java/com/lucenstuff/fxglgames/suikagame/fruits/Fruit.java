package com.lucenstuff.fxglgames.suikagame.fruits;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;
import com.lucenstuff.fxglgames.suikagame.EntityType;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;

public abstract class Fruit {
    String textureName;
    double bBoxRadius;
    PhysicsComponent fruitPhysics = new PhysicsComponent();
    Point2D position;

    EntityType entityType;

    public Fruit(String textureName, double bBoxRadius, EntityType entityType) {

        this.textureName = textureName;
        this.bBoxRadius = bBoxRadius;
        this.fruitPhysics = new PhysicsComponent();
        this.entityType = entityType;

        fruitPhysics.setBodyType(BodyType.DYNAMIC);
        FixtureDef fruitFixtureDef = new FixtureDef();
        fruitFixtureDef.setDensity(0.03f);
        fruitPhysics.setFixtureDef(fruitFixtureDef);
    }
    public Entity buildFruit() {
        return FXGL.entityBuilder()
                .type(entityType)
                .at(position)
                .view(textureName)
                .bbox(new HitBox(BoundingShape.circle(bBoxRadius)))
                .with(fruitPhysics)
                .with(new CollidableComponent(true))
                .build();
    }

    public Image getTexture() {
        return FXGL.getAssetLoader().loadImage(textureName);
    }
}
