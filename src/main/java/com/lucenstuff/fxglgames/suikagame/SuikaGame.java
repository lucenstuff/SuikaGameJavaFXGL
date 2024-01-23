package com.lucenstuff.fxglgames.suikagame;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.input.MouseEvent;
import static com.almasb.fxgl.dsl.FXGL.*;

public class SuikaGame extends GameApplication {

    public enum EntityType {
        FRUIT, WALL, FLOOR
    }
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

        FXGL.getInput().addEventHandler(MouseEvent.MOUSE_DRAGGED, event -> {
            double mouseX = event.getSceneX();
            double newRectangleX = mouseX - (rectangle.getWidth() / 2);
            newRectangleX = Math.max(minX, Math.min(newRectangleX, maxX));
            rectangle.setX(newRectangleX);
        });

        FXGL.getInput().addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            Entity newFruit = spawnFruitAt(rectangle.getPosition().add(50, 10));
            FXGL.getGameWorld().addEntity(newFruit);
        });

        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);

        FixtureDef fd = new FixtureDef();
        fd.setDensity(0.03f);
        physics.setFixtureDef(fd);

        fruit = FXGL.entityBuilder()
                .type(EntityType.FRUIT)
                .at(400, 100)
                .view("orange.png")
                .bbox(new HitBox(BoundingShape.Companion.circle(34)))
                .with(physics)
                .buildAndAttach();
    }

    private Entity spawnFruitAt(Point2D position) {
        PhysicsComponent fruitPhysics = new PhysicsComponent();
        fruitPhysics.setBodyType(BodyType.DYNAMIC);

        FixtureDef fruitFixtureDef = new FixtureDef();
        fruitFixtureDef.setDensity(0.03f);
        fruitPhysics.setFixtureDef(fruitFixtureDef);

        return FXGL.entityBuilder()
                .type(EntityType.FRUIT)
                .at(position.subtract(20, 20))
                .view("orange.png")
                .bbox(new HitBox(BoundingShape.circle(34)))
                .with(fruitPhysics)
                .with(new CollidableComponent(true))
                .build();
    }

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setDeveloperMenuEnabled(true);
        settings.setWidth(1280);
        settings.setHeight(720);
        settings.setTitle("Basic Game App");
        settings.setVersion("0.1");
    }

    protected void initInput() {

    }

    protected void initPhysics() {
    }

    protected void initUI() {

        Button resetButton = new Button("Reset");
        resetButton.setOnAction(e -> FXGL.getGameController().startNewGame());
        getGameScene().addUINode(resetButton);

    }

    public static void main(String[] args) {
        launch(args);
    }
}
