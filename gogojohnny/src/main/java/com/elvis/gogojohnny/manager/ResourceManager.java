package com.elvis.gogojohnny.manager;

import java.io.IOException;

import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.Engine;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.bitmap.BitmapTextureFormat;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.debug.Debug;

import com.elvis.gogojohnny.GoGoJohnny;
import com.elvis.gogojohnny.camera.FollowCamera;
import android.graphics.Color;

public class ResourceManager {
    private static final ResourceManager INSTANCE = new ResourceManager();
    public Engine engine;
    public GoGoJohnny activity;
    public FollowCamera camera;
    public VertexBufferObjectManager vbom;
    public Font font;

    public Sound jumpSound;
    public Sound dieSound;
    public Sound scoreSound;
    public Sound playSound;
    public Sound runSound;
    public static ResourceManager getInstance() {
        return INSTANCE;
    }

    public ITextureRegion splash_region;
    public ITextureRegion playGameBtn;
    public ITextureRegion fgtsCorrigidoBtn;
    public ITextureRegion flightDroneBtn;
    public ITextureRegion bgRegion;
    public ITextureRegion tapRegion;
    public ITextureRegion parallaxFrontLayerRegion;
    public ITiledTextureRegion playerRegion;
    public TextureRegion pillarRegion;
    public TextureRegion pillarRegionUp;
    public TextureRegion carrotRegion;
    public TextureRegion appleRegion;
    public TextureRegion eggRegion;
    public ITextureRegion menuBackgroundRegion;

    private BitmapTextureAtlas splashTextureAtlas;
    private BuildableBitmapTextureAtlas menuTextureAtlas;
    private BuildableBitmapTextureAtlas gameTextureAtlas;

    public static void prepareManager(Engine engine, GoGoJohnny activity, FollowCamera camera, VertexBufferObjectManager vbom) {
        getInstance().engine = engine;
        getInstance().activity = activity;
        getInstance().camera = camera;
        getInstance().vbom = vbom;
    }

    public void loadMenuResources() {
        loadMenuGraphic();
        loadMenuSound();
        loadMenuFonts();
    }

    private void loadMenuFonts() {
        FontFactory.setAssetBasePath("font/");
        final ITexture mainFontTexture = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        font = FontFactory.createStrokeFromAsset(activity.getFontManager(), mainFontTexture, activity.getAssets(), "font.ttf", 50, true, Color.WHITE, 2, Color.BLACK);
        font.load();
    }

    private void loadMenuSound() {
        // TODO Auto-generated method stub
    }

    public void loadGameSound() {
        try {
            jumpSound = SoundFactory.createSoundFromAsset(activity.getSoundManager(), activity, "sfx/jumpSound.wav");
            dieSound = SoundFactory.createSoundFromAsset(activity.getSoundManager(), activity, "sfx/deadSound.wav");
            scoreSound = SoundFactory.createSoundFromAsset(activity.getSoundManager(), activity, "sfx/rewardSound.wav");
            playSound = SoundFactory.createSoundFromAsset(activity.getSoundManager(), activity, "sfx/buttonSound.wav");
            runSound = SoundFactory.createSoundFromAsset(activity.getSoundManager(), activity, "sfx/runSound.wav");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadMenuGraphic() {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
        menuTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);
        fgtsCorrigidoBtn = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "fgts_corrigido_btn.png");
        flightDroneBtn = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "flight_drone_btn.png");
        playGameBtn = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "play_game_btn.png");
        menuBackgroundRegion =  BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "menu_bg.png");
        
        try
        {
            this.menuTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
            this.menuTextureAtlas.load();
        }
        catch (final TextureAtlasBuilderException e)
        {
            Debug.e(e);
        }

        loadGameSound();
    }

    public void loadGameResources() {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

        BitmapTextureAtlas repeatingGroundAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.REPEATING_BILINEAR_PREMULTIPLYALPHA);
        parallaxFrontLayerRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(repeatingGroundAtlas, activity, "game_bg.png", 0, 0);
        repeatingGroundAtlas.load();

        gameTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 2048, 2048, BitmapTextureFormat.RGBA_8888, TextureOptions.BILINEAR);
        this.gameTextureAtlas.addEmptyTextureAtlasSource(0, 0, 2048, 2048);
        bgRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "bg.png");
        tapRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "tap_screen.png");
        playerRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "player2.png", 4, 1);
        pillarRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "pillar_gray_down.png");
        pillarRegionUp = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "pillar_gray_up.png");
        carrotRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "carrot.png");
        appleRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "apple.png");
        eggRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "egg.png");
        try {
            this.gameTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
            this.gameTextureAtlas.load();
        } catch (final TextureAtlasBuilderException e) {
            Debug.e(e);
        }
    }

    public void loadSplashResources() {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
        splashTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 480, 800, TextureOptions.BILINEAR);
        splash_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(splashTextureAtlas, activity, "splash.png",0,0);
        splashTextureAtlas.load();
    }

    public void unloadSplashScreen() {
        splashTextureAtlas.unload();
        splash_region = null;
    }

    public void unloadMenuTexture() {
        menuTextureAtlas.unload();
    }

    public void loadMenuTextures() {
        menuTextureAtlas.load();
    }

    public void unloadGameTextures() { gameTextureAtlas.unload(); }
}