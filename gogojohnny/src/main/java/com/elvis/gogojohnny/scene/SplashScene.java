package com.elvis.gogojohnny.scene;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.util.GLState;
import org.andengine.util.color.Color;

import com.elvis.gogojohnny.GoGoJohnny;
import com.elvis.gogojohnny.manager.SceneManager.SceneType;

public class SplashScene extends BaseScene {

    Sprite splash;

    @Override
    public void createScene() {

        this.getBackground().setColor(Color.WHITE);

        splash = new Sprite(0, 0, resourceManager.splash_region, vbom) {
            @Override
            protected void preDraw(GLState pGLState, Camera pCamera) {
                super.preDraw(pGLState, pCamera);
                pGLState.enableDither();
            }
        };
        splash.setPosition(GoGoJohnny.CAMERA_WIDTH / 2 - splash.getWidth() / 2, GoGoJohnny.CAMERA_HEIGHT / 2 - splash.getHeight() / 2);
        attachChild(splash);
  }

    @Override
    public void onBackKeyPressed() {
        // TODO Auto-generated method stub
    }

    @Override
    public SceneType getSceneType() {
        return SceneType.SCENE_SPLASH;
    }

    @Override
    public void disposeScene() {
        splash.detachSelf();
        splash.dispose();
        this.detachSelf();
        this.dispose();

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
