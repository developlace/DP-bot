package pl.aztk.dpbot.commands;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import pl.aztk.dpbot.help.HelpResource;
import pl.aztk.dpbot.help.HelpType;
import pl.aztk.dpbot.punishments.Warning;
import pl.aztk.dpbot.users.DPUser;
import pl.aztk.dpbot.util.*;

public class WarnHistoryCommand implements Command, HelpResource {
    private String name;

    public WarnHistoryCommand(){
        this.name="warnhistory";
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
        //ilosc@kiedy UTC za_co od_kogo czas_wygasniecia czy_już_wygasło
        if(event.getMember().getPermissions().contains(Permission.ADMINISTRATOR)){
            if(event.getMessage().getMentionedUsers().size() == 1){
                DPUser user = DPUser.fromID(event.getMessage().getMentionedUsers().get(0).getId());
                event.getTextChannel().sendMessage(":mailbox: " + event.getAuthor().getAsMention() + " sprawdź PW").queue();
                event.getAuthor().openPrivateChannel().complete().sendMessage(":information_source: Ostrzeżenia użytkownika **" + event.getMessage().getMentionedUsers().get(0).getName() + "**").queue();
                Table warningTable = new Table();
                warningTable.addRow("ilość punktów", "data (UTC)", "powód", "nadane przez", "wygasło");
                for (Warning w : user.getAllWarnings()) {
                    warningTable.addRow(Integer.toString(w.getWarningPoints()), w.getTimeGivenUTC(), w.getReason().toLowerCase(), w.getGivenBy(), Boolean.toString(w.hasExpired()).replace("true", "tak").replace("false", "nie"));
                }
                try{
                    event.getAuthor().openPrivateChannel().complete().sendMessage(warningTable.build()).queue();
                }catch(TableTextTooLongException e) {
                    event.getAuthor().openPrivateChannel().complete().sendMessage(e.getText()).queue();
                    sendNextTables(event.getAuthor(), e);
                }
                return;
            }
        }
        DPUser user = DPUser.fromID(event.getAuthor().getId());
        if(user.getAllWarnings().size() > 0){
            if(ChannelUtil.isEnglish(event.getMessage().getTextChannel())){
                event.getTextChannel().sendMessage(":mailbox: " + event.getAuthor().getAsMention() + ", check your PMs").queue();
                event.getAuthor().openPrivateChannel().complete().sendMessage(":information_source: Your warnings:").queue();
                Table warningTable = new Table();
                warningTable.addRow("points", "date (UTC)", "reason", "given by", "has expired");
                for (Warning w : user.getAllWarnings()) {
                    warningTable.addRow(Integer.toString(w.getWarningPoints()), w.getTimeGivenUTC(), w.getReason().toLowerCase(), w.getGivenBy(), Boolean.toString(w.hasExpired()).replace("true", "yes").replace("false", "no"));
                }
                try{
                    event.getAuthor().openPrivateChannel().complete().sendMessage(warningTable.build()).queue();
                }catch(TableTextTooLongException e){
                    event.getAuthor().openPrivateChannel().complete().sendMessage(e.getText()).queue();
                    sendNextTables(event.getAuthor(), e);
                }

            } else {
                event.getTextChannel().sendMessage(":mailbox: " + event.getAuthor().getAsMention() + " sprawdź PW").queue();
                event.getAuthor().openPrivateChannel().complete().sendMessage(":information_source: Twoje ostrzeżenia:").queue();
                Table warningTable = new Table();
                warningTable.addRow("ilość punktów", "data (UTC)", "powód", "nadane przez", "wygasło");
                for (Warning w : user.getAllWarnings()) {
                    warningTable.addRow(Integer.toString(w.getWarningPoints()), w.getTimeGivenUTC(), w.getReason().toLowerCase(), w.getGivenBy(), Boolean.toString(w.hasExpired()).replace("true", "tak").replace("false", "nie"));
                }
                try{
                    event.getAuthor().openPrivateChannel().complete().sendMessage(warningTable.build()).queue();
                }catch(TableTextTooLongException e){
                    event.getAuthor().openPrivateChannel().complete().sendMessage(e.getText()).queue();
                    sendNextTables(event.getAuthor(), e);
                }
            }
        }else{
            if(ChannelUtil.isEnglish(event.getMessage().getTextChannel())) {
                event.getMessage().getTextChannel().sendMessage(":tada: **" + event.getAuthor().getName() + "** didn't recieve any warnings!").queue();
            }else{
                event.getMessage().getTextChannel().sendMessage(":tada: **" + event.getAuthor().getName() + "** nie otrzymał żadnych ostrzeżeń!").queue();
            }
        }
    }

    @Override
    public String getHelpMessage() {
        return CommandUtil.cmdPrefix + name + " **Opis:** Historia warnów. Można używać tylko dla swojego konta (bez @wzmianek).";
    }

    @Override
    public String getEnglishHelpMessage() {
        return CommandUtil.cmdPrefix + name + " **Description:** Warn history. Only use on your account (without @mentions).";
    }

    @Override
    public HelpType getHelpResourceType() {
        return HelpType.COMMAND;
    }

    private void sendNextTables(User user, TableTextTooLongException exception){
        try{
            user.openPrivateChannel().complete().sendMessage(exception.getTable().buildFrom(exception.getCurrentRowNumber())).queue();
        }catch(TableTextTooLongException e){
            user.openPrivateChannel().complete().sendMessage(e.getText()).queue();
            sendNextTables(user, e);
        }
    }
}