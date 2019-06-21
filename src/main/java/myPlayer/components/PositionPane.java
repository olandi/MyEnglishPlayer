package myPlayer.components;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static myPlayer.PlayerData.myPlayer;
import static myPlayer.utils.Time.formatTime;

public final class PositionPane extends JPanel {
    private final JLabel timeLabel;
    private final JSlider positionSlider;
    private final JLabel durationLabel;
    private long time;
    private final AtomicBoolean sliderChanging = new AtomicBoolean();
    private final AtomicBoolean positionChanging = new AtomicBoolean();

    public PositionPane() {
        timeLabel = new JLabel("9:99:99");
        positionSlider = new JSlider();
        positionSlider.setMinimum(0);
        positionSlider.setMaximum(1000);
        positionSlider.setValue(0);
        positionSlider.setFocusable(false);
        positionSlider.addChangeListener(e -> {
            if (!positionChanging.get()) {
                JSlider source = (JSlider) e.getSource();
                if (source.getValueIsAdjusting()) {
                    sliderChanging.set(true);
                } else {
                    sliderChanging.set(false);
                }
                myPlayer().mediaPlayerComponent().mediaPlayer().controls().setPosition(source.getValue() / 1000.0f);
            }
        });

        durationLabel = new JLabel("9:99:99");
        setLayout(new MigLayout("fill, insets 0 0 0 0", "[][grow][]", "[]"));
        add(timeLabel, "shrink");
        add(positionSlider, "grow");
        add(durationLabel, "shrink");
        timeLabel.setText("-:--:--");
        durationLabel.setText("-:--:--");
    }

    void refresh() {
        timeLabel.setText(formatTime(time));
        if (!sliderChanging.get()) {
            int value = (int) (myPlayer().mediaPlayerComponent().mediaPlayer().status().position() * 1000.0f);
            positionChanging.set(true);
            positionSlider.setValue(value);
            positionChanging.set(false);
        }
    }

    void setTime(long time) {
        this.time = time;
    }

    void setDuration(long duration) {
        durationLabel.setText(formatTime(duration));
    }
}
