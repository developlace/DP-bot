package pl.aztk.dpbot.commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import pl.aztk.dpbot.hardcoded.HardcodedGuild;
import pl.aztk.dpbot.help.HelpResource;
import pl.aztk.dpbot.help.HelpType;
import pl.aztk.dpbot.listeners.UserCountListener;
import pl.aztk.dpbot.util.ChannelUtil;
import pl.aztk.dpbot.util.CommandUtil;
import pl.aztk.dpbot.util.HelpUtil;

public class OnlineCommand implements Command, HelpResource{
    private String name;
    private int requiredVerificationLevel;

    public OnlineCommand(){
        this.name = "online";
        this.requiredVerificationLevel = 0;
        CommandUtil.commandList.add(this);
        HelpUtil.helpResources.add(this);
    }

    public String getName() {
        return this.name;
    }

    @Override
    public int getRequiredVerificationLevel() {
        return this.requiredVerificationLevel;
    }

    @Override
    public void execute(String[] args, MessageReceivedEvent event){
        if(ChannelUtil.isEnglish(event.getTextChannel())){
            event.getMessage().getChannel().sendMessage(":information_source: People online now: **" + UserCountListener.online + "**/" + HardcodedGuild.DP_GUILD.getGuild(event.getJDA()).getMembers().size() + " members").queue();
        }else{
            event.getMessage().getChannel().sendMessage(":information_source: Aktualna liczba osób online: **" + UserCountListener.online + "**/" + HardcodedGuild.DP_GUILD.getGuild(event.getJDA()).getMembers().size()).queue();
        }
    }

    @Override
    public String getHelpMessage() {
        return CommandUtil.cmdPrefix + name + " **Opis**: Liczba osób online";
    }

    @Override
    public String getEnglishHelpMessage() {
        return CommandUtil.cmdPrefix + name + " **Description**: See how many people are online";
    }

    @Override
    public HelpType getHelpResourceType() {
        return HelpType.COMMAND;
    }
}
