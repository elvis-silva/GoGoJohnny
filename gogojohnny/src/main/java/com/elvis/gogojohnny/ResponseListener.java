package com.elvis.gogojohnny;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.elvis.gogojohnny.manager.ResourceManager;

import org.brickred.socialauth.Profile;
import org.brickred.socialauth.android.DialogListener;
import org.brickred.socialauth.android.SocialAuthAdapter;
import org.brickred.socialauth.android.SocialAuthError;
import org.brickred.socialauth.android.SocialAuthListener;

import java.io.UnsupportedEncodingException;

public final class ResponseListener implements DialogListener {

    public void onComplete(Bundle values) {

        SocialAuthAdapter adapter = ResourceManager.getInstance().activity.adapter;

        String userName = adapter.getUserProfile().getFullName();
        String userLanguage = adapter.getUserProfile().getLanguage();
        String message = userLanguage.equals("pt") ? " marcou muitos pontos." : " scored many points.";
        String name = userLanguage.equals("pt") ? "TENTE QUEBRAR ESSE RECORDE AGORA!" : "TRY TO BREAK THIS RECORD NOW!";
        String caption = userLanguage.equals("pt") ? "Instale GO!GO! JOHNNY no seu android agora mesmo" : "Install GO!GO! JOHNNY on your android now.";
        String descriptionPart1 = userLanguage.equals("pt") ? "Eu fiz " : "I made ";
        String descriptionPart2 = userLanguage.equals("pt") ? " pontos no jogo GO!GO! JOHNNY. Agora quero ver se alguém vai quebrar meu recorde" : " points in the GO!GO! JOHNNY game. I want to see if anyone can break my record.";
        String gameLink = "https://play.google.com/store/apps/details?id=com.elvis.gogojohnny";
        String gamePicture = "https://fbcdn-photos-h-a.akamaihd.net/hphotos-ak-frc3/t39.2081-0/p128x128/10173513_622008327886809_97317897_n.png";

        MessageListener ml = new MessageListener();
        String newRecord = String.valueOf(ResourceManager.getInstance().activity.score);
        try {
            ResourceManager.getInstance().activity.adapter.updateStory(userName + message, name, caption,
                    descriptionPart1 + newRecord + descriptionPart2, gameLink, gamePicture, ml);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private final class ProfileDataListener implements SocialAuthListener<Profile> {

        public String userName = "";

        @Override
        public void onExecute(String s, Profile profile) {
            Log.d("Custom-UI", "Receiving Data");
            Log.d("Custom-UI", "Validate ID         = " + profile.getValidatedId());
            Log.d("Custom-UI", "First Name          = " + profile.getFirstName());
            Log.d("Custom-UI", "Last Name           = " + profile.getLastName());
            Log.d("Custom-UI", "Email               = " + profile.getEmail());
            Log.d("Custom-UI", "Gender              = " + profile.getGender());
            Log.d("Custom-UI", "Country             = " + profile.getCountry());
            Log.d("Custom-UI", "Language            = " + profile.getLanguage());
            Log.d("Custom-UI", "Location            = " + profile.getLocation());
            Log.d("Custom-UI", "Profile Image URL   = " + profile.getProfileImageURL());

            userName = profile.getFirstName() + " " + profile.getLastName();
        }

        @Override
        public void onError(SocialAuthError socialAuthError) {

        }
    }

    // To get status of message after authentication
    private final class MessageListener implements SocialAuthListener<Integer> {

        @Override
        public void onExecute(String s, Integer integer) {
            Integer status = integer;
            String messagePosted = ResourceManager.getInstance().activity.adapter.getUserProfile().getLanguage().equals("pt") ? "Mensagem postada" : "Message posted";
            if (status.intValue() == 200 || status.intValue() == 201 || status.intValue() == 204)
                Toast.makeText(ResourceManager.getInstance().activity, messagePosted, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onError(SocialAuthError socialAuthError) {

        }
    }

    @Override
    public void onError(SocialAuthError socialAuthError) {
    }

    @Override
    public void onCancel() {
    }

    @Override
    public void onBack() {
    }
}