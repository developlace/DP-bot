package pl.aztk.dpbot.commands;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import pl.aztk.dpbot.quicksettings.QuickSetting;
import pl.aztk.dpbot.util.CommandUtil;

public class QuickSettingCommand implements Command{

    private String name;

    public QuickSettingCommand(){
        this.name = "quicksetting";
        CommandUtil.commandList.add(this);
    }

    //syntax: quicksetting get/set key value
    @Override
    public void execute(String[] args, MessageReceivedEvent event) {
        if(event.getMember().getPermissions().contains(Permission.ADMINISTRATOR)){
            QuickSetting qs = new QuickSetting(args[2]);
            if(args[1].equalsIgnoreCase("set")){
                if(args.length == 4){
                    qs.setValue(args[3]);
                }
            }
            if(args[1].equalsIgnoreCase("get")){
                event.getChannel().sendMessage(args[2] + " ma wartość " + qs.getValue()).queue();
            }
        }
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getRequiredVerificationLevel() {
        return 2;
    }

}
