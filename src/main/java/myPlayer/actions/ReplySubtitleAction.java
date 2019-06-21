package myPlayer.actions;

import javax.swing.*;
import java.awt.event.ActionEvent;

import static myPlayer.PlayerData.myPlayer;

public class ReplySubtitleAction extends AbstractAction {
    @Override
    public void actionPerformed(ActionEvent e) {
        if (myPlayer().areSubtitlesActivated()) {
            long currentPosition = myPlayer().mediaPlayerComponent().mediaPlayer().status().time();
            long position = myPlayer().getSubtitleNavigation().getRepeatingSubtitle(currentPosition);
            myPlayer().mediaPlayerComponent().mediaPlayer().controls().setTime(position);
        }
    }
}
