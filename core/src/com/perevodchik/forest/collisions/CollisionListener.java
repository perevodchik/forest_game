package com.perevodchik.forest.collisions;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.perevodchik.forest.entity.Entity;

import java.util.ArrayList;

public class CollisionListener implements ContactListener {
    private static ArrayList<ContactHandler> handlers = new ArrayList<>();
    private ContactHandler listener;

    public static void init() {
        addHandler(new BulletWithItemContactHandler());
        addHandler(new BulletWithWallContactHandler());
        addHandler(new EntityWithBulletContactHandler());
        addHandler(new EntityWithItemContactHandler());
    }

    public CollisionListener() {
        init();
    }

    public static void addHandler(ContactHandler handler) {
        handlers.add(handler);
    }

    @Override
    public void beginContact(Contact contact) {
        for(ContactHandler handler: handlers) {
            if(handler.isValid(
                    (Entity) contact.getFixtureA().getBody().getUserData(),
                    (Entity) contact.getFixtureB().getBody().getUserData()
            )) {
                listener = handler;
                break;
            }
        }
        if(listener != null)
            listener.collision();
    }

    @Override
    public void endContact(Contact contact) {
        if(listener != null)
            listener.endCollision();
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) { }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) { }
}
