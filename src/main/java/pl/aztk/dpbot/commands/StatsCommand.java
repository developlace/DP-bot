package pl.aztk.dpbot.commands;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import pl.aztk.dpbot.hardcoded.HardcodedGuild;
import pl.aztk.dpbot.help.HelpResource;
import pl.aztk.dpbot.help.HelpType;
import pl.aztk.dpbot.users.DPUser;
import pl.aztk.dpbot.util.CommandUtil;
import pl.aztk.dpbot.util.HelpUtil;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;

public class StatsCommand  implements Command, HelpResource{
    private String name;

    public StatsCommand(){
        this.name = "stats";
        CommandUtil.commandList.add(this);
        HelpUtil.helpResources.add(this);
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
            DecimalFormat df = new DecimalFormat("#.##");
            df.setRoundingMode(RoundingMode.CEILING);
            boolean sendChannelMessages = true;
            boolean sendUserMessages  = true;
            int providedMaxUserCount = 20;
            boolean isSilent = false;
            if(args[1].equalsIgnoreCase("U")) {
                sendChannelMessages = false;
            }
            if(args[1].equalsIgnoreCase("C")) {
                sendUserMessages = false;
            }
            if(args.length >= 3 && sendUserMessages) {
                providedMaxUserCount = Integer.valueOf(args[2]);
            }
            if(args.length == 4 && args[3].equalsIgnoreCase("silent")) {
                isSilent = true;
            }

            Map<User, Integer> messageCount = new HashMap<>();
            int grandTotal = 0;
            if(!isSilent) event.getTextChannel().sendMessage(":ok_hand:").queue();
            if(isSilent) event.getTextChannel().sendMessage(":ok_hand: shhhhhhhhh").queue();
            long startTime = System.currentTimeMillis();
            for (TextChannel tc : HardcodedGuild.DP_GUILD.getGuild(event.getJDA()).getTextChannels()) {
                int previousCount = 0;
                MessageHistory history = tc.getHistory();
                for (int i = 0; i < 1000; i++) {
                    List<Message> retrievedPast = history.retrievePast(100).complete();
                    int done = retrievedPast.size();
                    previousCount += done;
                    if (sendUserMessages){
                        for (Message m : retrievedPast) {
                            if (messageCount.containsKey(m.getAuthor())) {
                                messageCount.replace(m.getAuthor(), messageCount.get(m.getAuthor()) + 1);
                            } else {
                                messageCount.put(m.getAuthor(), 1);
                            }
                        }
                    }
                    if (i > 0 && history.getRetrievedHistory().size() == previousCount && done == 0) {
                        break;
                    }
                }
                grandTotal += history.getRetrievedHistory().size();
                if(sendChannelMessages) event.getTextChannel().sendMessage("kanał #" + tc.getName() + " wiadomości: " + history.getRetrievedHistory().size()).queue();
            }

            for(User u : messageCount.keySet()){
                DPUser dpUser = DPUser.fromID(u.getId());
                dpUser.setSentMessageCount(messageCount.get(u));
            }

            if(sendChannelMessages) event.getTextChannel().sendMessage("ŁĄCZNIE: **" + grandTotal + "** wiadomości").queue();
            if(sendUserMessages) {
                int timesSent = 0;
                String currentMessage = "";
                StringBuilder sB = new StringBuilder(currentMessage);
                int userGrandTotal = 0;
                //if else dont even bother
                int count = 1;
                for (Map.Entry e : CommandUtil.entriesSortedByValues(messageCount)) {
                    User u = (User) e.getKey();
                    Integer i = (Integer) e.getValue();
                    if(!isSilent){
                        String toJoin = "#" + count + " **" + u.getName() + "**: *" + i.toString() + "* WIADOMOŚCI";
                        if(sB.length() + toJoin.length() >= 2000){
                            event.getTextChannel().sendMessage(sB.toString()).queue();
                            sB.setLength(0);
                            timesSent++;
                        }
                        sB.append(toJoin).append(System.lineSeparator());
                    }
                    if(isSilent) System.out.println("#" + count + " **" + u.getName() + "**: *" + i.toString() + "* WIADOMOŚCI");
                    userGrandTotal += i;
                    count++;
                    if (count > providedMaxUserCount) {
                        System.out.println(sB.toString());
                        event.getTextChannel().sendMessage(sB.toString()).queue();
                        sB.setLength(0);
                        timesSent++;
                        break;
                    }
                }
                if(timesSent == 0){
                    event.getTextChannel().sendMessage(sB.toString()).queue();
                    sB.setLength(0);
                }

                double procent = ((double) userGrandTotal / (double) grandTotal) * 100;
                System.out.println(procent);
                event.getTextChannel().sendMessage("ŁĄCZNIE: **" + userGrandTotal + "** wiadomości dla TOP " + providedMaxUserCount + " (" + df.format(procent)+ "% wiadomości)").queue();
            }

            long elapsed = System.currentTimeMillis() - startTime;
            if (elapsed > 60000) {
                double time = (double) elapsed/60000;
                event.getTextChannel().sendMessage("TRWAŁO TO: **" +  df.format(time) + " minut**").queue();
            } else {
                double time = (double) elapsed/1000;
                event.getTextChannel().sendMessage("TRWAŁO TO: **" + df.format(time)  + " sekund**").queue();
            }
        }
    }

    @Override
    public String getHelpMessage() {
        return CommandUtil.cmdPrefix + name + " **Opis:** Statystyki **Użycie:** dp!stats A<wszystko>/U<użytkownicy>/C<kanały> limit_uzytkownikow <silent - output do konsoli, szybsze>";
    }

    @Override
    public String getEnglishHelpMessage() {
        return CommandUtil.cmdPrefix + name + " **Description:** Stats **Usage:** dp!stats A<all>/U<users>/C<channels> user_limit <silent - console output, faster>";
    }

    @Override
    public HelpType getHelpResourceType() {
        return HelpType.COMMAND;
    }
}
