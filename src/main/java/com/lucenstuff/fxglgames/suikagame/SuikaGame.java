package com.lucenstuff.fxglgames.suikagame;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;
import com.lucenstuff.fxglgames.suikagame.fruits.Orange;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.input.MouseEvent;


import static com.almasb.fxgl.dsl.FXGL.*;

public class SuikaGame extends GameApplication {

//    ArrayList<Fruit> fruitArrayList = new ArrayList<>(Arrays.asList(
//            new Grape(new Point2D(0, 0)),
//            new Strawberry(new Point2D(0, 0)),
//            new Lemon(new Point2D(0, 0)),
//            new Orange(new Point2D(0, 0))
//    ));
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
       return new Orange(position).buildFruit();
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
