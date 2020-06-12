package pl.aztk.dpbot.listeners;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.MessageUpdateEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import pl.aztk.dpbot.emote.CustomEmojiPremium;
import pl.aztk.dpbot.util.EmojiUtil;

public class MessageUpdatedListener extends ListenerAdapter {
    @Override
    public void onMessageUpdate(MessageUpdateEvent event){

        if(event.getAuthor().isBot()){ return; }

        if (EmojiUtil.containsCustomPremiumEmoji(event.getMessage().getContentRaw())) {
            Emote em = EmojiUtil.getEmojiFromText(event.getMessage().getContentRaw(), event.getGuild());
            Role r = event.getGuild().getRolesByName("donator", true).get(0);
            Role v = event.getGuild().getRolesByName("vip", true).get(0);
            if (!event.getMember().getRoles().contains(r) && !event.getMember().getRoles().contains(v) && !event.getMember().getPermissions().contains(Permission.MANAGE_CHANNEL)) {
                event.getMessage().delete().queue();
                if (event.getMessage().getChannel().getName().contains("en-")) {
                    event.getMember().getUser().openPrivateChannel().complete().sendMessage(EmojiUtil.getPremiumEmoteMessage(em, event.getMessage().getContentRaw(), true).build()).queue();
                } else {
                    event.getMember().getUser().openPrivateChannel().complete().sendMessage(EmojiUtil.getPremiumEmoteMessage(em, event.getMessage().getContentRaw(), false).build()).queue();
                }
            }
        }
        if (event.getMessage().getContentRaw().contains("cdn.discordapp.com/emojis")) {
            event.getMessage().delete().queue();
            for (CustomEmojiPremium emojiPremium : EmojiUtil.customPremiumEmojis) {
                String s = emojiPremium.getFullName();
                Emote em = EmojiUtil.getEmojiFromText(s, event.getGuild());
                if (event.getMessage().getContentRaw().contains(em.getImageUrl())) {
                    Role r = event.getGuild().getRolesByName("donator", true).get(0);
                    Role v = event.getGuild().getRolesByName("vip", true).get(0);
                    if (!event.getMember().getRoles().contains(r) && !event.getMember().getRoles().contains(v) && !event.getMember().getPermissions().contains(Permission.MANAGE_CHANNEL)) {
                        if (event.getMessage().getChannel().getName().contains("en-")) {
                            event.getMember().getUser().openPrivateChannel().complete().sendMessage(EmojiUtil.getPremiumEmoteMessage(em, event.getMessage().getContentRaw(), true).build()).queue();
                        } else {
                            event.getMember().getUser().openPrivateChannel().complete().sendMessage(EmojiUtil.getPremiumEmoteMessage(em, event.getMessage().getContentRaw(), false).build()).queue();
                        }
                        event.getMessage().delete().queue();
                    }
                }

            }
        }

    }
}
