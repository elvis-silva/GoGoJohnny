package com.elvis.gogojohnny.object;

import java.util.LinkedList;
import java.util.Random;

import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.util.adt.pool.GenericPool;

import com.elvis.gogojohnny.manager.ResourceManager;

public class PillarFactory {
    private static final PillarFactory INSTANCE = new PillarFactory();

    public LinkedList<Sprite> itensList;

    private PillarFactory() {
    }

    GenericPool<Pillar> pool;

    int nextX;
    int nextY;
    int dy;
    int itemValue;

    int dx = 480;

    final int maxY = 650 - 150;
    final int minY = 450 - 150;

    private Random randomVal;

    public static final PillarFactory getInstance() {
        return INSTANCE;
    }

    public void create(final PhysicsWorld physics) {
        reset();
        pool = new GenericPool<Pillar>(5) {

            @Override
            protected Pillar onAllocatePoolItem() {
                Pillar p = new Pillar(0, 0,
                        ResourceManager.getInstance().pillarRegion,
                        ResourceManager.getInstance().pillarRegionUp,
                        ResourceManager.getInstance().vbom,
                        physics);
                p.setZIndex(997);
                return p;
            }
        };
    }

    public Pillar next() {
        Pillar p = pool.obtainPoolItem();
        p.setPosition(nextX, nextY);

        p.getPillarUpBody().setTransform(nextX / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,
                (nextY + p.getPillarShift()) / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 0);

        p.getPillarDownBody().setTransform(nextX / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,
                (nextY - p.getPillarShift()) / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 0);

        p.getScoreSensor().setTransform(nextX / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,
                nextY / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 0);

        p.getCarrotSensor().setTransform(nextX / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,
                nextY / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 0);

        p.getAppleSensor().setTransform(nextX / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,
                nextY / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 0);

        p.getEggSensor().setTransform(nextX / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,
                nextY / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 0);

        p.getPillarUpBody().setActive(true);
        p.getPillarDownBody().setActive(true);
        p.getScoreSensor().setActive(true);

        randomVal = new Random();

        nextX += dx;

        dy = randomVal.nextInt(maxY - minY) + minY;
        dy = dy > maxY || dy < minY ? maxY : dy;
        nextY = dy;

        randomVal = new Random();
        itemValue = randomVal.nextInt(4);
        p.getCarrot().setVisible(itemValue == 1);
        p.getCarrotSensor().setActive(itemValue == 1);
        if (p.getCarrot().isVisible()) itensList.add(p.getCarrot());
        p.getApple().setVisible(itemValue == 2);
        p.getAppleSensor().setActive(itemValue == 2);
        if (p.getApple().isVisible()) itensList.add(p.getApple());
        p.getEgg().setVisible(itemValue == 3);
        p.getEggSensor().setActive(itemValue == 3);
        if (p.getEgg().isVisible()) itensList.add(p.getEgg());

        return p;
    }

    public void recycle(Pillar p) {
        p.detachSelf();
        p.getPillarUpBody().setActive(false);
        p.getPillarDownBody().setActive(false);
        p.getScoreSensor().setActive(false);
        p.getCarrotSensor().setActive(false);
        p.getAppleSensor().setActive(false);
        p.getEggSensor().setActive(false);
        p.getPillarUpBody().setTransform(-1000, -1000, 0);
        p.getPillarDownBody().setTransform(-1000, -1000, 0);
        p.getScoreSensor().setTransform(-1000, -1000, 0);
        p.getCarrotSensor().setTransform(-1000, -1000, 0);
        p.getAppleSensor().setTransform(-1000, -1000, 0);
        p.getEggSensor().setTransform(-1000, -1000, 0);
        pool.recyclePoolItem(p);
    }

    public void reset() {
        itensList = new LinkedList<Sprite>();

        randomVal = new Random();

        nextX = 1500;

        dy = randomVal.nextInt(maxY - minY) + minY;
        dy = dy > maxY || dy < minY ? maxY : dy;
        nextY = dy;

        dy = randomVal.nextInt(maxY - minY) + minY;
        dy = dy > maxY || dy < minY ? maxY : dy;
    }
}
