package pl.aztk.dpbot.blacklist;

import com.google.gson.annotations.Expose;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import pl.aztk.dpbot.DoProgramowaniaBot;
import pl.aztk.dpbot.hardcoded.HardcodedGuild;
import pl.aztk.dpbot.hardcoded.HardcodedTextChannel;
import pl.aztk.dpbot.io.JSONSaver;
import pl.aztk.dpbot.punishments.WarnPredefinedReason;
import pl.aztk.dpbot.punishments.Warning;
import pl.aztk.dpbot.users.DPUser;
import pl.aztk.dpbot.util.*;

import java.util.HashMap;
import java.util.Map;

import static pl.aztk.dpbot.util.BlacklistWarningUitl.swearLimit;

public class BlacklistReport{

    public static Map<String, BlacklistReport> reports = new HashMap<>();

    @Expose
    private final String messageID;
    @Expose
    private final String connectedMessageID;
    @Expose
    private final String blacklistedWord;

    private DPUser userInvolved;

    //IMPORTANT: JUST FOR USE IN IO
    @Expose
    private String userID;

    private Message connectedMessage;

    @Expose
    private long timeExpires;
    @Expose
    private long timeArchives;
    @Expose
    private boolean hasExpired;
    @Expose
    private boolean hasArchived;

    private TextChannel textChannel;

    //IMPORTANT: JUST FOR USE IN IO
    @Expose
    private String textChannelID;

    @Expose
    private boolean hadAlreadyGivenPunishment = false;
    @Expose
    private boolean hadAlreadyDeniedPunishment = false;
    @Expose
    private boolean hadAlreadyTakenPunishmentAction = false;

    public BlacklistReport(String messageID, String connectedMessageID, Message connectedMessage, TextChannel textChannel, String blacklistedWord, DPUser userInvolved) {
        this.messageID = messageID;
        long expirationTime = 300000;
        this.timeExpires = System.currentTimeMillis() + expirationTime;
        long archivationTime = 172800000;
        this.timeArchives = System.currentTimeMillis() + archivationTime;
        this.connectedMessageID = connectedMessageID;
        this.textChannel = textChannel;
        this.blacklistedWord = blacklistedWord;
        this.userInvolved = userInvolved;
        this.connectedMessage = connectedMessage;
        reports.put(this.messageID, this);
        prepareToSave();
        JSONSaver.saveOne(this);
    }

    //TODO do stuff with PM and channels sending

    public void acceptPunishment(User userAccepting){
        if(hadAlreadyGivenPunishment){
            return;
        }

        hadAlreadyGivenPunishment = true;
        hadAlreadyDeniedPunishment = false;
        User user = HardcodedGuild.DP_GUILD.getGuild().getMemberById(userInvolved.getID()).getUser();
        String messageContent = connectedMessage.getContentRaw().replace(blacklistedWord, "**" + blacklistedWord + "**");

        prepareToSave();
        JSONSaver.saveOne(this);

        userInvolved.incrementSwearMessageCount();

        HardcodedTextChannel.LOGS_CHANNEL.getChannel().getMessageById(this.messageID).queue(messageG -> {
            MessageEmbed embed = messageG.getEmbeds().get(0);
            EmbedBuilder eb = new EmbedBuilder(embed);
            eb.appendDescription(System.lineSeparator());
            if(!this.hadAlreadyTakenPunishmentAction) {
                eb.appendDescription("- " + userAccepting.getName() + " nadał ostrzeżenie.");
            }else if(this.hadAlreadyTakenPunishmentAction){
                eb.appendDescription("- " + userAccepting.getName() + " nadał ostrzeżenie - poprawa poprzedniej decyzji");
            }else{
                eb.appendDescription("- " + userAccepting.getName() + " nadał ostrzeżenie.");
            }
            eb.setColor(AnnouncementUtil.hex2RGB("#32ef21"));
            messageG.editMessage(eb.build()).queue(message -> hadAlreadyTakenPunishmentAction = true);
        });

        connectedMessage.delete().queue();

        if (Math.abs(userInvolved.getSentMessageCount() - userInvolved.getLastSwearingWarningMessageNumber()) >= swearLimit) {
            if (ChannelUtil.isEnglish(textChannel)) {
                user.openPrivateChannel().complete().sendMessage(":warning: Used a blacklisted phrase!" + System.lineSeparator() + System.lineSeparator() + "Deleted message: " + messageContent).queue();
                connectedMessage.getTextChannel().sendMessage(":warning: " + user.getAsMention() + " try not to use bad language! This time without warning points, but if you swear again in next messages, you __will__ be warned. \n \n __Message was manually reviewed by a moderator.__").queue();
                EmbedBuilder eb = new EmbedBuilder();
                eb.setColor(AnnouncementUtil.hex2RGB("#4286f4"));
                eb.setAuthor(user.getName(), null, user.getAvatarUrl());
                eb.setTitle("Copy of the removed message:");
                StringBuilder sb = new StringBuilder();
                for(int i = 0; i<blacklistedWord.length(); i++){
                    sb.append("\\*");
                }
                eb.appendDescription(connectedMessage.getContentRaw().replace(blacklistedWord, sb.toString()));
                connectedMessage.getTextChannel().sendMessage(eb.build()).queue();
            }else{
                user.openPrivateChannel().complete().sendMessage(":warning: Użyto słowa z czarnej listy!" + System.lineSeparator() + System.lineSeparator() + "Usunięta wiadomość: " + messageContent).queue();
                connectedMessage.getTextChannel().sendMessage(":warning: " + user.getAsMention() + " staraj się nie używać wulgaryzmów! Tym razem tylko upomnienie, ale jeśli w następnych wiadomościach to powtórzysz, __otrzymasz__ ostrzeżenie. \n \n __Wiadomość została manualnie sprawdzona przez moderatora.__").queue();
                EmbedBuilder eb = new EmbedBuilder();
                eb.setColor(AnnouncementUtil.hex2RGB("#4286f4"));
                eb.setAuthor(user.getName(), null, user.getAvatarUrl());
                eb.setTitle("Kopia usuniętej wiadomości:");
                StringBuilder sb = new StringBuilder();

                for(int i = 0; i<blacklistedWord.length(); i++){
                    sb.append("\\*");
                }
                eb.appendDescription(connectedMessage.getContentRaw().replace(blacklistedWord, sb.toString()));
                connectedMessage.getTextChannel().sendMessage(eb.build()).queue();
            }
        } else if ((Math.abs(userInvolved.getSentMessageCount() - userInvolved.getLastSwearingWarningMessageNumber()) < swearLimit) && userInvolved.getLastSwearingWarningMessageNumber() == 0) {
            if (ChannelUtil.isEnglish(textChannel)){
                user.openPrivateChannel().complete().sendMessage(":warning: Used a blacklisted phrase!" + System.lineSeparator() + System.lineSeparator() + "Deleted message: " + messageContent).queue();
                connectedMessage.getTextChannel().sendMessage(":warning: " + user.getAsMention() + " try not to use bad language! This time without warning points, but if you swear again in next messages, you __will__ be warned. \n \n __Message was manually reviewed by a moderator.__").queue();
                EmbedBuilder eb = new EmbedBuilder();
                eb.setColor(AnnouncementUtil.hex2RGB("#4286f4"));
                eb.setAuthor(user.getName(), null, user.getAvatarUrl());
                eb.setTitle("Copy of the removed message:");
                StringBuilder sb = new StringBuilder();
                for(int i = 0; i < blacklistedWord.length(); i++){
                    sb.append("\\*");
                }
                eb.appendDescription(connectedMessage.getContentRaw().replace(blacklistedWord, sb.toString()));
                connectedMessage.getTextChannel().sendMessage(eb.build()).queue();
            } else {
                user.openPrivateChannel().complete().sendMessage(":warning: Użyto słowa z czarnej listy!" + System.lineSeparator() + System.lineSeparator() + "Usunięta wiadomość: " + messageContent).queue();
                connectedMessage.getTextChannel().sendMessage(":warning: " + user.getAsMention() + " staraj się nie używać wulgaryzmów! Tym razem tylko upomnienie, ale jeśli w następnych wiadomościach to powtórzysz, __otrzymasz__ ostrzeżenie. \n \n __Wiadomość została manualnie sprawdzona przez moderatora.__").queue();
                EmbedBuilder eb = new EmbedBuilder();
                eb.setColor(AnnouncementUtil.hex2RGB("#4286f4"));
                eb.setAuthor(user.getName(), null, user.getAvatarUrl());
                eb.setTitle("Kopia usuniętej wiadomości:");
                StringBuilder sb = new StringBuilder();
                for(int i = 0; i<blacklistedWord.length(); i++){
                    sb.append("\\*");
                }
                eb.appendDescription(connectedMessage.getContentRaw().replace(blacklistedWord, sb.toString()));
                connectedMessage.getTextChannel().sendMessage(eb.build()).queue();
            }
        } else {
            if (ChannelUtil.isEnglish(textChannel)) {
                user.openPrivateChannel().complete().sendMessage(":warning: Used a blacklisted phrase!" + System.lineSeparator() + System.lineSeparator() + "Deleted message: " + messageContent).queue();
                Warning warningGiven = new Warning(userInvolved, WarnPredefinedReason.BAD_LANGUAGE, DoProgramowaniaBot.jda.getSelfUser());
                user.openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage(":ok_hand: " + HardcodedGuild.DP_GUILD.getGuild().getMemberById(userInvolved.getID()).getUser().getName() + " was given **" + warningGiven.getWarningPoints() + "** warnings for** " +
                        warningGiven.getReason().toLowerCase() + "** by **DP-bot**, which will expire in **" + warningGiven.getPredefinedReason().getDaysExpirationTime() + "** days. They now have " + warningGiven.getUserWarned().getWarnCount() + "/**100** warnings"
            ).queue());
            } else {
                user.openPrivateChannel().complete().sendMessage(":warning: Użyto słowa z czarnej listy!" + System.lineSeparator() + System.lineSeparator() + "Usunięta wiadomość: " + messageContent).queue();
                Warning warningGiven = new Warning(userInvolved, WarnPredefinedReason.BAD_LANGUAGE, DoProgramowaniaBot.jda.getSelfUser());
                user.openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage(":ok_hand: Użytkownikowi " + HardcodedGuild.DP_GUILD.getGuild().getMemberById(userInvolved.getID()).getUser().getName() + " zostało nadane **" + warningGiven.getWarningPoints() + "** ostrzeżeń za** " + warningGiven.getReason().toLowerCase() + "** przez **DP-bot**. Wygasają one za **" + warningGiven.getPredefinedReason().getDaysExpirationTime() + "** dni. Teraz ma " + warningGiven.getUserWarned().getWarnCount() + "/**100** ostrzeżeń").queue());
            }
        }
    }

    public void denyPunishment(User userAccepting){
        if(hadAlreadyDeniedPunishment){
            return;
        }
        hadAlreadyGivenPunishment = false;
        hadAlreadyDeniedPunishment = true;

        prepareToSave();
        JSONSaver.saveOne(this);


        HardcodedTextChannel.LOGS_CHANNEL.getChannel().getMessageById(this.messageID).queue(messageG -> {
            MessageEmbed embed = messageG.getEmbeds().get(0);
            EmbedBuilder eb = new EmbedBuilder(embed);
            eb.appendDescription(System.lineSeparator());
            if(!this.hadAlreadyTakenPunishmentAction) {
                eb.appendDescription("- " + userAccepting.getName() + " nie nadał ostrzeżenia.");
            }else if(this.hadAlreadyTakenPunishmentAction){
                eb.appendDescription("- " + userAccepting.getName() + " nie nadał ostrzeżenia - poprawa poprzedniej decyzji.");
            }
            eb.setColor(AnnouncementUtil.hex2RGB("#32ef21"));
            messageG.editMessage(eb.build()).queue(message -> hadAlreadyTakenPunishmentAction = true);
        });
    }

    public void excludeWord(){
        //TODO
    }

    public void includeWord(){
        //TODO
    }

    public void afterLoading(){
        userInvolved = DPUser.fromID(userID);
        textChannel = DoProgramowaniaBot.jda.getTextChannelById(textChannelID);
        textChannel.getMessageById(connectedMessageID).queue(this::setConnectedMessage);
        reports.put(this.messageID, this);
        userID = null;
        textChannelID = null;
    }

    public void prepareToSave(){
        if(textChannel != null) {
            textChannelID = textChannel.getId();
        }
        if(userInvolved != null) {
            userID = userInvolved.getID();
        }
    }

    public static BlacklistReport getFromMessageID(String messageID) {
        return reports.getOrDefault(messageID, null);
    }

    public static boolean exists(String messageID) {
        return reports.containsKey(messageID);
    }

    private void setConnectedMessage(Message connectedMessage){
        this.connectedMessage = connectedMessage;
    }

    public long getExpireTime() {
        return timeExpires;
    }

    public void setExpireTime(long expires) {
        this.timeExpires = expires;
    }

    public long getArchiveTime() {
        return timeArchives;
    }

    public void setArchiveTime(long archives) {
        this.timeArchives = archives;
    }

    public boolean hasExpired() {
        return hasExpired;
    }

    public void setExpired(boolean hasExpired) {
        if(this.hadAlreadyTakenAction()){
            return;
        }
        this.hasExpired = hasExpired;
        User user = HardcodedGuild.DP_GUILD.getGuild().getMemberById(userInvolved.getID()).getUser();
        if(user == null){
            System.out.print("oh no");
        }
        if(connectedMessage == null) {
           return;
        }
        //TODO REMOVE COMMENT
        /*
        connectedMessage.delete().queue();
        if(ChannelUtil.isEnglish(textChannel)){
            connectedMessage.getTextChannel().sendMessage(":warning: " + user.getAsMention() + "You used a word, which our algorithm recognised as a curse word. For the time being,  moderator couldn't/didn't decide to give or to not give you a warning, so in the result your message containing curse word was deleted.").queue();
            EmbedBuilder eb = new EmbedBuilder();
            eb.setColor(AnnouncementUtil.hex2RGB("#4286f4"));
            eb.setAuthor(user.getName(), null, user.getAvatarUrl());
            eb.setTitle("Copy of the removed message:");
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i < blacklistedWord.length(); i++){
                sb.append("\\*");
            }
            eb.appendDescription(connectedMessage.getContentRaw().replace(blacklistedWord, sb.toString()));
            connectedMessage.getTextChannel().sendMessage(eb.build()).queue();
        }else{
            connectedMessage.getTextChannel().sendMessage(":warning: " + user.getAsMention() + "Użyłeś słowa, który został uznany za nasz algorytm jako wulgaryzm. Żaden moderator nie podjął/nie mógł podjąć jeszcze decyzji o nadaniu tobie ostrzeżenia, więc wiadomość zawierająca wulagryzm została usunięta.").queue();
            EmbedBuilder eb = new EmbedBuilder();
            eb.setColor(AnnouncementUtil.hex2RGB("#4286f4"));
            eb.setAuthor(user.getName(), null, user.getAvatarUrl());
            eb.setTitle("Kopia usuniętej wiadomości:");
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i<blacklistedWord.length(); i++){
                sb.append("\\*");
            }
            eb.appendDescription(connectedMessage.getContentRaw().replace(blacklistedWord, sb.toString()));
            connectedMessage.getTextChannel().sendMessage(eb.build()).queue();
        }*/

        HardcodedTextChannel.LOGS_CHANNEL.getChannel().getMessageById(this.messageID).queue(messageG -> {
            MessageEmbed embed = messageG.getEmbeds().get(0);
            EmbedBuilder eb = new EmbedBuilder(embed);
            eb.appendDescription(System.lineSeparator());
            eb.appendDescription("- raport przedawnił się - wiadomość została usunięta (należy nadal rozpatrzeć kwestię ostrzeżenia)");
            eb.setColor(AnnouncementUtil.hex2RGB("#d60018"));
            messageG.editMessage(eb.build()).queue();
        });
        prepareToSave();
        JSONSaver.saveOne(this);
    }
    public boolean hasArchived(){
        return hasArchived;
    }
    public void setArchived(boolean hasArchived){
        this.hasArchived = hasArchived;
        prepareToSave();
        JSONSaver.saveOne(this);
    }
    public String getConnectedMessageID() {
        return connectedMessageID;
    }

    public boolean hadAlreadyTakenAction(){
        return hadAlreadyGivenPunishment || hadAlreadyDeniedPunishment;
    }

    public TextChannel getTextChannel() {
        return textChannel;
    }

    public void setTextChannel(TextChannel textChannel) {
        this.textChannel = textChannel;
    }

    public boolean hadAlreadyGivenPunishment() {
        return hadAlreadyGivenPunishment;
    }

    public void setAlreadyGivenPunishment(boolean hadAlreadyGivenPunishment) {
        this.hadAlreadyGivenPunishment = hadAlreadyGivenPunishment;
    }
}
