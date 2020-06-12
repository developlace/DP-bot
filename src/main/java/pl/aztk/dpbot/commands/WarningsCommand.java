package pl.aztk.dpbot.commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import pl.aztk.dpbot.hardcoded.HardcodedGuild;
import pl.aztk.dpbot.help.HelpResource;
import pl.aztk.dpbot.help.HelpType;
import pl.aztk.dpbot.users.DPUser;
import pl.aztk.dpbot.util.ChannelUtil;
import pl.aztk.dpbot.util.CommandUtil;
import pl.aztk.dpbot.util.HelpUtil;

public class WarningsCommand implements Command, HelpResource{
    private String name;
    public WarningsCommand(){
        this.name="warnings";
        CommandUtil.commandList.add(this);
        HelpUtil.helpResources.add(this);
    }
    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getRequiredVerificationLevel() {
        return 0;
    }

    @Override
    public void execute(String[] args, MessageReceivedEvent event) {
        DPUser u;
        boolean self = false;
        if(event.getMessage().getMentionedUsers().size() == 1){
            u = DPUser.fromID(event.getMessage().getMentionedUsers().get(0).getId());

        }else{
            u = DPUser.fromID(event.getMessage().getAuthor().getId());
            self = true;
        }
        if(u.getWarnCount() == 0) {
            if(u.getAllWarnings().size() > 0 && self) {
                if (ChannelUtil.isEnglish(event.getMessage().getTextChannel())) {
                    event.getMessage().getTextChannel().sendMessage(":tada: **" + event.getMessage().getAuthor().getName() + "** doesn't have any warning points! You can check your warning history using command `dp!warnhistory`").queue();
                } else {
                    event.getMessage().getTextChannel().sendMessage(":tada: **" + event.getMessage().getAuthor().getName() + "** nie ma żadnych punktów ostrzeżeń! Możesz sprawdzić swoją historię warnów poprzez komendę `dp!warnhistory`").queue();
                }
            }else{
                if (ChannelUtil.isEnglish(event.getMessage().getTextChannel())) {
                    event.getMessage().getTextChannel().sendMessage(":tada: **" + HardcodedGuild.DP_GUILD.getGuild(event.getJDA()).getMemberById(u.getID()).getUser().getName() + "** doesn't have any warning points!").queue();
                } else {
                    event.getMessage().getTextChannel().sendMessage(":tada: **" + HardcodedGuild.DP_GUILD.getGuild(event.getJDA()).getMemberById(u.getID()).getUser().getName() + "** nie ma żadnych punktów ostrzeżeń!").queue();
                }
            }
        }else{
            if(ChannelUtil.isEnglish(event.getMessage().getTextChannel())){
                event.getMessage().getTextChannel().sendMessage(":warning: **" + HardcodedGuild.DP_GUILD.getGuild(event.getJDA()).getMemberById(u.getID()).getUser().getName() + "** has " + u.getWarnCount() + "/**100** warning points!").queue();
            }else{
                event.getMessage().getTextChannel().sendMessage(":warning: **" + HardcodedGuild.DP_GUILD.getGuild(event.getJDA()).getMemberById(u.getID()).getUser().getName() + "** ma " + u.getWarnCount() + "/**100** punktów ostrzeżeń!").queue();
            }
        }
    }

    @Override
    public String getHelpMessage() {
        return CommandUtil.cmdPrefix + name + " **Opis:** Sprawdzenie ile się ma ostrzeżeń. Można używać @wzmianek";
    }

    @Override
    public String getEnglishHelpMessage() {
        return CommandUtil.cmdPrefix + name + " **Description:** Checking warning points. Can use @mention";
    }

    @Override
    public HelpType getHelpResourceType() {
        return HelpType.COMMAND;
    }
}
