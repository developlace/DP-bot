package pl.aztk.dpbot;

import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.entities.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import pl.aztk.dpbot.blacklist.BlacklistReport;
import pl.aztk.dpbot.commands.*;
import pl.aztk.dpbot.hardcoded.HardcodedGuild;
import pl.aztk.dpbot.hardcoded.HardcodedRole;
import pl.aztk.dpbot.hardcoded.HardcodedTextChannel;
import pl.aztk.dpbot.io.JSONLoader;
import pl.aztk.dpbot.listeners.*;
import pl.aztk.dpbot.punishments.Warning;
import pl.aztk.dpbot.quicksettings.QuickSetting;
import pl.aztk.dpbot.util.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

public class DoProgramowaniaBot {

    public static JDA jda;

    public static void main(String[] args)
    {
        try {

            BlacklistUtil.loadWords();
            JSONLoader.load();

            QuickSetting.load();
            QuickSetting lmp = new QuickSetting("maxonline");
            UserCountListener.lastMaxPeople = Integer.parseInt(lmp.getValue());

            QuickSetting token = new QuickSetting("token");

            jda = new JDABuilder(AccountType.BOT)
                    .setToken(token.getValue())
                    .addEventListener(new MessageSentListener())
                    .addEventListener(new UserCountListener())
                    .addEventListener(new MessageUpdatedListener())
                    .addEventListener(new ReactionAddListener())
                    .addEventListener(new UserJoinListener())
                    .addEventListener(new UserLeaveListener())
                    .addEventListener(new ReactionRemoveListener())
                     .buildBlocking();

            jda.getPresence().setGame(Game.playing("RIP"));

            new WarnCommand();
            new HelpCommand();
            new OnlineCommand();
            new AnnounceCommand();
            new FileCommand();
            new StatsCommand();
            new WarningsCommand();
            new WarnHistoryCommand();
            new BlacklistCommand();
            new MaintenanceCommand();
            new NoLangCommand();
            new EmojiCommand();
            new BadGuysCommand();
            new CountCommand();
            new CustomiseCommand();
            new QuickSettingCommand();
            new AddDonatorCommand();
            new DonatorInfoCommand();
            new SlowmodeCommand();
            new RoleCommand();

            Iterator<Warning> warningIterator = WarnUtil.yetToExpire.iterator();
            Iterator<Map.Entry<String, BlacklistReport>> blacklistReportIterator = BlacklistReport.reports.entrySet().iterator();

            Timer t = new Timer();
            t.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    while(warningIterator.hasNext()){
                        Warning warning = warningIterator.next();
                        if(System.currentTimeMillis() >= warning.getExpirationDate()){
                            warningIterator.remove();
                            warning.setHasExpired(true);
                            warning.getUserWarned().decreaseWarnCount(warning.getWarningPoints());
                        }
                    }
                }
            },0, 100);


            for(Member m : HardcodedGuild.DP_GUILD.getGuild(jda).getMembers()){
                if(m.getUser().isBot()){
                    continue;
                }
                if(m.getRoles().size() == 0){
                    HardcodedTextChannel.LOGS_CHANNEL.getChannel(jda).sendMessage(m.getUser().getAsMention() + " nie mial usera :/").queue();
                    HardcodedGuild.DP_GUILD.getGuild(jda).getController().addRolesToMember(m, HardcodedRole.USER.getRole(jda)).queue();
                }
                if(!m.getRoles().contains(HardcodedRole.USER.getRole(jda))){
                    HardcodedTextChannel.LOGS_CHANNEL.getChannel(jda).sendMessage(m.getUser().getAsMention() + " nie mial usera :/").queue();
                    HardcodedGuild.DP_GUILD.getGuild(jda).getController().addRolesToMember(m,  HardcodedRole.USER.getRole(jda)).queue();
                }
            }

            t.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    try {
                        while(blacklistReportIterator.hasNext()) {
                            Map.Entry<String, BlacklistReport> entry = blacklistReportIterator.next();
                            BlacklistReport br = entry.getValue();
                            if (System.currentTimeMillis() >= br.getExpireTime()) {
                                br.setExpired(true);
                            }
                            if (System.currentTimeMillis() >= br.getArchiveTime()) {
                                br.setArchived(true);
                                blacklistReportIterator.remove();
                            }
                        }
                    }catch(RuntimeException e){
                        e.printStackTrace();
                    }
                }
            }, 0, 200);

        }catch(Exception e){
            try {
                String timeStamp = Long.toString(System.currentTimeMillis());
                File f = new File("ERROR-" + timeStamp + ".txt");
                PrintWriter out = new PrintWriter(f);
                e.printStackTrace(out);
                out.close();
                EmbedBuilder eb = new EmbedBuilder();
                eb.setTitle("DPBOT SIĘ PALI :fire: :fire: :fire:");
                eb.setDescription(StringUtils.abbreviate(ExceptionUtils.getStackTrace(e), 600));
                eb.addField("Zapisano jako", "ERROR-" + timeStamp + ".txt", false);
                HardcodedTextChannel.ERROR_LOG_CHANNEL.getChannel().sendMessage(eb.build()).queue();
            }catch(FileNotFoundException ex){
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
    }
}