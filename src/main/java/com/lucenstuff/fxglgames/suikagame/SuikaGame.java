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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.input.MouseEvent;

import java.util.*;

import static com.almasb.fxgl.dsl.FXGL.*;

public class SuikaGame extends GameApplication {

    int APP_WIDTH = 1280;
    int APP_HEIGHT = 720;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setDeveloperMenuEnabled(true);
        settings.setWidth(APP_WIDTH);
        settings.setHeight(APP_HEIGHT);
        settings.setTitle("Basic Game App");
        settings.setVersion("0.1");
    }
    protected void initGame() {

        double wallThickness = 15;
        double floorHeight = getAppHeight() - wallThickness * 4;
        double wallHeight = 520;
        double floorWidth = getAppWidth() - 2 * 400 - 2 * wallThickness + 2 * wallThickness;

        PhysicsComponent containerPhysics = new PhysicsComponent();
        containerPhysics.setBodyType(BodyType.STATIC);

        PhysicsComponent wallPhysics = new PhysicsComponent();
        wallPhysics.setBodyType(BodyType.STATIC);

        PhysicsComponent rightWallPhysics = new PhysicsComponent();
        rightWallPhysics.setBodyType(BodyType.STATIC);

        Entity backgroundImg = FXGL.entityBuilder()
                .view("background_view.png")
                .buildAndAttach();

        Entity ContainerImg = FXGL.entityBuilder()
                .view("container_view.png")
                .at(387.5, 135)
                .zIndex(10)
                .buildAndAttach();


        Entity rectangle = FXGL.entityBuilder()
                .at(400, 60)
                .viewWithBBox(new Rectangle(100, 50, Color.BLUE))
                .with(new CollidableComponent(true))
                .buildAndAttach();

        Entity line = FXGL.entityBuilder()
                .at (rectangle.getX() + rectangle.getWidth() , rectangle.getY()+rectangle.getHeight())
                .view(new Line(0, 0, 0, wallHeight+25) {{
                    setStroke(Color.WHITE);
                }})
                .buildAndAttach();

        Entity floor = FXGL.entityBuilder()
                .type(EntityType.FLOOR)
                .at(400, floorHeight)
                .viewWithBBox(new Rectangle(floorWidth, wallThickness, Color.TRANSPARENT))
                .with(new CollidableComponent(true))
                .with(containerPhysics)
                .buildAndAttach();

        Entity leftWall = FXGL.entityBuilder()
                .type(EntityType.WALL)
                .at(387.5, floorHeight - wallHeight)
                .viewWithBBox(new Rectangle(wallThickness, wallHeight, Color.TRANSPARENT))
                .with(new PhysicsComponent(), new CollidableComponent(true))
                .buildAndAttach();

        Entity rightWall = FXGL.entityBuilder()
                .type(EntityType.WALL)
                .at(getAppWidth() - wallThickness - 387.5, floorHeight - wallHeight)
                .viewWithBBox(new Rectangle(wallThickness, wallHeight, Color.TRANSPARENT))
                .with(rightWallPhysics, new CollidableComponent(true))
                .buildAndAttach();



        double minX = 330;
        double maxX = FXGL.getAppWidth() - 440;

        FXGL.getInput().addEventHandler(MouseEvent.MOUSE_MOVED, event -> {
            double mouseX = event.getSceneX();
            double newRectangleX = mouseX - (rectangle.getWidth() / 2);
            newRectangleX = Math.max(minX, Math.min(newRectangleX, maxX));
            rectangle.setX(newRectangleX);
            line.setX(rectangle.getX() + rectangle.getWidth() / 2);
        });


        FXGL.getInput().addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            Entity newFruit = spawnFruitAt(rectangle.getPosition());
            FXGL.getGameWorld().addEntity(newFruit);
        });

        FXGL.getInput().addEventHandler(MouseEvent.MOUSE_DRAGGED, event -> {
            double mouseX = event.getSceneX();
            double newRectangleX = mouseX - (rectangle.getWidth() / 2);
            newRectangleX = Math.max(minX, Math.min(newRectangleX, maxX));
            rectangle.setX(newRectangleX);
            line.setX(rectangle.getX() + rectangle.getWidth() / 2);
        });

    }
    private final Queue<Fruit> fruitQueue = new LinkedList<>();
    private ImageView nextFruitImageView;

    private Entity spawnFruitAt(Point2D currentPosition) {
        if (fruitQueue.isEmpty()) {
            fillFruitQueue(currentPosition);
        }

        Fruit fruitToSpawn = fruitQueue.poll();

        enqueueRandomFruit(currentPosition);

        updateNextFruitImageView();

        Entity newFruit = fruitToSpawn.buildFruit();
        newFruit.setPosition(currentPosition);

        return newFruit;
    }

    private void fillFruitQueue(Point2D position) {
        fruitQueue.addAll(Arrays.asList(
                new Cherry(position),
                new Grape(position),
                new Lemon(position),
                new Strawberry(position),
                new Orange(position),
                new Apple(position)
        ));
        Collections.shuffle((List<?>) fruitQueue);
    }

    private void enqueueRandomFruit(Point2D position) {
        Fruit[] fruits = {
                new Cherry(position),
                new Grape(position),
                new Lemon(position),
                new Strawberry(position),
                new Orange(position),
                new Apple(position)
        };
        int randomIndex = new Random().nextInt(fruits.length);
        fruitQueue.offer(fruits[randomIndex]);
    }

    private void updateNextFruitImageView() {
        if (fruitQueue.peek() != null) {
            Image nextFruitTexture = fruitQueue.peek().getTexture();
            nextFruitImageView.setImage(nextFruitTexture);
        }
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
        nextFruitImageView = new ImageView();
        nextFruitImageView.setFitWidth(60);
        nextFruitImageView.setFitHeight(60);
        nextFruitImageView.setX(1095);
        nextFruitImageView.setY(120);
        getGameScene().addUINode(nextFruitImageView);

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

    }


    public static void main(String[] args) {
        launch(args);
    }
}
