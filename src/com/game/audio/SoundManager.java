package com.game.audio;

import com.game.database.DatabaseManager;
import com.game.entities.UserSession;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;

public class SoundManager {

    private static Clip backgroundClip;
    private static Clip effectClip;

    private static boolean isEnabled(String key) {

        String username = UserSession.getUserName();

        if (username == null)
            return true;

        return DatabaseManager.getSoundSetting(username, key);
    }

    private static void play(String path) {

        try {

            URL url = SoundManager.class.getResource(path);

            if (url == null) {
                System.out.println(path + " not found");
                return;
            }

            AudioInputStream audio =
                    AudioSystem.getAudioInputStream(url);

            effectClip = AudioSystem.getClip();
            effectClip.open(audio);
            effectClip.start();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void startBackgroundMusic() {

        if (!isEnabled("bg_music"))
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

        if (isEnabled("shot_sound"))
            play("/Assets/sounds/sound-effects/gun-shot.wav");

    }

    public static void playExplosion() {

        if (isEnabled("crash_sound"))
            play("/Assets/sounds/sound-effects/afar-explosion.wav");

    }

    public static void playGameOver() {
        stopBackgroundMusic();

        if (isEnabled("game_over_sound"))
            play("/Assets/sounds/sound-effects/game-over.wav");

    }

    public static void playWin() {
        stopBackgroundMusic();

        if (isEnabled("game_over_sound"))
            play("/Assets/sounds/sound-effects/win.wav");

    }
    public static void stopEffectSound() {

        if (effectClip != null) {
            effectClip.stop();
            effectClip.close();
            effectClip = null;
        }

    }
}