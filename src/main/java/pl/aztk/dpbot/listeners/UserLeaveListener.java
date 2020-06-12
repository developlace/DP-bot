package pl.aztk.dpbot.listeners;

import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import pl.aztk.dpbot.hardcoded.HardcodedTextChannel;

public class UserLeaveListener extends ListenerAdapter {
    @Override
    public void onGuildMemberLeave(GuildMemberLeaveEvent event){
        HardcodedTextChannel.LEAVING_CHANNEL.getChannel(event.getJDA()).sendMessage("**@" + event.getUser().getName() + "** sobie poszedl :/").queue();
    }
}
