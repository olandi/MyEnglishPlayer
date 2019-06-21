package myPlayer.actions;

import javax.swing.*;
import java.awt.event.ActionEvent;

import static myPlayer.PlayerData.myPlayer;

public class PlayPauseAction extends AbstractAction {
    @Override
    public void actionPerformed(ActionEvent e) {

        if (!myPlayer().mediaPlayerComponent().mediaPlayer().status().isPlaying()
        ) {
            myPlayer().mediaPlayerComponent().mediaPlayer().controls().play();

        } else {
            myPlayer().mediaPlayerComponent().mediaPlayer().controls().pause();

        }
    }
}
