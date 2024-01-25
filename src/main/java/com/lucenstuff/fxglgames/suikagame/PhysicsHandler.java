package com.lucenstuff.fxglgames.suikagame;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.GameWorld;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.PhysicsWorld;
import com.lucenstuff.fxglgames.suikagame.fruits.*;
import javafx.geometry.Point2D;

public class PhysicsHandler {
    private final PhysicsWorld physicsWorld;
    private final GameWorld gameWorld;

    public PhysicsHandler(PhysicsWorld physicsWorld, GameWorld gameWorld) {
        this.physicsWorld = physicsWorld;
        this.gameWorld = gameWorld;
    }

    public void initPhysics() {
        return;
    }
}
