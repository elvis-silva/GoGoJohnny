package com.elvis.gogojohnny;

import java.io.IOException;

import org.andengine.engine.Engine;
import org.andengine.engine.LimitedFPSEngine;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.view.RenderSurfaceView;
import org.andengine.ui.activity.BaseGameActivity;
import org.brickred.socialauth.android.SocialAuthAdapter;
import org.brickred.socialauth.android.SocialAuthAdapter.Provider;

import com.elvis.gogojohnny.camera.FollowCamera;
import com.elvis.gogojohnny.data.DataHandler;
import com.elvis.gogojohnny.manager.ResourceManager;
import com.elvis.gogojohnny.manager.SceneManager;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class GoGoJohnny extends BaseGameActivity {

    public static final float CAMERA_WIDTH = 480;
    public static final float CAMERA_HEIGHT = 800;
    private FollowCamera camera;
    private int highScore = 0;
    public int score = 0;
    private DataHandler dataHandler;
    public SocialAuthAdapter adapter;
    public boolean shared = false;
    public AdView adView;
    public InterstitialAd interstitialAd;
    private int interstitialAdIndex = 0;
    public FrameLayout frameLayout;

    SharedPreferences prefs;

    public void setHighScore(int score) {
        if (score > getHighScore()) {
            updateHighScore(score);
        }
    }

    public int getHighScore() {
        dataHandler = new DataHandler(this);
        dataHandler.open();
        Cursor cursor = dataHandler.returnData();
        if(cursor.moveToFirst()) {
            if (cursor.moveToLast()) {
                highScore = cursor.getInt(0);
            } do {
                highScore = cursor.getInt(0);
            } while (cursor.moveToNext());
        }
        dataHandler.close();
        return highScore;
    }

    @Override
    public Engine onCreateEngine(EngineOptions pEngineOptions) {
        return new LimitedFPSEngine(pEngineOptions, 60);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            SceneManager.getInstance().getCurrentScene().onBackKeyPressed();
        }
        return false;
    }

    @Override
    public EngineOptions onCreateEngineOptions() {
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        this.camera = new FollowCamera(0, 0, GoGoJohnny.CAMERA_WIDTH, GoGoJohnny.CAMERA_HEIGHT);
        EngineOptions engineOption = new EngineOptions(true, ScreenOrientation.PORTRAIT_SENSOR, new FillResolutionPolicy(), this.camera);
        engineOption.getAudioOptions().setNeedsMusic(true);
        engineOption.getAudioOptions().setNeedsSound(true);
        engineOption.getRenderOptions().setMultiSampling(true);
        engineOption.setWakeLockOptions(WakeLockOptions.SCREEN_ON);
        return engineOption;
    }

    @Override
    public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback) throws IOException {
        ResourceManager.prepareManager(mEngine, this, camera, getVertexBufferObjectManager());
        pOnCreateResourcesCallback.onCreateResourcesFinished();
    }

    @Override
    public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) throws IOException {
        SceneManager.getInstance().createSplashScene(pOnCreateSceneCallback);
    }

    @Override
    public void onPopulateScene(Scene pScene,
                                OnPopulateSceneCallback pOnPopulateSceneCallback)
            throws IOException {
        mEngine.registerUpdateHandler(new TimerHandler(2f, new ITimerCallback() {

            @Override
            public void onTimePassed(TimerHandler pTimerHandler) {
                mEngine.unregisterUpdateHandler(pTimerHandler);
                SceneManager.getInstance().createMenuScene();
            }
        }));
        pOnPopulateSceneCallback.onPopulateSceneFinished();
    }

    @Override
    public synchronized void onResumeGame() {
        super.onResumeGame();
        if (SceneManager.getInstance().getCurrentScene() == SceneManager.getInstance().gameScene) {
            SceneManager.getInstance().gameScene.resume();
        }
    }

    @Override
    protected synchronized void onResume() {
        super.onResume();
        if (adView != null) {
            adView.resume();
        }
    }

    @Override
    protected void onPause() {
        if (adView != null) {
            adView.pause();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy () {
        if (adView != null) {
            adView.destroy();
        }
        System.exit(0);
        super.onDestroy();
    }

    @Override
    protected void onSetContentView() {
        super.onSetContentView();

        frameLayout = new FrameLayout(GoGoJohnny.this);

        final FrameLayout.LayoutParams frameLayoutLayoutParams =
                new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT);

        final RelativeLayout.LayoutParams surfaceViewLayoutParams =
                new RelativeLayout.LayoutParams(BaseGameActivity.createSurfaceViewLayoutParams());

        adView = new AdView(GoGoJohnny.this);
        adView.refreshDrawableState();

        int screenSize = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;

        switch(screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                adView.setAdSize(AdSize.FULL_BANNER);
                break;
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                adView.setAdSize(AdSize.FULL_BANNER);
                break;
            default:
                adView.setAdSize(AdSize.BANNER);
        }

        adView.setAdUnitId("ca-app-pub-4768510961285493/1091159566");

        adView.setVisibility(View.VISIBLE);
        adView.setEnabled(true);

        AdRequest adRequest = new AdRequest
                .Builder()
                .build();
        adView.loadAd(adRequest);

        this.mRenderSurfaceView = new RenderSurfaceView(GoGoJohnny.this);
        this.mRenderSurfaceView.setRenderer(this.mEngine, GoGoJohnny.this);

        final FrameLayout.LayoutParams adViewLayoutParams =
                new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT);

        frameLayout.addView(this.mRenderSurfaceView, surfaceViewLayoutParams);
        frameLayout.addView(adView);

        adView.setAdListener(new AdListener() {
            public void onAdLoaded(){
                adView.setLayoutParams(adViewLayoutParams);
            }
        });

        this.setContentView(frameLayout, frameLayoutLayoutParams);
    }

    public void showInterstialAd () {
        if (interstitialAdIndex == 20) {
            interstitialAd.show();
            interstitialAdIndex = 0;
        }
    }

    public static RelativeLayout.LayoutParams getSurfaceViewLayoutParams () {
        return new RelativeLayout.LayoutParams(BaseGameActivity.createSurfaceViewLayoutParams());
    }

    public void updateHighScore (int newHighScore) {
        dataHandler = new DataHandler(this);
        dataHandler.open();
        dataHandler.deleteData();
        long highScoreLong = dataHandler.insertData(newHighScore);
        dataHandler.close();
    }

    public void showScore(){
        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                if (interstitialAdIndex == 0) loadInterstitial();
                interstitialAdIndex++;

                final Dialog dialog = new Dialog(GoGoJohnny.this);
                dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_score);
                LinearLayout.LayoutParams btnLayoutParams = new LinearLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                btnLayoutParams.weight = 1f;

                FrameLayout.LayoutParams tableRowtLayoutParams = new FrameLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                final TableLayout tableLayout = new TableLayout(GoGoJohnny.this);
                tableLayout.setLayoutParams(new FrameLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
                TableRow tableRow = new TableRow(GoGoJohnny.this);
                tableLayout.addView(tableRow, tableRowtLayoutParams);
                tableRow.setGravity(Gravity.CENTER_HORIZONTAL);
                final TextView lblScore = new TextView(GoGoJohnny.this);
                tableRow.addView(lblScore);
                lblScore.setTextColor(Color.rgb(20, 20, 200));
                lblScore.setTextSize(30f);
                Typeface font = Typeface.createFromAsset(getAssets(),"font/font.ttf");
                lblScore.setTypeface(font);

                lblScore.setText("Your score:  " + String.valueOf(ResourceManager.getInstance().activity.score));

                tableRow = new TableRow(GoGoJohnny.this);
                tableLayout.addView(tableRow, tableRowtLayoutParams);
                tableRow.setGravity(Gravity.CENTER_HORIZONTAL);
                final TextView lblHighScore = new TextView(GoGoJohnny.this);
                tableRow.addView(lblHighScore);
                lblHighScore.setTextColor(Color.rgb(200, 20, 20));
                lblHighScore.setTextSize(30f);
                Typeface hfont = Typeface.createFromAsset(getAssets(),"font/font.ttf");
                lblHighScore.setTypeface(hfont);

                lblHighScore.setText("High score:  " + String.valueOf(ResourceManager.getInstance().activity.getHighScore()));

                tableRow = new TableRow(GoGoJohnny.this);
                tableLayout.addView(tableRow, tableRowtLayoutParams);
                final TextView tv = new TextView(GoGoJohnny.this);
                tv.setTextSize(20f);
                tv.setText("");
                tableRow.addView(tv);
                tableRow = new TableRow(GoGoJohnny.this);
                tableLayout.addView(tableRow, tableRowtLayoutParams);
                tableRow.setGravity(Gravity.CENTER_HORIZONTAL);

                final Button shareBtn = new Button(GoGoJohnny.this);
                shareBtn.setTextSize(20f);
                shareBtn.setTextColor(Color.rgb(20, 20, 200));
                shareBtn.setText("Share");

                adapter = new SocialAuthAdapter(new ResponseListener());
                adapter.addProvider(Provider.FACEBOOK, R.drawable.facebook);
                adapter.enable(shareBtn);

                final Button exitBtn = new Button(GoGoJohnny.this);
                exitBtn.setTextSize(20f);
                exitBtn.setTextColor(Color.rgb(20, 20, 200));
                exitBtn.setText("Try again");

                tableRow.addView(exitBtn);
                tableRow.addView(shareBtn);

                exitBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mEngine.start();
                        dialog.dismiss();
                    }
                });

                dialog.setContentView(tableLayout);

                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        mEngine.start();
                        showInterstialAd();
                        dialog.dismiss();
                    }
                });

                mEngine.stop();
                dialog.show();

            }
        });
    }

    private void loadInterstitial () {
        interstitialAd = new InterstitialAd(GoGoJohnny.this);
        interstitialAd.setAdUnitId("ca-app-pub-4768510961285493/2567892766");

        AdRequest adRequest = new AdRequest
                .Builder()
                .build();

        interstitialAd.loadAd(adRequest);
    }
}