package pl.aztk.dpbot.commands;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import pl.aztk.dpbot.DoProgramowaniaBot;
import pl.aztk.dpbot.users.DPUser;
import pl.aztk.dpbot.util.CommandUtil;

import java.util.HashMap;
import java.util.Map;

public class BadGuysCommand implements Command {

    private String name;

    public BadGuysCommand(){
        this.name = "badguys";
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
            Map<DPUser, Integer> warned = new HashMap<>();
            for(DPUser dpUser : DPUser.dpUserList){
                if(dpUser.getWarnCount() > 0){
                    warned.put(dpUser, dpUser.getWarnCount());
                }
                if(dpUser.getAllWarnings().size() > 0){
                    warned.put(dpUser, dpUser.getWarnCount()*2);
                }
            }
            int pos = 1;
            String message = "";
            StringBuilder sB = new StringBuilder(message);
            for(Map.Entry e : CommandUtil.entriesSortedByValues(warned)){
                DPUser dpUser = (DPUser) e.getKey();
                if(DoProgramowaniaBot.jda.getUserById(dpUser.getID()) == null){
                    String content = sB.toString();
                    if(sB.length() > 0){
                        event.getMessage().getTextChannel().sendMessage(content).queue();
                    }
                    sB.setLength(0);
                    continue;
                }
                sB.append("**#").append(pos).append("** ").append(DoProgramowaniaBot.jda.getUserById(dpUser.getID()).getName()).append(" - ").append(dpUser.getWarnCount()).append(" punktów ostrzeżeń obecnie (")
                        .append(dpUser.getAllWarnings().size()).append(" otrzymanych ostrzeżeń łącznie)").append(System.lineSeparator());
                pos++;
                if(sB.length() >= 1950){
                    event.getMessage().getTextChannel().sendMessage(sB.toString()).queue();
                    sB.setLength(0);
                }

            }
            event.getMessage().getTextChannel().sendMessage(sB.toString()).queue();
        }
    }
}
