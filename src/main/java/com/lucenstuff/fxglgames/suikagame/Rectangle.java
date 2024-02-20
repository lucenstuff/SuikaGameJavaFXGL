//package com.lucenstuff.fxglgames.suikagame;
//
//
//import com.almasb.fxgl.dsl.FXGL;
//import com.almasb.fxgl.entity.Entity;
//import com.almasb.fxgl.entity.components.CollidableComponent;
//import com.almasb.fxgl.physics.PhysicsComponent;
//import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
//import javafx.scene.paint.Color;
//
//public class Rectangle {
//
//    PhysicsComponent containerPhysics = new PhysicsComponent();
//        containerPhysics.setBodyType(BodyType.STATIC);
//
//    PhysicsComponent wallPhysics = new PhysicsComponent();
//        wallPhysics.setBodyType(BodyType.STATIC);
//
//    PhysicsComponent rightWallPhysics = new PhysicsComponent();
//        rightWallPhysics.setBodyType(BodyType.STATIC);
//
//    private Entity createRectangle() {
//        return FXGL.entityBuilder()
//                .at(400, 60)
//                .viewWithBBox(new Rectangle(100, 50, Color.TRANSPARENT))
//                .with(new CollidableComponent(true))
//                .buildAndAttach();
//    }
//}
//
