package pl.aztk.dpbot.listeners;

import net.dv8tion.jda.core.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import pl.aztk.dpbot.hardcoded.HardcodedGuild;
import pl.aztk.dpbot.hardcoded.HardcodedRole;
import pl.aztk.dpbot.hardcoded.HardcodedTextChannel;
import pl.aztk.dpbot.util.EmojiUtil;

public class ReactionRemoveListener extends ListenerAdapter {
    @Override
    public void onMessageReactionRemove(MessageReactionRemoveEvent event){
        if(event.getChannel().getId().equals(HardcodedTextChannel.LANGUAGE_CHANNEL.getChannel(event.getJDA()).getId())){
            if(EmojiUtil.unicodeToText(event.getReactionEmote().getName()).equalsIgnoreCase("flag_pl")){
                if(event.getMember().getRoles().contains(HardcodedRole.PL.getRole(event.getJDA())) || event.getMember().getRoles().contains(HardcodedRole.PL_EN.getRole(event.getJDA()))){
                    if(event.getMember().getRoles().contains(HardcodedRole.PL_EN.getRole(event.getJDA()))){
                        HardcodedGuild.DP_GUILD.getGuild(event.getJDA()).getController().removeSingleRoleFromMember(event.getMember(), HardcodedRole.PL_EN.getRole(event.getJDA())).queue();
                        HardcodedGuild.DP_GUILD.getGuild(event.getJDA()).getController().addSingleRoleToMember(event.getMember(), HardcodedRole.EN.getRole(event.getJDA())).queue();
                    }else if(event.getMember().getRoles().contains(HardcodedRole.PL.getRole(event.getJDA()))){
                        HardcodedGuild.DP_GUILD.getGuild(event.getJDA()).getController().removeSingleRoleFromMember(event.getMember(), HardcodedRole.PL.getRole(event.getJDA())).queue();
                        HardcodedGuild.DP_GUILD.getGuild(event.getJDA()).getController().addSingleRoleToMember(event.getMember(), HardcodedRole.NO_LANG.getRole(event.getJDA())).queue();
                    }
                }
            }
            if(EmojiUtil.unicodeToText(event.getReactionEmote().getName()).equalsIgnoreCase("flag_gb")){
                if(event.getMember().getRoles().contains(HardcodedRole.EN.getRole(event.getJDA())) || event.getMember().getRoles().contains(HardcodedRole.PL_EN.getRole(event.getJDA()))){
                    if(event.getMember().getRoles().contains(HardcodedRole.PL_EN.getRole(event.getJDA()))){
                        HardcodedGuild.DP_GUILD.getGuild(event.getJDA()).getController().removeSingleRoleFromMember(event.getMember(), HardcodedRole.PL_EN.getRole(event.getJDA())).queue();
                        HardcodedGuild.DP_GUILD.getGuild(event.getJDA()).getController().addSingleRoleToMember(event.getMember(), HardcodedRole.PL.getRole(event.getJDA())).queue();
                    }else if(event.getMember().getRoles().contains(HardcodedRole.EN.getRole(event.getJDA()))){
                        HardcodedGuild.DP_GUILD.getGuild(event.getJDA()).getController().removeSingleRoleFromMember(event.getMember(), HardcodedRole.EN.getRole(event.getJDA())).queue();
                        HardcodedGuild.DP_GUILD.getGuild(event.getJDA()).getController().addSingleRoleToMember(event.getMember(), HardcodedRole.NO_LANG.getRole(event.getJDA())).queue();
                    }
                }
            }
        }
    }
}
