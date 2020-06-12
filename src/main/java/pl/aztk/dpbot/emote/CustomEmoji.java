package pl.aztk.dpbot.emote;

import net.dv8tion.jda.core.entities.Icon;
import net.dv8tion.jda.core.entities.Role;
import pl.aztk.dpbot.hardcoded.HardcodedGuild;
import pl.aztk.dpbot.io.JSONSaver;
import pl.aztk.dpbot.util.EmojiUtil;

import java.io.File;

public class CustomEmoji extends Emoji {

    public CustomEmoji(String name, File imageFile) {
        super(name, imageFile);
        EmojiUtil.customEmojis.add(this);
        JSONSaver.saveOne(this);
    }

    public static CustomEmoji fromName(String name){
        for(CustomEmoji customEmoji : EmojiUtil.customEmojis){
            if(customEmoji.getRawName().equalsIgnoreCase(name)){
                return customEmoji;
            }
        }
        return null;
    }

    @Override
    public void setActive(boolean active){
        try {
            this.isActive = active;
            if(active) {
                HardcodedGuild.DP_GUILD.getGuild().getController().createEmote(this.getRawName(), Icon.from(this.imageFile), (Role) null).queue();
            }else{
                HardcodedGuild.DP_GUILD.getGuild().getEmotesByName(this.name, true).get(0).delete().queue();
            }
            JSONSaver.saveOne(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
