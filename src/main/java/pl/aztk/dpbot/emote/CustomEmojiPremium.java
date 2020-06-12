package pl.aztk.dpbot.emote;

import net.dv8tion.jda.core.entities.Icon;
import pl.aztk.dpbot.hardcoded.HardcodedGuild;
import pl.aztk.dpbot.hardcoded.HardcodedRole;
import pl.aztk.dpbot.io.JSONSaver;
import pl.aztk.dpbot.util.EmojiUtil;

import java.io.File;

public class CustomEmojiPremium extends Emoji{
    public CustomEmojiPremium(String name, File imageFile) {
        super(name, imageFile);
        EmojiUtil.customPremiumEmojis.add(this);
        JSONSaver.saveOne(this);
    }
    public static CustomEmojiPremium fromName(String name){
        for(CustomEmojiPremium customEmoji : EmojiUtil.customPremiumEmojis){
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
                HardcodedGuild.DP_GUILD.getGuild().getController().createEmote(this.name, Icon.from(this.imageFile),
                        HardcodedRole.DONATOR.getRole(), HardcodedRole.VIP.getRole(), HardcodedRole.ELITE.getRole(), HardcodedRole.ADMIN.getRole(), HardcodedRole.ADMIN_EN.getRole(),
                        HardcodedRole.ADMIN_PL.getRole(), HardcodedRole.MOD_EN.getRole(), HardcodedRole.MOD_PL.getRole(), HardcodedRole.BOT.getRole()).queue();
            }else{
                HardcodedGuild.DP_GUILD.getGuild().getEmotesByName(this.name, true).get(0).delete().queue();
            }
            JSONSaver.saveOne(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
