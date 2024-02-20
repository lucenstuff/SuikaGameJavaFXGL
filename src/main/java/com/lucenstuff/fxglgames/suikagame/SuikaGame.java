package com.lucenstuff.fxglgames.suikagame;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.CollisionHandler;
import com.lucenstuff.fxglgames.suikagame.fruits.*;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.util.Duration;


import static com.almasb.fxgl.dsl.FXGL.*;

public class SuikaGame extends GameApplication {
    int APP_WIDTH = 1280;
    int APP_HEIGHT = 720;
    double MIN_X = 340;
    double MAX_X = 840;

    long lastClickTime = 0;
    long cooldownDuration = 650; // in ms

    private IntegerProperty GAME_SCORE;
    private Point2D rectanglePosition = new Point2D(0, 0);

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setAppIcon("watermelon_view.png");
        settings.setDeveloperMenuEnabled(true);
        settings.setWidth(APP_WIDTH);
        settings.setHeight(APP_HEIGHT);
        settings.setTitle("Suika FXGL");
        settings.setVersion("0.1");
    }
    protected void initGame() {

        double wallThickness = 1;
        double floorHeight = getAppHeight() - getAppHeight()*0.0833;
        double wallHeight = getAppHeight()*0.752;
        double floorWidth = getAppWidth() ;
        GAME_SCORE = new SimpleIntegerProperty(0);

        Container container = new Container(wallThickness, floorHeight, wallHeight, floorWidth);
        ContainerEntity containerEntity = container.createContainer();

        //Encapsular Rectangle y Line en EntityPlayer y Player

        Entity rectangle = FXGL.entityBuilder()
                .at(400, 60)
                .viewWithBBox(new Rectangle(100, 50, Color.TRANSPARENT))
                .with(new CollidableComponent(true))
                .buildAndAttach();

        Entity line = FXGL.entityBuilder()
                .at (rectangle.getX() + rectangle.getWidth() , rectangle.getY()+rectangle.getHeight())
                .view(new Line(0, 0, 0, wallHeight+15) {{
                    setStroke(Color.WHITE);
                }})
                .buildAndAttach();


        //Encapsular Inputs en InputHandler

        FXGL.getInput().addEventHandler(MouseEvent.MOUSE_MOVED, event -> {
            double mouseX = event.getSceneX();
            double newRectangleX = mouseX - (rectangle.getWidth() / 2);
            newRectangleX = Math.max(MIN_X, Math.min(newRectangleX, MAX_X));
            rectangle.setX(newRectangleX);
            line.setX(rectangle.getX() + rectangle.getWidth() / 2);
        });



        FXGL.getInput().addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastClickTime >= cooldownDuration){
                lastClickTime = currentTime;
            Entity newFruit = fruitFactory.spawnFruitAt(rectangle.getPosition().add((rectangle.getWidth() / 2-fruitFactory.currentFruitImageView.getImage().getWidth()/2), 0));
            FXGL.getGameWorld().addEntity(newFruit);
            FXGL.play("fruit_drop.wav");
            double imageWidth = fruitFactory.currentFruitImageView.getImage().getWidth();
            double imageHeight = fruitFactory.currentFruitImageView.getImage().getHeight();
            fruitFactory.currentFruitImageView.setX(rectangle.getX()+rectangle.getWidth()/2 - imageWidth/2);
            fruitFactory.currentFruitImageView.setY(60+(rectangle.getHeight())-imageHeight);
            MIN_X = (340+fruitFactory.currentFruitImageView.getImage().getWidth()/2);
            MAX_X = (FXGL.getAppWidth() - 440 - fruitFactory.currentFruitImageView.getImage().getWidth()/2);}
        });

        FXGL.getInput().addEventHandler(MouseEvent.MOUSE_DRAGGED, event -> {
            double mouseX = event.getSceneX();
            double newRectangleX = mouseX - (rectangle.getWidth() / 2);
            newRectangleX = Math.max(MIN_X, Math.min(newRectangleX, MAX_X));
            rectangle.setX(newRectangleX);
            line.setX(rectangle.getX() + rectangle.getWidth() / 2);
        });

        rectangle.xProperty().addListener((obs, oldX, newX) -> {
            double imageWidth = fruitFactory.currentFruitImageView.getImage().getWidth();
            double imageHeight = fruitFactory.currentFruitImageView.getImage().getHeight();
            fruitFactory.currentFruitImageView.setX(newX.doubleValue()+rectangle.getWidth()/2 - imageWidth/2);
            fruitFactory.currentFruitImageView.setY(60+(rectangle.getHeight())-imageHeight);
            MIN_X = (340+fruitFactory.currentFruitImageView.getImage().getWidth()/2);
            MAX_X = (FXGL.getAppWidth() - 440 - fruitFactory.currentFruitImageView.getImage().getWidth()/2);
        });

    }
    FruitFactory fruitFactory = new FruitFactory();

    protected void initPhysics() {
        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(FruitType.CHERRY, FruitType.CHERRY) {
            @Override
            protected void onCollisionBegin(Entity e1, Entity e2) {
                Point2D position1 = e1.getPosition();
                Point2D position2 = e2.getPosition();
                Point2D spawnPoint = position1.add(position2).multiply(0.5);
                Entity strawberry = new Strawberry(spawnPoint).buildFruit();
                FXGL.getGameWorld().addEntity(strawberry);
                e1.removeFromWorld();
                e2.removeFromWorld();
                GAME_SCORE.set(GAME_SCORE.get() + 1);
                FXGL.play("fruit_merge.wav");
            }
        });

        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(FruitType.STRAWBERRY, FruitType.STRAWBERRY) {
            @Override
            protected void onCollisionBegin(Entity e1, Entity e2) {
                Point2D position1 = e1.getPosition();
                Point2D position2 = e2.getPosition();
                Point2D spawnPoint = position1.add(position2).multiply(0.5);
                Entity grape = new Grape(spawnPoint).buildFruit();
                FXGL.getGameWorld().addEntity(grape);
                e1.removeFromWorld();
                e2.removeFromWorld();
                GAME_SCORE.set(GAME_SCORE.get() + 4);
                FXGL.play("fruit_merge.wav");
            }
        });

        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(FruitType.GRAPE, FruitType.GRAPE) {
            @Override
            protected void onCollisionBegin(Entity e1, Entity e2) {
                Point2D position1 = e1.getPosition();
                Point2D position2 = e2.getPosition();
                Point2D spawnPoint = position1.add(position2).multiply(0.5);
                Entity lemon = new Lemon(spawnPoint).buildFruit();
                FXGL.getGameWorld().addEntity(lemon);
                e1.removeFromWorld();
                e2.removeFromWorld();
                GAME_SCORE.set(GAME_SCORE.get() + 9);
                FXGL.play("fruit_merge.wav");
            }
        });

        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(FruitType.LEMON, FruitType.LEMON) {
            @Override
            protected void onCollisionBegin(Entity e1, Entity e2) {
                Point2D position1 = e1.getPosition();
                Point2D position2 = e2.getPosition();
                Point2D spawnPoint = position1.add(position2).multiply(0.5);
                Entity orange = new Orange(spawnPoint).buildFruit();
                FXGL.getGameWorld().addEntity(orange);
                e1.removeFromWorld();
                e2.removeFromWorld();
                GAME_SCORE.set(GAME_SCORE.get() + 14);
                FXGL.play("fruit_merge.wav");
            }
        });
        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(FruitType.ORANGE, FruitType.ORANGE) {
            @Override
            protected void onCollisionBegin(Entity e1, Entity e2) {
                Point2D position1 = e1.getPosition();
                Point2D position2 = e2.getPosition();
                Point2D spawnPoint = position1.add(position2).multiply(0.5);
                Entity apple = new Apple(spawnPoint).buildFruit();
                FXGL.getGameWorld().addEntity(apple);
                e1.removeFromWorld();
                e2.removeFromWorld();
                GAME_SCORE.set(GAME_SCORE.get() + 20);
                FXGL.play("fruit_merge.wav");
            }
        });

        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(FruitType.APPLE, FruitType.APPLE) {
            @Override
            protected void onCollisionBegin(Entity e1, Entity e2) {
                Point2D position1 = e1.getPosition();
                Point2D position2 = e2.getPosition();
                Point2D spawnPoint = position1.add(position2).multiply(0.5);
                Entity pear = new Pear(spawnPoint).buildFruit();
                FXGL.getGameWorld().addEntity(pear);
                e1.removeFromWorld();
                e2.removeFromWorld();
                GAME_SCORE.set(GAME_SCORE.get() + 27);
                FXGL.play("fruit_merge.wav");
            }
        });

        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(FruitType.PEAR, FruitType.PEAR) {
            @Override
            protected void onCollisionBegin(Entity e1, Entity e2) {
                Point2D position1 = e1.getPosition();
                Point2D position2 = e2.getPosition();
                Point2D spawnPoint = position1.add(position2).multiply(0.5);
                Entity peach = new Peach(spawnPoint).buildFruit();
                FXGL.getGameWorld().addEntity(peach);
                e1.removeFromWorld();
                e2.removeFromWorld();
                GAME_SCORE.set(GAME_SCORE.get() + 35);
                FXGL.play("fruit_merge.wav");
            }
        });

        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(FruitType.PEACH, FruitType.PEACH) {
            @Override
            protected void onCollisionBegin(Entity e1, Entity e2) {
                Point2D position1 = e1.getPosition();
                Point2D position2 = e2.getPosition();
                Point2D spawnPoint = position1.add(position2).multiply(0.5);
                Entity pineapple = new Pineapple(spawnPoint).buildFruit();
                FXGL.getGameWorld().addEntity(pineapple);
                e1.removeFromWorld();
                e2.removeFromWorld();
                GAME_SCORE.set(GAME_SCORE.get() + 44);
                FXGL.play("fruit_merge.wav");
            }
        });

        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(FruitType.PINEAPPLE, FruitType.PINEAPPLE) {
            @Override
            protected void onCollisionBegin(Entity e1, Entity e2) {
                Point2D position1 = e1.getPosition();
                Point2D position2 = e2.getPosition();
                Point2D spawnPoint = position1.add(position2).multiply(0.5);
                Entity melon = new Melon(spawnPoint).buildFruit();
                FXGL.getGameWorld().addEntity(melon);
                e1.removeFromWorld();
                e2.removeFromWorld();
                GAME_SCORE.set(GAME_SCORE.get() + 54);
                FXGL.play("fruit_merge.wav");
            }
        });
        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(FruitType.MELON, FruitType.MELON) {
            @Override
            protected void onCollisionBegin(Entity e1, Entity e2) {
                Point2D position1 = e1.getPosition();
                Point2D position2 = e2.getPosition();
                Point2D spawnPoint = position1.add(position2).multiply(0.5);
                Entity watermelon = new Watermelon(spawnPoint).buildFruit();
                FXGL.getGameWorld().addEntity(watermelon);
                e1.removeFromWorld();
                e2.removeFromWorld();
                GAME_SCORE.set(GAME_SCORE.get() + 65);
                FXGL.play("fruit_merge.wav");
            }
        });
        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(FruitType.WATERMELON, FruitType.WATERMELON) {
            @Override
            protected void onCollisionBegin(Entity e1, Entity e2) {
                e1.removeFromWorld();
                e2.removeFromWorld();
                GAME_SCORE.set(GAME_SCORE.get() + 77);
                FXGL.play("fruit_merge.wav");
            }
        });

        //End Game Condition

        FruitType[] fruitTypes = {
                FruitType.CHERRY, FruitType.STRAWBERRY, FruitType.GRAPE, FruitType.LEMON, FruitType.ORANGE, FruitType.APPLE, FruitType.PEAR, FruitType.PEACH, FruitType.PINEAPPLE, FruitType.MELON, FruitType.WATERMELON
        };

        for (FruitType fruitType : fruitTypes) {
            FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(fruitType, ContainerType.LOOSE_COLLIDER) {
                @Override
                protected void onCollisionBegin(Entity fruit, Entity looseCollider) {
                    FXGL.getGameTimer().runOnceAfter(() -> {
                        if (fruit.isActive() && fruit.isColliding(looseCollider)) {
                            endGame();
                        }
                    }, Duration.seconds(2));
                }
            });
        }
    }

    private void endGame() {
        FXGL.getDialogService().showMessageBox("Game Over, your score is: " + GAME_SCORE.get()+"\n Do you want to play again? ", () -> {
            GAME_SCORE.set(0);
            for (Enum  fruitType : FruitType.values()) {
                getGameWorld().getEntitiesByType(fruitType).forEach(Entity::removeFromWorld);
            }
        });
    }

    protected void initUI() {
        fruitFactory.nextFruitImageView = new ImageView();
        fruitFactory.nextFruitImageView.setFitWidth(60);
        fruitFactory.nextFruitImageView.setFitHeight(60);
        fruitFactory.nextFruitImageView.setX(1095);
        fruitFactory.nextFruitImageView.setY(120);
        getGameScene().addUINode(fruitFactory.nextFruitImageView);

        fruitFactory.currentFruitImageView = new ImageView();
        fruitFactory.currentFruitImageView.setX(rectanglePosition.getX());
        fruitFactory.currentFruitImageView.setY(rectanglePosition.getY());
        getGameScene().addUINode(fruitFactory.currentFruitImageView);

        Text scoreText = FXGL.getUIFactoryService().newText("", Color.WHITE, 60);
        scoreText.textProperty().bind(GAME_SCORE.asString());
        scoreText.setX(105);
        scoreText.setY(160);
        scoreText.setStroke(Color.BLACK);
        scoreText.setStrokeWidth(2);
        getGameScene().addUINode(scoreText);

        Entity ringOfFruits = FXGL.entityBuilder()
                .view("ring_view.png")
                .at(1000, 400)
                .buildAndAttach();

        Entity NextFruitBuuble = FXGL.entityBuilder()
                .view("next_fruit_view.png")
                .at(1040, 50)
                .buildAndAttach();

        Entity ScoreBubble = FXGL.entityBuilder()
                .view("score_view.png")
                .at(40, 50)
                .buildAndAttach();

        fruitFactory.initFruits();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
