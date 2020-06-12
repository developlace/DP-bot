package pl.aztk.dpbot.util;

import net.dv8tion.jda.core.entities.TextChannel;

public class ChannelUtil {
    public static boolean isEnglish(TextChannel tc){
        return tc.getName().contains("en-");
    }
}
