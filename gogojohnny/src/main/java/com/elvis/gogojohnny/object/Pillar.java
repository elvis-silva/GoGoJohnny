package com.elvis.gogojohnny.object;

import org.andengine.entity.Entity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.elvis.gogojohnny.manager.ResourceManager;

public class Pillar extends Entity {
    private Sprite pillarUp;
    private Sprite pillarDown;
    private Sprite apple;
    private Sprite carrot;
    private Sprite egg;

    private Body pillarUpBody;
    private Body pillarDownBody;
    private Body scoreSensor;
    private Body carrotSensor;
    private Body appleSensor;
    private Body eggSensor;

    public final static String BODY_WALL = "wall";
    public final static String BODY_ACTOR = "actor";
    public final static String BODY_SENSOR = "sensor";
    public final static String BODY_FLOOR = "floor";
    public final static String BODY_ITEM_SENSOR = "itemSensor";

    public static final FixtureDef PLAYER_FIXTURE = PhysicsFactory.createFixtureDef(1f, 0f, 1f, false);
    public static final FixtureDef WALL_FIXTURE = PhysicsFactory.createFixtureDef(1f, 0f, 1f, false);
    public static final FixtureDef CEILLING_FIXTURE = PhysicsFactory.createFixtureDef(0f, 0f, 0f, false);//(1f, 0f, 0f, false);
    public static final FixtureDef SENSOR_FIXTURE = PhysicsFactory.createFixtureDef(1f, 0f, 1f, true);
    public static final FixtureDef ITEM_SENSOR_FIXTURE = PhysicsFactory.createFixtureDef(1f, 0f, 1f, true);

    public static final float SPEED_X = 14;
    public static final float GRAVITY = 80;
    public static final float SPEED_Y = -27;

    static float shift = 100;

    public Pillar(float x, float y, TextureRegion reg, TextureRegion regUp, VertexBufferObjectManager vbom, PhysicsWorld physics) {
        super(x, y);

        shift = 100;
        pillarUp = new Sprite(-40, shift, 80, 600, regUp, vbom);
        pillarUp.setFlippedVertical(true);
        pillarUp.setSkewCenterY(0);
        attachChild(pillarUp);

        pillarUpBody = PhysicsFactory.createBoxBody(physics, pillarUp, BodyType.StaticBody, WALL_FIXTURE);
        pillarUpBody.setUserData(BODY_WALL);

        pillarDown = new Sprite(-40, -shift -600, 80, 600, reg, vbom);
        pillarDown.setFlippedVertical(true);
        pillarDown.setSkewCenterY(1);
        attachChild(pillarDown);
        
        pillarDownBody = PhysicsFactory.createBoxBody(physics, pillarDown, BodyType.StaticBody, WALL_FIXTURE);
        pillarDownBody.setUserData(BODY_WALL);

        Rectangle r = new Rectangle(40, -shift, 1, 9999, vbom);
        r.setColor(Color.RED);
        r.setAlpha(0f);
        attachChild(r);

        scoreSensor = PhysicsFactory.createBoxBody(physics, r, BodyType.StaticBody, SENSOR_FIXTURE);
        scoreSensor.setUserData(BODY_SENSOR);

        carrot = new Sprite(-25, -25, 50, 50, ResourceManager.getInstance().carrotRegion, vbom);
        attachChild(carrot);

        carrotSensor = PhysicsFactory.createBoxBody(physics, carrot, BodyType.StaticBody, ITEM_SENSOR_FIXTURE);
        carrotSensor.setUserData(BODY_ITEM_SENSOR);

        apple = new Sprite(-25, -25, 50, 50, ResourceManager.getInstance().appleRegion, vbom);
        attachChild(apple);

        appleSensor = PhysicsFactory.createBoxBody(physics, apple, BodyType.StaticBody, ITEM_SENSOR_FIXTURE);
        appleSensor.setUserData(BODY_ITEM_SENSOR);

        egg = new Sprite(-20, -28, 40, 56, ResourceManager.getInstance().eggRegion, vbom);
        attachChild(egg);

        eggSensor = PhysicsFactory.createBoxBody(physics, egg, BodyType.StaticBody, ITEM_SENSOR_FIXTURE);
        eggSensor.setUserData(BODY_ITEM_SENSOR);
    }

    public Body getPillarUpBody() {
        return pillarUpBody;
    }

    public Body getPillarDownBody() {
        return pillarDownBody;
    }

    public Body getScoreSensor() {
        return scoreSensor;
    }

    public Body getCarrotSensor() {
        return carrotSensor;
    }

    public Body getAppleSensor() {
        return appleSensor;
    }

    public Body getEggSensor () {
        return eggSensor;
    }

    public Sprite getCarrot () {
        return carrot;
    }

    public Sprite getApple () {
        return apple;
    }

    public Sprite getEgg () {
        return egg;
    }

    public float getPillarShift() {
        return pillarUp.getHeight() / 2 + shift;
    }

    public float getWidth() {
        return pillarDown.getWidth();
    }
}
