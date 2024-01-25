package com.lucenstuff.fxglgames.suikagame;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;
import com.lucenstuff.fxglgames.suikagame.fruits.*;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.input.MouseEvent;

import java.util.Random;

import static com.almasb.fxgl.dsl.FXGL.*;

public class SuikaGame extends GameApplication {

    private Entity fruit;

    protected void initGame() {

        double wallThickness = 15;
        double floorHeight = getAppHeight() - wallThickness * 4;
        double wallHeight = 500;
        double floorWidth = getAppWidth() - 2 * 200 - 2 * wallThickness + 2 * wallThickness;

        PhysicsComponent containerPhysics = new PhysicsComponent();
        containerPhysics.setBodyType(BodyType.STATIC);

        PhysicsComponent wallPhysics = new PhysicsComponent();
        wallPhysics.setBodyType(BodyType.STATIC);


        FXGL.entityBuilder()
                .type(EntityType.FLOOR)
                .at(200, floorHeight)
                .viewWithBBox(new Rectangle(floorWidth, wallThickness, Color.GREY))
                .with(new CollidableComponent(true))
                .with(containerPhysics)
                .buildAndAttach();

        Entity leftWall = FXGL.entityBuilder()
                .type(EntityType.WALL)
                .at(350, floorHeight - wallHeight)
                .viewWithBBox(new Rectangle(wallThickness, wallHeight, Color.GREY))
                .with(new PhysicsComponent(), new CollidableComponent(true))
                .buildAndAttach();

        PhysicsComponent rightWallPhysics = new PhysicsComponent();
        rightWallPhysics.setBodyType(BodyType.STATIC);

        Entity rightWall = FXGL.entityBuilder()
                .type(EntityType.WALL)
                .at(getAppWidth() - wallThickness - 350, floorHeight - wallHeight)
                .viewWithBBox(new Rectangle(wallThickness, wallHeight, Color.GREY))
                .with(rightWallPhysics, new CollidableComponent(true))
                .buildAndAttach();

        Entity rectangle = FXGL.entityBuilder()
                .at(400, 120)
                .viewWithBBox(new Rectangle(100, 20, Color.BLUE))
                .with(new CollidableComponent(true))
                .buildAndAttach();

        rectangle.getViewComponent().addEventHandler(MouseEvent.MOUSE_MOVED, event -> {
            double mouseX = event.getX();
            double newRectangleX = mouseX - 50;
            rectangle.setPosition(newRectangleX, rectangle.getY());
        });

        double minX = 330;
        double maxX = FXGL.getAppWidth() - 440;

        FXGL.getInput().addEventHandler(MouseEvent.MOUSE_MOVED, event -> {
            double mouseX = event.getSceneX();
            double newRectangleX = mouseX - (rectangle.getWidth() / 2);
            newRectangleX = Math.max(minX, Math.min(newRectangleX, maxX));
            rectangle.setX(newRectangleX);
        });

        FXGL.getInput().addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            Entity newFruit = spawnFruitAt(rectangle.getPosition().add(50, 10));
            FXGL.getGameWorld().addEntity(newFruit);
        });

        FXGL.getInput().addEventHandler(MouseEvent.MOUSE_DRAGGED, event -> {
            double mouseX = event.getSceneX();
            double newRectangleX = mouseX - (rectangle.getWidth() / 2);
            newRectangleX = Math.max(minX, Math.min(newRectangleX, maxX));
            rectangle.setX(newRectangleX);
        });

        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);

        FixtureDef fd = new FixtureDef();
        fd.setDensity(0.03f);
        physics.setFixtureDef(fd);
    }

    private Entity spawnFruitAt(Point2D position) {
        Fruit[] fruits = {new Cherry(position), new Grape(position), new Lemon(position), new Strawberry(position), new Orange(position), new Apple(position)};
        int randomIndex = new Random().nextInt(fruits.length);
        return fruits[randomIndex].buildFruit();
    }

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setDeveloperMenuEnabled(true);
        settings.setWidth(1280);
        settings.setHeight(720);
        settings.setTitle("Basic Game App");
        settings.setVersion("0.1");
    }

    protected void initPhysics() {

        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.CHERRY, EntityType.CHERRY) {
            @Override
            protected void onCollisionBegin(Entity e1, Entity e2) {
                Point2D position1 = e1.getPosition();
                Point2D position2 = e2.getPosition();
                Point2D spawnPoint = position1.add(position2).multiply(0.5);
                Entity strawberry = new Strawberry(spawnPoint).buildFruit();
                FXGL.getGameWorld().addEntity(strawberry);
                e1.removeFromWorld();
                e2.removeFromWorld();
            }
        });

        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.STRAWBERRY, EntityType.STRAWBERRY) {
            @Override
            protected void onCollisionBegin(Entity e1, Entity e2) {
                Point2D position1 = e1.getPosition();
                Point2D position2 = e2.getPosition();
                Point2D spawnPoint = position1.add(position2).multiply(0.5);
                Entity grape = new Grape(spawnPoint).buildFruit();
                FXGL.getGameWorld().addEntity(grape);
                e1.removeFromWorld();
                e2.removeFromWorld();
            }
        });

        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.GRAPE, EntityType.GRAPE) {
            @Override
            protected void onCollisionBegin(Entity e1, Entity e2) {
                Point2D position1 = e1.getPosition();
                Point2D position2 = e2.getPosition();
                Point2D spawnPoint = position1.add(position2).multiply(0.5);
                Entity lemon = new Lemon(spawnPoint).buildFruit();
                FXGL.getGameWorld().addEntity(lemon);
                e1.removeFromWorld();
                e2.removeFromWorld();
            }
        });

        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.LEMON, EntityType.LEMON) {
            @Override
            protected void onCollisionBegin(Entity e1, Entity e2) {
                Point2D position1 = e1.getPosition();
                Point2D position2 = e2.getPosition();
                Point2D spawnPoint = position1.add(position2).multiply(0.5);
                Entity orange = new Orange(spawnPoint).buildFruit();
                FXGL.getGameWorld().addEntity(orange);
                e1.removeFromWorld();
                e2.removeFromWorld();
            }
        });
        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.ORANGE, EntityType.ORANGE) {
            @Override
            protected void onCollisionBegin(Entity e1, Entity e2) {
                Point2D position1 = e1.getPosition();
                Point2D position2 = e2.getPosition();
                Point2D spawnPoint = position1.add(position2).multiply(0.5);
                Entity apple = new Apple(spawnPoint).buildFruit();
                FXGL.getGameWorld().addEntity(apple);
                e1.removeFromWorld();
                e2.removeFromWorld();
            }
        });

        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.APPLE, EntityType.APPLE) {
            @Override
            protected void onCollisionBegin(Entity e1, Entity e2) {
                Point2D position1 = e1.getPosition();
                Point2D position2 = e2.getPosition();
                Point2D spawnPoint = position1.add(position2).multiply(0.5);
                Entity pear = new Pear(spawnPoint).buildFruit();
                FXGL.getGameWorld().addEntity(pear);
                e1.removeFromWorld();
                e2.removeFromWorld();
            }
        });

        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.PEAR, EntityType.PEAR) {
            @Override
            protected void onCollisionBegin(Entity e1, Entity e2) {
                Point2D position1 = e1.getPosition();
                Point2D position2 = e2.getPosition();
                Point2D spawnPoint = position1.add(position2).multiply(0.5);
                Entity peach = new Peach(spawnPoint).buildFruit();
                FXGL.getGameWorld().addEntity(peach);
                e1.removeFromWorld();
                e2.removeFromWorld();
            }
        });

        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.PEACH, EntityType.PEACH) {
            @Override
            protected void onCollisionBegin(Entity e1, Entity e2) {
                Point2D position1 = e1.getPosition();
                Point2D position2 = e2.getPosition();
                Point2D spawnPoint = position1.add(position2).multiply(0.5);
                Entity pineapple = new Pineapple(spawnPoint).buildFruit();
                FXGL.getGameWorld().addEntity(pineapple);
                e1.removeFromWorld();
                e2.removeFromWorld();
            }
        });

        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.PINEAPPLE, EntityType.PINEAPPLE) {
            @Override
            protected void onCollisionBegin(Entity e1, Entity e2) {
                Point2D position1 = e1.getPosition();
                Point2D position2 = e2.getPosition();
                Point2D spawnPoint = position1.add(position2).multiply(0.5);
                Entity melon = new Melon(spawnPoint).buildFruit();
                FXGL.getGameWorld().addEntity(melon);
                e1.removeFromWorld();
                e2.removeFromWorld();
            }
        });

        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.MELON, EntityType.MELON) {
            @Override
            protected void onCollisionBegin(Entity e1, Entity e2) {
                Point2D position1 = e1.getPosition();
                Point2D position2 = e2.getPosition();
                Point2D spawnPoint = position1.add(position2).multiply(0.5);
                Entity watermelon = new Watermelon(spawnPoint).buildFruit();
                FXGL.getGameWorld().addEntity(watermelon);
                e1.removeFromWorld();
                e2.removeFromWorld();
            }
        });
        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.WATERMELON, EntityType.WATERMELON) {
            @Override
            protected void onCollisionBegin(Entity e1, Entity e2) {
                e1.removeFromWorld();
                e2.removeFromWorld();
            }
        });

    }


    protected void initUI() {

    }

    public static void main(String[] args) {
        launch(args);
    }
}
