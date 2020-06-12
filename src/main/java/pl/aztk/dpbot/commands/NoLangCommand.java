package pl.aztk.dpbot.commands;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import pl.aztk.dpbot.hardcoded.HardcodedGuild;
import pl.aztk.dpbot.hardcoded.HardcodedRole;
import pl.aztk.dpbot.util.CommandUtil;

public class NoLangCommand implements Command {

    private String name;
    public NoLangCommand(){
        this.name = "nolang";
        CommandUtil.commandList.add(this);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getRequiredVerificationLevel() {
        return 2;
    }

    @Override
    public void execute(String[] args, MessageReceivedEvent event) {
        if(event.getMember().getPermissions().contains(Permission.ADMINISTRATOR)){
            if(args.length == 2){
                if(args[1].equalsIgnoreCase("add")){
                    for(Member m : HardcodedGuild.DP_GUILD.getGuild(event.getJDA()).getMembers()){
                        if(!(m.getRoles().contains(HardcodedRole.PL.getRole(event.getJDA()))) && !(m.getRoles().contains(HardcodedRole.EN.getRole(event.getJDA())))){
                            HardcodedGuild.DP_GUILD.getGuild(event.getJDA()).getController().addSingleRoleToMember(m, HardcodedRole.NO_LANG.getRole(event.getJDA())).queue();
                        }
                    }
                }else if(args[1].equalsIgnoreCase("pl+en")){
                    for(Member m : HardcodedGuild.DP_GUILD.getGuild(event.getJDA()).getMembers()){
                        if(m.getRoles().contains(HardcodedRole.PL.getRole(event.getJDA())) && m.getRoles().contains(HardcodedRole.EN.getRole(event.getJDA()))){
                            HardcodedGuild.DP_GUILD.getGuild(event.getJDA()).getController().addSingleRoleToMember(m, HardcodedRole.PL_EN.getRole(event.getJDA())).queue();
                            HardcodedGuild.DP_GUILD.getGuild(event.getJDA()).getController().removeSingleRoleFromMember(m, HardcodedRole.PL.getRole(event.getJDA())).queue();
                            HardcodedGuild.DP_GUILD.getGuild(event.getJDA()).getController().removeSingleRoleFromMember(m, HardcodedRole.EN.getRole(event.getJDA())).queue();
                        }
                    }
                }
            }
        }
    }
}
