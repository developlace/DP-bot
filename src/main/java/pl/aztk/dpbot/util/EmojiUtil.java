package pl.aztk.dpbot.util;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Guild;
import pl.aztk.dpbot.emote.CustomEmoji;
import pl.aztk.dpbot.emote.CustomEmojiPremium;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class EmojiUtil {

    public static List<CustomEmojiPremium> customPremiumEmojis = new ArrayList<>();
    public static List<CustomEmoji> customEmojis = new ArrayList<>();

    public static boolean containsCustomPremiumEmoji(String s){
        for(CustomEmojiPremium emojiPremium : customPremiumEmojis){
            if(s.contains(emojiPremium.getFullName())){
                return true;
            }
        }
        return false;
    }


    @Deprecated
    public static List<String> premiumEmojis = new ArrayList<>();
    @Deprecated
    public static boolean containsPremiumEmoji(String s){
        for(String em : premiumEmojis){
            if(s.contains(em)){
                return true;
            }
        }
        return false;
    }

    public static Emote getEmojiFromText(String text, Guild guild){
        for(CustomEmojiPremium emojiPremium : customPremiumEmojis){
            if(text.contains(emojiPremium.getFullName())) {
                return guild.getEmotesByName(emojiPremium.getFullName().replace(":", ""), true).get(0);
            }
        }
        return null;
    }
    public static EmbedBuilder getPremiumEmoteMessage(Emote em, String message, boolean english){
        EmbedBuilder eb = new EmbedBuilder();
        eb.setThumbnail(em.getImageUrl());
        eb.setTitle("Emoji premium");
        if (english){
            eb.setDescription(":warning: This emoji is only avaible for the donators!" + System.lineSeparator() + System.lineSeparator() + "Removed message: " + message + System.lineSeparator() + System.lineSeparator() + "Become one here: http://doprogramowania.pl/dotacja");
        } else {
            eb.setDescription(":warning: To emoji jest dostępne tylko dla donatorów!" + System.lineSeparator() + System.lineSeparator() + "Usunięta wiadomość: " + message + System.lineSeparator() + System.lineSeparator() + "Zostań nim tutaj: http://doprogramowania.pl/dotacja");
        }
        eb.setColor(Color.RED);
        return  eb;
    }
    public static String unicodeToText(String unicodee) {
        String unicode = unicodee.replace(" ", "");
        if (unicode.equalsIgnoreCase("\uD83C\uDDF5\uD83C\uDDF1")) {
            return "flag_pl";
        }
        if (unicode.equalsIgnoreCase("\uD83C\uDDEC\uD83C\uDDE7")) {
            return "flag_gb";
        }
        return null;
    }

    public static String textToUnicode(String text){
        String textt = text.replace(" ", "");
        if(textt.equalsIgnoreCase("flag_pl")){
            return "\uD83C\uDDF5\uD83C\uDDF1";
        }
        if(textt.equalsIgnoreCase("flag_gb")){
            return "\uD83C\uDDEC\uD83C\uDDE7";
        }
        return null;
    }
}
