package com.elvis.gogojohnny.scene;

import android.content.Intent;
import android.net.Uri;

import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.sprite.Sprite;

import com.elvis.gogojohnny.GoGoJohnny;
import com.elvis.gogojohnny.manager.ResourceManager;
import com.elvis.gogojohnny.manager.SceneManager;
import com.elvis.gogojohnny.manager.SceneManager.SceneType;

public class MainMenuScene extends BaseScene implements IOnMenuItemClickListener {

    private static final int MENU_PLAY = 0;
    private static final int FGTS_CORRIGIDO = 1;
    private static final int FLIGHT_DRONE = 2;
    private MenuScene menuChild;
    private Sprite menubackGround;

    @Override
    public void createScene() {
        createBackground();
        createMenuChildScene();
    }

    private void createMenuChildScene() {
        menuChild = new MenuScene(camera);
        menuChild.setPosition(0, 0);
        final IMenuItem playGameBtn = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_PLAY, resourceManager.playGameBtn, vbom), 1.2f, 1f);
        menuChild.addMenuItem(playGameBtn);
        final IMenuItem fgtsCorrigidoBtn = new ScaleMenuItemDecorator(new SpriteMenuItem(FGTS_CORRIGIDO, resourceManager.fgtsCorrigidoBtn, vbom), 1.2f, 1f);
        menuChild.addMenuItem(fgtsCorrigidoBtn);
        final IMenuItem flightDroneBtn = new ScaleMenuItemDecorator(new SpriteMenuItem(FLIGHT_DRONE, resourceManager.flightDroneBtn, vbom), 1.2f, 1f);
        menuChild.addMenuItem(flightDroneBtn);
        menuChild.buildAnimations();
        menuChild.setBackgroundEnabled(true);
        SpriteBackground sBg = new SpriteBackground(menubackGround);
        menuChild.setBackground(sBg);
        fgtsCorrigidoBtn.setPosition(GoGoJohnny.CAMERA_WIDTH / 2 - (fgtsCorrigidoBtn.getWidth() + 48),
                GoGoJohnny.CAMERA_HEIGHT / 2);
        flightDroneBtn.setPosition(GoGoJohnny.CAMERA_WIDTH / 2 + 48,
                GoGoJohnny.CAMERA_HEIGHT / 2);
        playGameBtn.setPosition(GoGoJohnny.CAMERA_WIDTH / 2 - playGameBtn.getWidth() / 2,
        		fgtsCorrigidoBtn.getY() + fgtsCorrigidoBtn.getHeight() * 2);
        menuChild.setOnMenuItemClickListener(this);
        setChildScene(menuChild);
    }

    private void createBackground() {
        menubackGround = new Sprite(0, 0, 500, 900, resourceManager.menuBackgroundRegion, vbom);
    }

    @Override
    public void onBackKeyPressed() {
        System.exit(0);
    }

    @Override
    public SceneType getSceneType() {
        return SceneManager.SceneType.SCENE_MENU;
    }

    @Override
    public void disposeScene() {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean onMenuItemClicked(
            org.andengine.entity.scene.menu.MenuScene pMenuScene,
            IMenuItem pMenuItem, float pMenuItemLocalX, float pMenuItemLocalY) {
        Intent intent;
        switch (pMenuItem.getID()) {
            case MENU_PLAY:
                ResourceManager.getInstance().playSound.play();
                SceneManager.getInstance().loadGameScene(engine);
                return true;
            case FGTS_CORRIGIDO:
                ResourceManager.getInstance().playSound.play();
                Uri uriFGTS = Uri.parse("https://play.google.com/store/apps/details?id=com.elvis.fgtscorrigido.app");
                intent = new Intent(Intent.ACTION_VIEW, uriFGTS);
                ResourceManager.getInstance().activity.startActivity(intent);
                return true;
            case FLIGHT_DRONE:
                ResourceManager.getInstance().playSound.play();
                Uri uriFlightDrone = Uri.parse("https://play.google.com/store/apps/details?id=com.elvis.flightdrone");
                intent = new Intent(Intent.ACTION_VIEW, uriFlightDrone);
                ResourceManager.getInstance().activity.startActivity(intent);
                return true;
            default:
                return false;
        }
    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub
    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub
    }
}
