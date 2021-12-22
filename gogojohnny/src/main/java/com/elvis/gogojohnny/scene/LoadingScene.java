package com.elvis.gogojohnny.scene;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;
import org.andengine.util.color.Color;

import com.elvis.gogojohnny.GoGoJohnny;
import com.elvis.gogojohnny.manager.SceneManager.SceneType;

public class LoadingScene extends BaseScene {
	
    @Override
    public void createScene() {
        setBackground(new Background(Color.WHITE));
        attachChild(new Text(GoGoJohnny.CAMERA_WIDTH/2, GoGoJohnny.CAMERA_HEIGHT/2, resourceManager.font, "Loading...", vbom));
    }

    @Override
    public void onBackKeyPressed()
    {
        return;
    }

    @Override
    public SceneType getSceneType()
    {
        return SceneType.SCENE_LOADING;
    }

    @Override
    public void disposeScene() {
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