package pl.aztk.dpbot.messagechecks;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import pl.aztk.dpbot.roles.DPRole;
import pl.aztk.dpbot.util.ChannelUtil;

public class RolePingedCheck implements MessageCheck{
    @Override
    public boolean passed(MessageReceivedEvent event) {
        if(event.getMessage().getMentionedRoles().size() > 0 ){
            for(Role r : event.getMessage().getMentionedRoles()){
                DPRole dpRole = DPRole.getByRoleID(r.getId());
                if(dpRole != null){
                    if(dpRole.isEnabled()){
                        if(dpRole.getTextChannelsMentionable().contains(event.getMessage().getTextChannel().getId()) || event.getMessage().getMember().hasPermission(Permission.MESSAGE_MENTION_EVERYONE)){
                            Role toMention = event.getGuild().getRoleById(dpRole.getRoleToMentionID());
                            String text = "";
                            if(!ChannelUtil.isEnglish(event.getTextChannel())) {
                                text = ":mega: " + event.getAuthor().getAsMention() + " oznacza rangę " + toMention.getAsMention() + "!";
                            }else{
                                text = ":mega: " + event.getAuthor().getAsMention() + " mentions " + toMention.getAsMention() + " role!";
                            }
                            String finalText = text;
                            toMention.getManager().setMentionable(true).queue(success -> event.getTextChannel().sendMessage(finalText).queue(abc -> toMention.getManager().setMentionable(false).queue()));
                        }else{
                            if(!ChannelUtil.isEnglish(event.getTextChannel())) {
                                event.getTextChannel().sendMessage(":warning: " + event.getAuthor().getAsMention() + ", nie możesz oznaczyć rangi " + r.getAsMention() + " na tym kanale!").queue();
                            }else{
                                event.getTextChannel().sendMessage(":warning: " + event.getAuthor().getAsMention() + ", you can't mention " + r.getAsMention() + " role on this channel!").queue();
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
}
