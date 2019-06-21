package myPlayer.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static myPlayer.PlayerData.myPlayer;

public class MuteAction implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        myPlayer().mediaPlayerComponent().mediaPlayer().audio().mute();
    }
}
