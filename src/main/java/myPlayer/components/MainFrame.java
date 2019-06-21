package myPlayer.components;

import myPlayer.actions.NextSubtitleAction;
import myPlayer.actions.PlayPauseAction;
import myPlayer.actions.PreviousSubtitleAction;
import myPlayer.actions.ReplySubtitleAction;
import myPlayer.utils.Resource;
import net.miginfocom.swing.MigLayout;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.prefs.Preferences;

import static myPlayer.PlayerData.myPlayer;


public class MainFrame extends JFrame {
    private PositionPane positionPane;
    private JFileChooser fileChooser = new JFileChooser();
    private ControlsPane controlsPane;

    public MainFrame() {
        applyPreferences();

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);

        JMenuBar menuBar = new JMenuBar();
        JMenu mediaMenu = new JMenu(Resource.get("menu.media"));
        JMenuItem open = new JMenuItem(Resource.get("menu.media.item.openFile"));
        mediaMenu.add(open);
        open.addActionListener(e -> {
                    if (JFileChooser.APPROVE_OPTION == fileChooser.showOpenDialog(MainFrame.this)) {
                        File file = fileChooser.getSelectedFile();
                        String mrl = file.getAbsolutePath();

                        myPlayer().addRecentMedia(mrl);
                        myPlayer().mediaPlayerComponent().mediaPlayer().media().play(mrl);
                    }
                }
        );


        JMenu recent = new JMenu(Resource.get("menu.media.item.recent"));
        List<String> mrls = myPlayer().recentMedia();
        if (!mrls.isEmpty()) {
            for (String mrl : mrls) {
                JMenuItem jMenuItem = new JMenuItem(mrl);
                recent.add(jMenuItem);
                jMenuItem.addActionListener(e -> myPlayer().mediaPlayerComponent().mediaPlayer().media().play(mrl));
            }
            recent.add(new JSeparator());
        }
        mediaMenu.add(recent);


        JMenuItem quit = new JMenuItem(Resource.get("menu.media.item.quit"));
        mediaMenu.add(quit);
        quit.addActionListener(e -> {
            dispose();
            shutdown();
            //Window close
            System.exit(0);
        });

        menuBar.add(mediaMenu);

        JMenu audioMenu = new AudioMenu(Resource.get("menu.audio"));
        menuBar.add(audioMenu);
        setJMenuBar(menuBar);

        mainPanel.add(myPlayer().mediaPlayerComponent(), BorderLayout.CENTER);

        myPlayer().mediaPlayerComponent().mediaPlayer().events()
                .addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
                    @Override
                    public void lengthChanged(MediaPlayer mediaPlayer, long newLength) {
                        SwingUtilities.invokeLater(() -> positionPane.setDuration(newLength));
                    }

                    @Override
                    public void timeChanged(MediaPlayer mediaPlayer, long newTime) {
                        SwingUtilities.invokeLater(() -> {
                            positionPane.setTime(newTime);
                            positionPane.refresh();
                        });
                    }

                    @Override
                    public void playing(MediaPlayer mediaPlayer) {
                        controlsPane.onPlaying();
                    }

                    @Override
                    public void paused(MediaPlayer mediaPlayer) {
                        controlsPane.onPaused();
                    }

                    @Override
                    public void stopped(MediaPlayer mediaPlayer) {
                        controlsPane.onStopped();
                    }

                    @Override
                    public void finished(MediaPlayer mediaPlayer) {
                        SwingUtilities.invokeLater(() -> {
                            positionPane.setTime(0);
                            positionPane.refresh();
                        });
                    }

                    @Override
                    public void mediaPlayerReady(MediaPlayer mediaPlayer) {
                        updateSubtitlesData();
                    }
                });

        JPanel bottomPane = new JPanel();
        bottomPane.setLayout(new BorderLayout());

        JPanel bottomControlsPane = new JPanel();
        bottomControlsPane.setLayout(new MigLayout("fill, insets 0 n n n", "[grow]", "[]0[]"));

        positionPane = new PositionPane();
        bottomControlsPane.add(positionPane, "grow, wrap");

        controlsPane = new ControlsPane();
        bottomPane.add(bottomControlsPane, BorderLayout.CENTER);
        bottomControlsPane.add(controlsPane, "grow");

        mainPanel.add(bottomPane, BorderLayout.SOUTH);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                myPlayer().mediaPlayerComponent().mediaPlayer().controls().stop();
                setVisible(false);
                myPlayer().mediaPlayerComponent().release();
                shutdown();
            }
        });


        mainPanel.setFocusable(true);
        InputMap imap = mainPanel.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        imap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "Left");
        imap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "Right");
        imap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "Up");
        imap.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "Space");


        ActionMap amap = mainPanel.getActionMap();
        amap.put("Left", new PreviousSubtitleAction());
        amap.put("Right", new NextSubtitleAction());
        amap.put("Up", new ReplySubtitleAction());
        amap.put("Space", new PlayPauseAction());

    }

    private void updateSubtitlesData() {
        String mrl = "";
        try {
            mrl = URLDecoder.decode(myPlayer().mediaPlayerComponent().mediaPlayer().media().info().mrl(), "UTF-8");
            mrl = mrl.replace("file:", "");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        boolean isSubtitleNavigationCreated = myPlayer().createSubtitleNavigation(mrl);
        controlsPane.setSubtitleNavigationEnabled(isSubtitleNavigationCreated);
        if (!isSubtitleNavigationCreated) myPlayer().resetSubtitles();
    }


    private void applyPreferences() {
        Preferences prefs = Preferences.userNodeForPackage(MainFrame.class);
        setBounds(
                prefs.getInt("frameX", 100),
                prefs.getInt("frameY", 100),
                prefs.getInt("frameWidth", 800),
                prefs.getInt("frameHeight", 600)
        );

        fileChooser.setCurrentDirectory(new File(prefs.get("chooserDirectory", ".")));
        String recentMedia = prefs.get("recentMedia", "");
        if (recentMedia.length() > 0) {
            List<String> mrls = Arrays.asList(prefs.get("recentMedia", "").split("\\|"));
            Collections.reverse(mrls);
            for (String mrl : mrls) {
                myPlayer().addRecentMedia(mrl);
            }
        }
    }

    private void shutdown() {
        {
            Preferences prefs = Preferences.userNodeForPackage(MainFrame.class);
            prefs.putInt("frameX", getX());
            prefs.putInt("frameY", getY());
            prefs.putInt("frameWidth", getWidth());
            prefs.putInt("frameHeight", getHeight());
            prefs.put("chooserDirectory", fileChooser.getCurrentDirectory().toString());

            String recentMedia;
            List<String> mrls = myPlayer().recentMedia();
            if (!mrls.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (String mrl : mrls) {
                    if (sb.length() > 0) {
                        sb.append('|');
                    }
                    sb.append(mrl);
                }
                recentMedia = sb.toString();
            } else {
                recentMedia = "";
            }
            prefs.put("recentMedia", recentMedia);
        }
    }
}
