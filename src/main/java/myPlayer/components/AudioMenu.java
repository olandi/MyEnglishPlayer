package myPlayer.components;

import uk.co.caprica.vlcj.player.base.TrackDescription;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import static myPlayer.PlayerData.myPlayer;

public class AudioMenu extends JMenu {
    public AudioMenu(String s) {
        super(s);

        addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {
                ButtonGroup buttonGroup = new ButtonGroup();
                // audioMenu.setEnabled(audioMenu.getItemCount() > 0);
                int selectedTrack = myPlayer().mediaPlayerComponent().mediaPlayer().audio().track();
                for (TrackDescription trackDescription : myPlayer().mediaPlayerComponent().mediaPlayer().audio().trackDescriptions()) {
                    JRadioButtonMenuItem menuItem = new JRadioButtonMenuItem(trackDescription.description());
                    //trackDescription.description()
                    menuItem.putClientProperty("track-description", trackDescription);
                    menuItem.addActionListener(a -> myPlayer().mediaPlayerComponent().mediaPlayer().audio().setTrack(trackDescription.id()));
                    buttonGroup.add(menuItem);
                    add(menuItem);
                    if (selectedTrack == trackDescription.id()) {
                        menuItem.setSelected(true);
                    }
                }
            }

            @Override
            public void menuDeselected(MenuEvent e) {
                removeAll();
            }

            @Override
            public void menuCanceled(MenuEvent e) {
            }
        });

    }
}
