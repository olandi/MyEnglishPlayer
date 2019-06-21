package myPlayer.utils;

import java.util.ResourceBundle;

public final class Resource {
    private static final String RESOURCE_BUNDLE_BASE_NAME = "strings/vlcj-player";
    private static final ResourceBundle resourceBundle = ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE_NAME);

    public static String get(String value) {
        if (resourceBundle.containsKey(value)) {
            return resourceBundle.getString(value);
        } else {
            return null;
        }
    }
}
