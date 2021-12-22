package com.elvis.gogojohnny.scene;

import org.andengine.engine.Engine;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.elvis.gogojohnny.GoGoJohnny;
import com.elvis.gogojohnny.camera.FollowCamera;
import com.elvis.gogojohnny.manager.ResourceManager;
import com.elvis.gogojohnny.manager.SceneManager.SceneType;

public abstract class BaseScene extends Scene {

    // Variables
    protected Engine engine;
    protected GoGoJohnny activity;
    protected FollowCamera camera;
    protected VertexBufferObjectManager vbom;
    protected ResourceManager resourceManager;

    // Constructors
    public BaseScene() {
        this.resourceManager = ResourceManager.getInstance();
        this.activity = resourceManager.activity;
        this.camera = resourceManager.camera;
        this.vbom = resourceManager.vbom;
        this.engine = resourceManager.engine;
        createScene();
    }

    // Abstraction
    public abstract void createScene();
    public abstract void onBackKeyPressed();
    public abstract SceneType getSceneType();
    public abstract void disposeScene();
    public abstract void resume();
    public abstract void pause();
}
