package pl.aztk.dpbot.commands;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import pl.aztk.dpbot.help.HelpResource;
import pl.aztk.dpbot.help.HelpType;
import pl.aztk.dpbot.punishments.WarnPredefinedReason;
import pl.aztk.dpbot.punishments.Warning;
import pl.aztk.dpbot.users.DPUser;
import pl.aztk.dpbot.util.ChannelUtil;
import pl.aztk.dpbot.util.CommandUtil;
import pl.aztk.dpbot.util.HelpUtil;

public class WarnCommand implements Command, HelpResource{

    private String name;
    private int requiredVerificationLevel;

    public WarnCommand(){
        this.name = "warn";
        this.requiredVerificationLevel = 1;
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
        //Syntax /warn user reason OR /warn user custom points exp_time reason
        if(event.getMember().getPermissions().contains(Permission.MESSAGE_MANAGE)) {
            if (event.getMessage().getMentionedUsers().size() == 1) {
                DPUser user = DPUser.fromID(event.getMessage().getMentionedUsers().get(0).getId());
                if(args.length == 3){
                    try{
                        WarnPredefinedReason reason = WarnPredefinedReason.valueOf(args[2].toUpperCase());
                        Warning warningGiven = new Warning(user, reason, event.getAuthor());
                        if(ChannelUtil.isEnglish(event.getTextChannel())) {
                            event.getTextChannel().sendMessage(":ok_hand: " + event.getMessage().getMentionedUsers().get(0).getName() + " was given **" + warningGiven.getWarningPoints() + "** warnings for** " +
                                    warningGiven.getReason().toLowerCase() + "** by **" + warningGiven.getGivenBy() +  "**, which will expire in **" + warningGiven.getPredefinedReason().getDaysExpirationTime() + "** days. They now have " + warningGiven.getUserWarned().getWarnCount() + "/**100** warnings").queue();
                        }else{
                            event.getTextChannel().sendMessage(":ok_hand: Użytkownikowi " + event.getMessage().getMentionedUsers().get(0).getName() + " zostało nadane **" + warningGiven.getWarningPoints() + "** ostrzeżeń za** " +
                                    warningGiven.getReason().toLowerCase() + "** przez **" + warningGiven.getGivenBy() +  "**. Wygasają one za **" + warningGiven.getPredefinedReason().getDaysExpirationTime() + "** dni. Teraz ma " + warningGiven.getUserWarned().getWarnCount() + "/**100** ostrzeżeń").queue();
                        }
                    }catch(IllegalArgumentException ex){
                        ex.printStackTrace();
                        if(ChannelUtil.isEnglish(event.getTextChannel())) {
                            event.getTextChannel().sendMessage(":warning: Predefined warn type not found, please use `custom` instead").queue();
                        }else{
                            event.getTextChannel().sendMessage(":warning: Nie znaleziono predefiniowanego typu warna, zamiast tego użyj `custom`.").queue();
                        }
                    }
                }else if(args.length >= 6){
                    try{
                        WarnPredefinedReason reason = WarnPredefinedReason.valueOf(args[2].toUpperCase());
                        StringBuilder customReason = new StringBuilder();
                        for(int i = 5; i < args.length; i++){
                            customReason.append(args[i]).append(" ");
                        }
                        Warning warningGiven = new Warning(user, reason, Integer.valueOf(args[3]), Integer.valueOf(args[4]), customReason.toString(), event.getAuthor());
                        if(ChannelUtil.isEnglish(event.getTextChannel())) {
                            event.getTextChannel().sendMessage(":ok_hand: " + event.getMessage().getMentionedUsers().get(0).getName() + " was given **" + warningGiven.getWarningPoints() + "** warnings for** " +
                                    warningGiven.getReason().toLowerCase() + "** by **" + warningGiven.getGivenBy() + "**, which will expire in ** " + Integer.valueOf(args[4]) + "** days. They now have " + warningGiven.getUserWarned().getWarnCount() + "/**100** warnings.").queue();
                        }else{
                            event.getTextChannel().sendMessage(":ok_hand: Użytkownikowi " + event.getMessage().getMentionedUsers().get(0).getName() + " zostało nadane **" + warningGiven.getWarningPoints() + "** ostrzeżeń za** " +
                                    warningGiven.getReason().toLowerCase() + "** przez **" + warningGiven.getGivenBy() +  "**. Wygasają one za **"  + Integer.valueOf(args[4]) + "** dni. Teraz ma " + warningGiven.getUserWarned().getWarnCount() + "/**100** ostrzeżeń.").queue();
                        }
                    }catch(IllegalArgumentException ex){
                        ex.printStackTrace();
                        if(ChannelUtil.isEnglish(event.getTextChannel())) {
                            event.getTextChannel().sendMessage(":warning: Predefined warn type not found, please use `custom` instead").queue();
                        }else{
                            event.getTextChannel().sendMessage(":warning: Nie znaleziono predefiniowanego typu warna, zamiast tego użyj `custom`.").queue();
                        }
                    }
                }
            }else{
                if(ChannelUtil.isEnglish(event.getTextChannel())) {
                    event.getTextChannel().sendMessage(":warning: You need to use exactly one mention!").queue();
                }else{
                    event.getTextChannel().sendMessage(":warning: Musisz użyć dokładnie jednej wzmianki!").queue();
                }
            }
        }
    }

    @Override
    public String getHelpMessage() {
        return CommandUtil.cmdPrefix + name + " **Opis:** Warny **Użycie:** dp!warn @wzmianka powód(pkt, wygasanie(dni)) <SPAM(5, 30) OFFTOPIC(5, 30) BAD_LANGUAGE(5, 30) FLAME_WAR(10, 45) REFLINK(15, 60) TROLLING(15,60) lub" + System.lineSeparator() +
                "dp!warn @wzmianka custom ilosc_pkt czas_wygasania(dni) powod...";
    }

    @Override
    public String getEnglishHelpMessage() {
        return CommandUtil.cmdPrefix + name + " **Description:** Warnings **Usage:** dp!warn @mention reason(points, expiration(days) from: <SPAM(5, 30) OFFTOPIC(5, 30) BAD_LANGUAGE(5, 30) FLAME_WAR(10, 45) REFLINK(15, 60) TROLLING(15,60) OR" + System.lineSeparator() +
                "dp!warn @mention custom points expiration(days) reason...";
    }

    @Override
    public HelpType getHelpResourceType() {
        return HelpType.COMMAND;
    }
}
