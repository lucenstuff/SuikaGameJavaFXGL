//package com.lucenstuff.fxglgames.suikagame;
//
//import com.almasb.fxgl.dsl.FXGL;
//import com.almasb.fxgl.entity.Entity;
//import com.almasb.fxgl.physics.CollisionHandler;
//import javafx.geometry.Point2D;
//import javafx.util.Duration;
//
//public class CollisionHandlers {
//
//    public static void initCollisionHandlers() {
//        initFruitCollisions();
//        initEndGameCondition();
//    }
//
//    private static void initFruitCollisions() {
//        addFruitCollisionHandler(FruitType.CHERRY, FruitType.CHERRY, 1);
//        addFruitCollisionHandler(FruitType.STRAWBERRY, FruitType.STRAWBERRY, 4);
//        addFruitCollisionHandler(FruitType.GRAPE, FruitType.GRAPE, 9);
//        addFruitCollisionHandler(FruitType.LEMON, FruitType.LEMON, 14);
//        addFruitCollisionHandler(FruitType.ORANGE, FruitType.ORANGE, 20);
//        addFruitCollisionHandler(FruitType.APPLE, FruitType.APPLE, 27);
//        addFruitCollisionHandler(FruitType.PEAR, FruitType.PEAR, 35);
//        addFruitCollisionHandler(FruitType.PEACH, FruitType.PEACH, 44);
//        addFruitCollisionHandler(FruitType.PINEAPPLE, FruitType.PINEAPPLE, 54);
//        addFruitCollisionHandler(FruitType.MELON, FruitType.MELON, 65);
//        addFruitCollisionHandler(FruitType.WATERMELON, FruitType.WATERMELON, 77);
//    }
//
//    private static void initEndGameCondition() {
//        for (FruitType fruitType : FruitType.values()) {
//            addFruitCollisionHandler(fruitType, ContainerType.LOOSE_COLLIDER, 0);
//        }
//    }
//
//    private static void addFruitCollisionHandler(FruitType type1, FruitType type2, int scoreIncrease) {
//        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(type1, type2) {
//            @Override
//            protected void onCollisionBegin(Entity e1, Entity e2) {
//                Point2D position1 = e1.getPosition();
//                Point2D position2 = e2.getPosition();
//                Point2D spawnPoint = position1.add(position2).multiply(0.5);
//                Entity newFruit = FruitFactory.createFruit(type1, spawnPoint);
//                FXGL.getGameWorld().addEntity(newFruit);
//                e1.removeFromWorld();
//                e2.removeFromWorld();
//                GAME_SCORE.set(GAME_SCORE.get() + scoreIncrease);
//                FXGL.play("fruit_merge.wav");
//            }
//        });
//    }
//}
