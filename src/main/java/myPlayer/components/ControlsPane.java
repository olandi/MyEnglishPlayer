package myPlayer.components;

import javax.swing.*;

import myPlayer.actions.*;
import net.miginfocom.swing.MigLayout;
import uk.co.caprica.vlcj.player.base.LibVlcConst;

import java.awt.event.ActionListener;

import static myPlayer.PlayerData.myPlayer;


public final class ControlsPane extends JPanel {
    private final Icon playIcon = newIcon("play");
    private final Icon pauseIcon = newIcon("pause");
    private final Icon stopIcon = newIcon("stop");
    private final Icon fullscreenIcon = newIcon("fullscreen");
    private final Icon volumeHighIcon = newIcon("volume-high");
    private final Icon volumeMutedIcon = newIcon("volume-muted");
    private final Icon previousSubIcon = newIcon("previousSubIcon");
    private final Icon replySubIcon = newIcon("replySubIcon");
    private final Icon nextSubIcon = newIcon("nextSubIcon");
    private final Icon showSubIcon = newIcon("showSub");
    private final JButton previousSubButton;
    private final JButton replySubButton;
    private final JButton nextSubButton;
    private final JButton showSubButton;
    private final JButton playPauseButton;
    private final JButton stopButton;
    private final JButton fullscreenButton;
    private final JButton muteButton;
    private final JSlider volumeSlider;
    private final ActionListener toggleSubtitlesVisibilityAction = new ToggleSubtitlesVisibilityAction();
    private final ActionListener playPauseAction = new PlayPauseAction();
    private final ActionListener stopAction = new StopAction();
    private final ActionListener previousSubtitleAction = new PreviousSubtitleAction();
    private final ActionListener nextSubtitleAction = new NextSubtitleAction();
    private final ActionListener replySubtitleAction = new ReplySubtitleAction();
    private final ActionListener muteAction = new MuteAction();

    public ControlsPane() {
        playPauseButton = createButton(playIcon, playPauseAction);
        stopButton = createButton(stopIcon, stopAction);
        muteButton = createButton(volumeHighIcon, muteAction);
        volumeSlider = new JSlider();
        volumeSlider.setMinimum(LibVlcConst.MIN_VOLUME);
        volumeSlider.setMaximum(LibVlcConst.MAX_VOLUME);
        volumeSlider.setFocusable(false);
        fullscreenButton = createButton(fullscreenIcon, null);
        previousSubButton = createButton(previousSubIcon, previousSubtitleAction);
        nextSubButton = createButton(nextSubIcon, nextSubtitleAction);
        replySubButton = createButton(replySubIcon, replySubtitleAction);
        showSubButton = createButton(showSubIcon, toggleSubtitlesVisibilityAction);
        setSubtitleNavigationEnabled(false);

        setLayout(new MigLayout("fill, insets 0 0 0 0", "[]6[]12[]0[]0[]6[]12[]push[]0[]", "[]"));
        add(playPauseButton);
        add(stopButton, "sg 1");
        add(previousSubButton, "sg 1");
        add(replySubButton, "sg 1");
        add(nextSubButton, "sg 1");
        add(showSubButton);
        add(fullscreenButton, "sg 1");
        add(muteButton, "sg 1");
        add(volumeSlider, "wmax 100");

        volumeSlider.addChangeListener(e -> myPlayer().mediaPlayerComponent().mediaPlayer().audio().setVolume(volumeSlider.getValue()));
        // fullscreenButton.addActionListener(e -> myPlayer().mediaPlayerComponent().mediaPlayer().fullScreen().toggle());
    }

    private JButton createButton(Icon icon, ActionListener actionListener) {
        JButton jButton = new JButton();
        jButton.setHideActionText(true);
        jButton.setFocusable(false);
        jButton.setIcon(icon);
        jButton.addActionListener(actionListener);
        return jButton;
    }

    private Icon newIcon(String name) {
        return new ImageIcon(getClass().getResource("/icons/buttons/" + name + ".png"));
    }

    public void onPlaying() {
        playPauseButton.setIcon(pauseIcon);
    }


    public void onPaused() {
        playPauseButton.setIcon(playIcon);
    }

    public void onStopped() {
        playPauseButton.setIcon(playIcon);
    }

    public void setSubtitleNavigationEnabled(boolean b) {
        showSubButton.setEnabled(b);
        previousSubButton.setEnabled(b);
        nextSubButton.setEnabled(b);
        replySubButton.setEnabled(b);
    }

}
