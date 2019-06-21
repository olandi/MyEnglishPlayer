package myPlayer.actions;

import javax.swing.*;
import java.awt.event.ActionEvent;

import static myPlayer.PlayerData.myPlayer;

public class PreviousSubtitleAction extends AbstractAction {
    @Override
    public void actionPerformed(ActionEvent e) {
        if (myPlayer().areSubtitlesActivated()) {
            long position = myPlayer().getSubtitleNavigation().getPreviousSubtitle(myPlayer().mediaPlayerComponent().mediaPlayer().status().time());
            myPlayer().mediaPlayerComponent().mediaPlayer().controls().setTime(position);
        }
    }
}
