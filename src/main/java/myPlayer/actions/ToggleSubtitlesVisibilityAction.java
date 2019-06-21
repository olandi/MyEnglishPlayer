package myPlayer.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static myPlayer.PlayerData.myPlayer;

public class ToggleSubtitlesVisibilityAction implements ActionListener {

    private int currentTrack;

    @Override
    public void actionPerformed(ActionEvent e) {

        if (myPlayer().mediaPlayerComponent().mediaPlayer().subpictures().track() != -1) {
            currentTrack = myPlayer().mediaPlayerComponent().mediaPlayer().subpictures().track();
            myPlayer().mediaPlayerComponent().mediaPlayer().subpictures().setTrack(-1);
        } else {
            myPlayer().mediaPlayerComponent().mediaPlayer().subpictures().setTrack(currentTrack);
        }

    }
}
