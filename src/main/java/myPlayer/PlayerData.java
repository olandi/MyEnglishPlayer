package myPlayer;

import myPlayer.utils.SubtitleNavigation;
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent;

import java.io.File;
import java.util.*;


public final class PlayerData {

    private static final int MAX_RECENT_MEDIA_SIZE = 10;

    private final EmbeddedMediaPlayerComponent mediaPlayerComponent;

    private final Deque<String> recentMedia = new ArrayDeque<>(MAX_RECENT_MEDIA_SIZE);

    private SubtitleNavigation subtitleNavigation;

    private PlayerData() {
        this.mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
    }

    private static final class MyPlayerHolder {
        private static final PlayerData INSTANCE = new PlayerData();
    }

    public static PlayerData myPlayer() {
        return MyPlayerHolder.INSTANCE;
    }


    public void addRecentMedia(String mrl) {
        if (!recentMedia.contains(mrl)) {
            recentMedia.addFirst(mrl);
            while (recentMedia.size() > MAX_RECENT_MEDIA_SIZE) {
                recentMedia.pollLast();
            }
        }
    }

    public List<String> recentMedia() {
        return new ArrayList<>(recentMedia);
    }

    public void clearRecentMedia() {
        recentMedia.clear();
    }

    public EmbeddedMediaPlayerComponent mediaPlayerComponent() {
        return mediaPlayerComponent;
    }


    public boolean createSubtitleNavigation(String path) {
        String checkingPath = "";
        int i = path.lastIndexOf('.');
        int p = Math.max(path.lastIndexOf('/'), path.lastIndexOf('\\'));
        if (i > p) {
            checkingPath = path.substring(0, i + 1).concat("srt");
        }

        File subFile = new File(checkingPath);

        boolean doExist = subFile.exists();
        if (doExist) {
            subtitleNavigation = new SubtitleNavigation(subFile.getPath());
        }
        return doExist;
    }

    public boolean areSubtitlesActivated() {
        return subtitleNavigation != null;
    }

    public void resetSubtitles() {
        subtitleNavigation = null;
    }

    public SubtitleNavigation getSubtitleNavigation() {
        return subtitleNavigation;
    }

}
