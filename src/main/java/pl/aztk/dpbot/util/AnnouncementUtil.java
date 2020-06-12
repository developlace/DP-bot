package pl.aztk.dpbot.util;

import java.awt.*;

public class AnnouncementUtil {
    public static Color hex2RGB(String colorStr) {
        return new Color(
                Integer.valueOf( colorStr.substring( 1, 3 ), 16 ),
                Integer.valueOf( colorStr.substring( 3, 5 ), 16 ),
                Integer.valueOf( colorStr.substring( 5, 7 ), 16 ));
    }
}
