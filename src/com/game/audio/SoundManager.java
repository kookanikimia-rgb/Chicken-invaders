package com.game.audio;

import com.game.database.DatabaseManager;
import com.game.entities.UserSession;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import java.net.URL;

public class SoundManager {

    private static Clip backgroundClip;

    private static boolean backgroundMusicEnabled = true;
    private static boolean shotSoundEnabled = true;
    private static boolean crashSoundEnabled = true;
    private static boolean gameOverSoundEnabled = true;

    public static void loadSettings(String username) {

        if (username == null)
            return;

        backgroundMusicEnabled =
                DatabaseManager.getSoundSetting(username, "bg_music");

        shotSoundEnabled =
                DatabaseManager.getSoundSetting(username, "shot_sound");

        crashSoundEnabled =
                DatabaseManager.getSoundSetting(username, "crash_sound");

        gameOverSoundEnabled =
                DatabaseManager.getSoundSetting(username, "game_over_sound");
    }

    private static void play(String path) {

            URL url = SoundManager.class.getResource(path);

            if (url == null) {
                System.out.println(path + " not found");
                return;
            }

            try (AudioInputStream audio = AudioSystem.getAudioInputStream(url)){

                Clip clip = AudioSystem.getClip();

                clip.addLineListener(event -> {
                    if (event.getType() == LineEvent.Type.STOP) {
                        clip.close();
                    }
                });

                clip.open(audio);
                clip.start();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void startBackgroundMusic() {

        if (!backgroundMusicEnabled)
            return;

        if (backgroundClip != null) {
            if (!backgroundClip.isRunning()) {
                backgroundClip.setFramePosition(0);
                backgroundClip.loop(Clip.LOOP_CONTINUOUSLY);
                backgroundClip.start();
            }
            return;
        }

        try {

            URL url = SoundManager.class.getResource("/Assets/sounds/sound-effects/background.wav");

            AudioInputStream audio = AudioSystem.getAudioInputStream(url);

            backgroundClip = AudioSystem.getClip();
            backgroundClip.open(audio);
            backgroundClip.setFramePosition(0);
            backgroundClip.loop(Clip.LOOP_CONTINUOUSLY);
            backgroundClip.start();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void stopBackgroundMusic() {

        if (backgroundClip != null) {

            backgroundClip.stop();
            backgroundClip.close();
            backgroundClip = null;

        }

    }

    public static void playShot() {

        if (shotSoundEnabled)
            play("/Assets/sounds/sound-effects/gun-shot.wav");

    }

    public static void playExplosion() {

        if (crashSoundEnabled)
            play("/Assets/sounds/sound-effects/afar-explosion.wav");

    }

    public static void playGameOver() {
        stopBackgroundMusic();

        if (gameOverSoundEnabled)
            play("/Assets/sounds/sound-effects/game-over.wav");

    }

    public static void playWin() {
        stopBackgroundMusic();

        if (gameOverSoundEnabled)
            play("/Assets/sounds/sound-effects/win.wav");

    }

}