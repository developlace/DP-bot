package pl.aztk.dpbot.commands;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import pl.aztk.dpbot.hardcoded.HardcodedGuild;
import pl.aztk.dpbot.hardcoded.HardcodedRole;
import pl.aztk.dpbot.util.CommandUtil;

//TODO If we want to keep it, rework - adding roles to users takes a lot of time, rework based on changing channel permissions

public class MaintenanceCommand implements Command {
    private String name;

    public MaintenanceCommand(){
        this.name = "maintenance";
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
        if(event.getMember().getPermissions().contains(Permission.ADMINISTRATOR)) {
            if (args[1].equalsIgnoreCase("on")) {
                for (Member m : HardcodedGuild.DP_GUILD.getGuild(event.getJDA()).getMembers()) {
                    HardcodedGuild.DP_GUILD.getGuild(event.getJDA()).getController().addSingleRoleToMember(m, HardcodedRole.MAINTENANCE.getRole()).queue();
                }
            }else if(args[1].equalsIgnoreCase("off")){
                for (Member m : HardcodedGuild.DP_GUILD.getGuild(event.getJDA()).getMembers()) {
                    HardcodedGuild.DP_GUILD.getGuild(event.getJDA()).getController().removeSingleRoleFromMember(m, HardcodedRole.MAINTENANCE.getRole()).queue();
                }
            }
        }
    }
}
