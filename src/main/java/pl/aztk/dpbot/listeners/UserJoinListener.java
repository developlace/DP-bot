package pl.aztk.dpbot.listeners;

import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import pl.aztk.dpbot.hardcoded.HardcodedRole;
import pl.aztk.dpbot.users.DPUser;

public class UserJoinListener extends ListenerAdapter {
    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event){
        event.getGuild().getController().addSingleRoleToMember(event.getMember(), HardcodedRole.USER.getRole(event.getJDA())).reason("autorole").queue();
        event.getGuild().getController().addSingleRoleToMember(event.getMember(), HardcodedRole.NO_LANG.getRole(event.getJDA())).reason("default").queue();
        if(!DPUser.exists(event.getUser().getId())){
            new DPUser(event.getUser().getId());
        }
    }
}
