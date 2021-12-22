package com.elvis.gogojohnny.scene;

import java.util.Iterator;
import java.util.LinkedList;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.AutoParallaxBackground;
import org.andengine.entity.scene.background.ParallaxBackground;
import org.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.HorizontalAlign;

import com.elvis.gogojohnny.GoGoJohnny;
import com.elvis.gogojohnny.manager.SceneManager;
import com.elvis.gogojohnny.manager.SceneManager.SceneType;
import com.elvis.gogojohnny.object.Pillar;
import com.elvis.gogojohnny.object.PillarFactory;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

public class GameScene extends BaseScene implements IOnSceneTouchListener, ContactListener{

    private static final int TIME_TO_RESSURECTION = 200;
    private final String TAP_TO_PLAY = "TAP TO PLAY";
    private final String TAP_TO_PLAY_AGAIN = "TAP TO PLAY AGAIN";
    private final String YOUR_SCORE = "Your Score: ";
    private final String HIGH_SCORE = "High Score: ";
    private final String SCORE = "Score: ";
    private HUD gameHUD;
    private Text scoreText;
    private Text tapToPlayText;
    private Text tapToPlayAgainText;
    private PhysicsWorld physicWorld;
    LinkedList<Pillar> pillars = new LinkedList<Pillar>();
    private TiledSprite player;
    protected Body playerBody;
    private boolean scored;
    private boolean scoredItem;
    private int score;
    private int controler = 0;
    ParallaxBackground autoParallaxBackground;
    private float rotationControler = 0;
    private String rotationState = "";
    private String deadState = "no";
    private Sprite ground;
    private float playerY = 0;
    private int jumpControl = 0;
    private boolean jumping = false;
    private int itensController = 0;
    private Sprite parallaxTapBackLayerSprite;

    public enum STATE {
        NEW, PAUSED, PLAY, DEAD, AFTERLIFE;
    }

    long timestamp = 0;
    private STATE state = STATE.NEW;
    private STATE lastState;

    @Override
    public void createScene() {

        createPhysics();
        PillarFactory.getInstance().create(physicWorld);
        createBackground();
        createHUD();
        createBounds();
        createActor();
        resourceManager.camera.setChaseEntity(player);
        setOnSceneTouchListener(this);

        try {
            activity.getHighScore();
        } catch (Exception e) {
            activity.setHighScore(0);
        }
    }

    private void createPhysics() {
        physicWorld = new PhysicsWorld(new Vector2(0, 0), true);
        physicWorld.setContactListener(this);
        registerUpdateHandler(physicWorld);
    }

    private void createBackground() {
        autoParallaxBackground = new AutoParallaxBackground(0f, 0f, 5f, 60);
        this.setBackground(autoParallaxBackground);

        final Sprite parallaxBackLayerSprite = new Sprite(0, -150, resourceManager.bgRegion, vbom);
        parallaxBackLayerSprite.setSkewCenter(0, 0);

        autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(0.0f, parallaxBackLayerSprite));

        this.setBackground(autoParallaxBackground);
        this.setBackgroundEnabled(true);
    }

    private void createBounds() {
        float bigNumber = 999999;
        resourceManager.parallaxFrontLayerRegion.setTextureWidth(bigNumber);
        ground = new Sprite(0, 780 - 150, resourceManager.parallaxFrontLayerRegion, vbom);
        ground.setHeight(32f);
        ground.setSkewCenter(0, 0);
        ground.setZIndex(998);
        attachChild(ground);

        Body ceillingBody = PhysicsFactory.createBoxBody(
                physicWorld, bigNumber / 2, 800 - 150, bigNumber, 55, BodyType.StaticBody, Pillar.CEILLING_FIXTURE);
        ceillingBody.setUserData(Pillar.BODY_FLOOR);
    }

    private void createActor() {
        playerY = ground.getY();
        player = new TiledSprite(150, 750 - 150, 90, 75, resourceManager.playerRegion, vbom);
        player.setCurrentTileIndex(0);
        player.setSkewCenter(0, 0);
        player.setZIndex(999);
        player.registerUpdateHandler(new IUpdateHandler() {

            @Override
            public void onUpdate(float pSecondsElapsed) {
                if (deadState.equals("dead")) {
                    player.setCurrentTileIndex(3);
                } else if (state == STATE.PLAY) {
                    if (playerBody.getLinearVelocity().y > -0.01f) {
                        if (jumpControl == 0) {
                            if (controler <= 5) {
                                player.setCurrentTileIndex(0);
                                controler++;
                            } else if (controler >= 6 && controler <= 11) {
                                player.setCurrentTileIndex(1);
                                controler++;
                            } else {
                                player.setCurrentTileIndex(2);
                                controler++;
                                controler = controler == 17 ? 0 : controler;
                            }
                        } else {
                            player.setCurrentTileIndex(2);
                        }

                    } else {
                        player.setCurrentTileIndex(1);
                    }
                }
            }
            @Override
            public void reset() { }
        });
        playerBody = PhysicsFactory.createCircleBody(physicWorld, 150, 750 - 150, 30, BodyType.DynamicBody, Pillar.PLAYER_FIXTURE);
        playerBody.setFixedRotation(true);
        playerBody.setUserData(Pillar.BODY_ACTOR);
        physicWorld.registerPhysicsConnector(new PhysicsConnector(player, playerBody));
        attachChild(player);
    }

    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {

        super.onManagedUpdate(pSecondsElapsed);

        if (state == STATE.PLAY) {
            Vector2 v = playerBody.getLinearVelocity();
            v.x = Pillar.SPEED_X;
            playerBody.setLinearVelocity(v);
            if (controler == 9) {
                resourceManager.runSound.play();
            }
        }

        if (scored) {
            addPillar();
            sortChildren();
            scored = false;
        }

        // if first pillar is out of the screen, delete it
        if (!pillars.isEmpty()) {
            Pillar fp = pillars.getFirst();
            if (fp.getX() + fp.getWidth() < resourceManager.camera.getXMin()) {
                PillarFactory.getInstance().recycle(fp);
                pillars.remove();
            }
        }

        if (state == STATE.DEAD && timestamp + TIME_TO_RESSURECTION < System.currentTimeMillis()) {
            state = STATE.AFTERLIFE;

            if (!resourceManager.activity.shared) {
                resourceManager.activity.shared = true;
                resourceManager.activity.showScore();
            }
        }
    }

    private void createHUD() {
        gameHUD = new HUD();
        scoreText = new Text(20,  100, resourceManager.font, SCORE + "0123456789", 20, vbom);
        scoreText.setSkewCenter(1, 1);
        scoreText.setText(SCORE + "0");

        tapToPlayText = new Text(GoGoJohnny.CAMERA_WIDTH * 0.10f, scoreText.getY() + 50, resourceManager.font, TAP_TO_PLAY, new TextOptions(HorizontalAlign.LEFT), vbom);
        tapToPlayText.setSkewCenter(0, 0);
        tapToPlayText.setScale(0.5f);
        tapToPlayText.setText(TAP_TO_PLAY);

        tapToPlayAgainText = new Text(tapToPlayText.getX() - 50f, scoreText.getY() + 50, resourceManager.font, TAP_TO_PLAY_AGAIN, new TextOptions(HorizontalAlign.LEFT), vbom);
        tapToPlayAgainText.setSkewCenter(0, 0);
        tapToPlayAgainText.setScale(0.5f);
        tapToPlayAgainText.setText(TAP_TO_PLAY_AGAIN);

        parallaxTapBackLayerSprite = new Sprite(0, 650, resourceManager.tapRegion, vbom);
        parallaxTapBackLayerSprite.setSkewCenter(0, 0);
//        parallaxTapBackLayerSprite.setZIndex(999);

        gameHUD.attachChild(tapToPlayText);
        gameHUD.attachChild(tapToPlayAgainText);
        gameHUD.attachChild(scoreText);
        gameHUD.attachChild(parallaxTapBackLayerSprite);

        camera.setHUD(gameHUD);

        tapToPlayAgainText.setVisible(false);
    }

    @Override
    public void reset() {
        super.reset();

        rotationControler = 0;
        rotationState = "";
        itensController = 0;

        physicWorld.setGravity(new Vector2(0, 0));

        Iterator<Pillar> pi = pillars.iterator();
        while (pi.hasNext()) {
            Pillar p = pi.next();
            PillarFactory.getInstance().recycle(p);
            pi.remove();
        }

        PillarFactory.getInstance().reset();

        playerBody.setTransform(150 / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,
                (745 - 150) / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 0);

        addPillar();
        addPillar();
        addPillar();

        score = 0;

        tapToPlayText.setText(TAP_TO_PLAY);
        tapToPlayText.setVisible(true);

        tapToPlayAgainText.setVisible(false);

        sortChildren();

        unregisterUpdateHandler(physicWorld);
        physicWorld.onUpdate(0);

        state = STATE.NEW;
    }

    private void addPillar() {
        Pillar p = PillarFactory.getInstance().next();
        pillars.add(p);
        attachIfNotAttached(p);
    }

    private void attachIfNotAttached(Pillar p) {
        if (!p.hasParent()) {
            attachChild(p);
        }
    }

    @Override
    public void onBackKeyPressed() {
        gameHUD.setVisible(false);
        camera.clearUpdateHandlers();
        SceneManager.getInstance().loadMenuScene(engine);
    }

    @Override
    public SceneType getSceneType() {
        return SceneType.SCENE_GAME;
    }

    @Override
    public void disposeScene() {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
        if (pSceneTouchEvent.isActionDown()) {
            if (state == STATE.PAUSED) {
                if (lastState != STATE.NEW) {
                    registerUpdateHandler(physicWorld);
                }
                state = lastState;
            } else if (state == STATE.NEW) {
                reset();
                rotationState = "go";
                resourceManager.activity.shared = false;
                registerUpdateHandler(physicWorld);
                state = STATE.PLAY;
                physicWorld.setGravity(new Vector2(0, Pillar.GRAVITY));
//                playerBody.setLinearVelocity(new Vector2(Pillar.SPEED_X, 0));
                scoreText.setText(SCORE + "0");
                tapToPlayText.setVisible(false);
            } else if (state == STATE.DEAD) {
                // don't touch the dead!
            } else if (state == STATE.AFTERLIFE) {
                deadState = "no";
                reset();
                player.setCurrentTileIndex(0);
                state = STATE.NEW;
            } else {
                if (jumpControl < 3) {
                    controler = 0;
                    resourceManager.jumpSound.play();
                    Vector2 v = playerBody.getLinearVelocity();
                    v.x = Pillar.SPEED_X;
                    v.y = Pillar.SPEED_Y;
                    playerBody.setLinearVelocity(v);
                }
                jumpControl++;
            }
        }
        return false;
    }

    @Override
    public void beginContact(Contact contact) {
        if (Pillar.BODY_FLOOR.equals(contact.getFixtureA().getBody().getUserData()) ||
                Pillar.BODY_FLOOR.equals(contact.getFixtureB().getBody().getUserData())) {
            jumpControl = 0;
        }

        if (Pillar.BODY_ITEM_SENSOR.equals(contact.getFixtureA().getBody().getUserData()) ||
                Pillar.BODY_ITEM_SENSOR.equals(contact.getFixtureB().getBody().getUserData())) {
            resourceManager.scoreSound.play();
            PillarFactory.getInstance().itensList.get(itensController).setVisible(false);
            itensController++;
            score++;
            scoreText.setText(SCORE + String.valueOf(score));
            resourceManager.activity.score = score;
        }

        if (Pillar.BODY_WALL.equals(contact.getFixtureA().getBody().getUserData()) ||
                Pillar.BODY_WALL.equals(contact.getFixtureB().getBody().getUserData())) {
            state = STATE.DEAD;
            deadState = "dead";
            rotationState = "";
            jumpControl = 0;
            itensController = 0;

            resourceManager.activity.score = score;
            resourceManager.activity.setHighScore(score);

            timestamp = System.currentTimeMillis();
            // play sound die
            resourceManager.dieSound.play();
            playerBody.setLinearVelocity(0, 0);
            for (Pillar p : pillars) {
                p.getPillarUpBody().setActive(false);
                p.getPillarDownBody().setActive(false);
            }

            tapToPlayAgainText.setVisible(true);
        }
    }

    @Override
    public void endContact(Contact contact) {
        if (Pillar.BODY_SENSOR.equals(contact.getFixtureA().getBody().getUserData()) ||
                Pillar.BODY_SENSOR.equals(contact.getFixtureB().getBody().getUserData())) {
            if (state != STATE.DEAD) {
                resourceManager.scoreSound.play();
                scored = true;
                score++;
                scoreText.setText(SCORE + String.valueOf(score));
            }
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        // TODO Auto-generated method stub
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        // TODO Auto-generated method stub
    }

    public void resume() {
    }

    public void pause() {
    }
}
