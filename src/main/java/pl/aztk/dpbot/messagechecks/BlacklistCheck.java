package pl.aztk.dpbot.messagechecks;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import pl.aztk.dpbot.DoProgramowaniaBot;
import pl.aztk.dpbot.blacklist.BlacklistReport;
import pl.aztk.dpbot.hardcoded.HardcodedGuild;
import pl.aztk.dpbot.hardcoded.HardcodedTextChannel;
import pl.aztk.dpbot.punishments.WarnPredefinedReason;
import pl.aztk.dpbot.punishments.Warning;
import pl.aztk.dpbot.users.DPUser;
import pl.aztk.dpbot.util.AnnouncementUtil;
import pl.aztk.dpbot.util.BlacklistUtil;
import pl.aztk.dpbot.util.ChannelUtil;

public class BlacklistCheck implements MessageCheck {
    @Override
    public boolean passed(MessageReceivedEvent event) {
        if(event.getMember().getPermissions().contains(Permission.ADMINISTRATOR)){
            return true;
        }else{
            DPUser dpUser = DPUser.fromID(event.getAuthor().getId());
            String[] words = event.getMessage().getContentRaw().replaceAll("[^a-zA-Z\\s]", "").split("\\s+");
            for (String word : words) {
                String wordProcessed = word.toLowerCase().replaceAll("[^a-zA-Z]", "");
                if (BlacklistUtil.getBlacklistedWord(wordProcessed) != null) {
                    String blacklistWord = BlacklistUtil.getBlacklistedWord(wordProcessed);
                    String messageContent = event.getMessage().getContentRaw().replace(word, "**" + word + "**");
                    execute(event, dpUser, blacklistWord, messageContent, wordProcessed, word);
                    return false;
                }
            }
        }

        return true;
    }

    private void execute(MessageReceivedEvent event, DPUser dpUser, String blacklistWord, String messageContent, String wordProcessed, String word) {
        int swearLimit = 35;
        dpUser.incrementSwearMessageCount();
        if (BlacklistUtil.isAutomaticallyDeleted(wordProcessed, blacklistWord)) {
            event.getMessage().delete().queue();
            if(isUnderSwearLimit(dpUser, swearLimit)){
                if (ChannelUtil.isEnglish(event.getTextChannel())) {
                    event.getAuthor().openPrivateChannel().complete().sendMessage(":warning: Used a blacklisted phrase!" + System.lineSeparator() + System.lineSeparator() + "Deleted message: " + messageContent).queue();
                    event.getMessage().getTextChannel().sendMessage(":warning: " + event.getAuthor().getAsMention() + " try not to use bad language! This time without warning points, but if you swear again in next messages, you __will__ be warned.").queue();
                } else {
                    event.getAuthor().openPrivateChannel().complete().sendMessage(":warning: Użyto słowa z czarnej listy!" + System.lineSeparator() + System.lineSeparator() + "Usunięta wiadomość: " + messageContent).queue();
                    event.getMessage().getTextChannel().sendMessage(":warning: " + event.getAuthor().getAsMention() + " staraj się nie używać wulgaryzmów! Tym razem tylko upomnienie, ale jeśli w następnych wiadomościach to powtórzysz, __otrzymasz__ ostrzeżenie").queue();
                }
            } else {
                Warning warningGiven = new Warning(dpUser, WarnPredefinedReason.BAD_LANGUAGE, DoProgramowaniaBot.jda.getSelfUser());
                if (ChannelUtil.isEnglish(event.getTextChannel())) {
                    event.getAuthor().openPrivateChannel().complete().sendMessage(":warning: Used a blacklisted phrase!" + System.lineSeparator() + System.lineSeparator() + "Deleted message: " + messageContent).queue();
                    event.getTextChannel().sendMessage(":ok_hand: " + HardcodedGuild.DP_GUILD.getGuild(event.getJDA()).getMemberById(dpUser.getID()).getUser().getName() + " was given **" + warningGiven.getWarningPoints() + "** warnings for** " +
                    warningGiven.getReason().toLowerCase() + "** by **DP-bot**, which will expire in **" + warningGiven.getPredefinedReason().getDaysExpirationTime() + "** days. They now have " + warningGiven.getUserWarned().getWarnCount() + "/**100** warnings").queue();
                } else {
                    event.getAuthor().openPrivateChannel().complete().sendMessage(":warning: Użyto słowa z czarnej listy!" + System.lineSeparator() + System.lineSeparator() + "Usunięta wiadomość: " + messageContent).queue();
                    event.getTextChannel().sendMessage(":ok_hand: Użytkownikowi " + HardcodedGuild.DP_GUILD.getGuild(event.getJDA()).getMemberById(dpUser.getID()).getUser().getName() + " zostało nadane **" + warningGiven.getWarningPoints() + "** ostrzeżeń za** " +
                    warningGiven.getReason().toLowerCase() + "** przez **DP-bot**. Wygasają one za **" + warningGiven.getPredefinedReason().getDaysExpirationTime() + "** dni. Teraz ma " + warningGiven.getUserWarned().getWarnCount() + "/**100** ostrzeżeń").queue();
                }
            }
            EmbedBuilder eb = new EmbedBuilder();
            eb.setColor(AnnouncementUtil.hex2RGB("#8c8c8c"));
            eb.setTitle("Automatyczne zgłoszenie - użyto niewłaściwego słowa!");
            eb.addField("Kanał", "<#"+ event.getChannel().getId() + ">", true);
            eb.addField("Kontekst", "[klik](" + event.getMessage().getJumpUrl() + ")", true);
            eb.appendDescription(System.lineSeparator());
            eb.appendDescription(messageContent);
            eb.setAuthor(event.getAuthor().getName(), null, event.getAuthor().getAvatarUrl());
            HardcodedTextChannel.LOGS_CHANNEL.getChannel(event.getJDA()).sendMessage(eb.build()).queue();
        }else{
            EmbedBuilder eb = new EmbedBuilder();
            eb.setColor(AnnouncementUtil.hex2RGB("#f48f42"));
            eb.setTitle("Automatyczne zgłoszenie - użyto potencjalnie niewłaściwego słowa!");
            eb.addField("Kanał", "<#"+ event.getChannel().getId() + ">", true);
            eb.addField("Kontekst", "[klik](" + event.getMessage().getJumpUrl() + ")", true);
            eb.appendDescription(System.lineSeparator());
            eb.appendDescription(messageContent);
            eb.appendDescription(System.lineSeparator());
            eb.appendDescription(System.lineSeparator());
            eb.appendDescription("słowo, którego odmiana została użyta: " + blacklistWord);
            eb.appendDescription(System.lineSeparator());
            eb.appendDescription(System.lineSeparator());
            eb.appendDescription("**LOGI:**");
            eb.setAuthor(event.getAuthor().getName(), null, event.getAuthor().getAvatarUrl());
            HardcodedTextChannel.LOGS_CHANNEL.getChannel(event.getJDA()).sendMessage("@here").queue();
            HardcodedTextChannel.LOGS_CHANNEL.getChannel(event.getJDA()).sendMessage(eb.build()).queue(message -> {
                String messageID = message.getId();
                BlacklistReport report = new BlacklistReport(messageID, event.getMessageId(), event.getMessage(), event.getTextChannel(), word, dpUser);
                message.addReaction("\u2705").queue();
                message.addReaction("\u26D4").queue();
                //message.addReaction("\u1F4A3").queue();
                message.addReaction("\u2934").queue();
                message.addReaction("\u2935").queue();
                //TODO change
                                    /*if (event.getGuild().getMembers().stream().filter(member -> !(member.getOnlineStatus().equals(OnlineStatus.OFFLINE))).filter(member -> member.getRoles().contains(DoProgramowaniaBot.modEN) ||
                                            member.getRoles().contains(DoProgramowaniaBot.modPL) || member.getRoles().contains(DoProgramowaniaBot.adminPL) || member.getRoles().contains(DoProgramowaniaBot.adminEN) || member.getRoles().contains(DoProgramowaniaBot.admin)).count() == 0) {
                                        report.setExpired(true);
                                    }*/
            });
        }
    }

    private boolean isUnderSwearLimit(DPUser dpUser, int swearLimit){
        if(Math.abs(dpUser.getSentMessageCount() - dpUser.getLastSwearingWarningMessageNumber()) >= swearLimit) return true;
        return (Math.abs(dpUser.getSentMessageCount() - dpUser.getLastSwearingWarningMessageNumber()) < swearLimit) && dpUser.getLastSwearingWarningMessageNumber() == 0;
    }
}
