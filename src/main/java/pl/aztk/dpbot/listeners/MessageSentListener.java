package pl.aztk.dpbot.listeners;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import pl.aztk.dpbot.emote.CustomEmojiPremium;
import pl.aztk.dpbot.hardcoded.HardcodedRole;
import pl.aztk.dpbot.hardcoded.HardcodedTextChannel;
import pl.aztk.dpbot.messagechecks.*;
import pl.aztk.dpbot.users.DPUser;
import pl.aztk.dpbot.util.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

public class MessageSentListener extends ListenerAdapter{

    private final BlacklistCheck blacklistCheck = new BlacklistCheck();
    private final BotCheck botCheck = new BotCheck();
    private final CommandCheck commandCheck = new CommandCheck();
    private final DonatorEmojiCheck donatorEmojiCheck = new DonatorEmojiCheck();
    private final RolePingedCheck rolePingedCheck = new RolePingedCheck();

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        try {

            if (botCheck.passed(event)) {
                if (blacklistCheck.passed(event)) {
                    if (commandCheck.passed(event)) {
                        if(!donatorEmojiCheck.passed(event)){
                            rolePingedCheck.passed(event);
                        }
                    }
                }
            }


            if(!event.getAuthor().isBot()) {
                DPUser dpUser = DPUser.fromID(event.getAuthor().getId());
                //dpUser.incrementSentMessageCount();

                if(dpUser != null) {
                    if (dpUser.getSlowMode() > 0) {
                        event.getGuild().getController().addSingleRoleToMember(event.getMember(), HardcodedRole.SLOWMODE.getRole(event.getJDA())).queue(consumer -> {
                                    Timer t = new Timer();
                                    t.schedule(new TimerTask() {
                                        @Override
                                        public void run() {
                                            event.getGuild().getController().removeRolesFromMember(event.getMember(), HardcodedRole.SLOWMODE.getRole(event.getJDA())).queue();
                                        }
                                    }, dpUser.getSlowMode() * 1000);
                                }
                        );
                    }
                }

            }
            //TODO CHANGE LIMIT
            /*if(dpUser.getSentMessageCount() - dpUser.getLastSentAdMessageCount() > 5){
                if(event.getMember().getRoles().contains(event.getGuild().getRoleById("385511316435107840"))){
                    
                }
            }*/


            //donator emojis


            if (event.getMessage().getContentRaw().contains("jahumen") && (event.getMessage().getContentRaw().contains("krul") || event.getMessage().getContentRaw().contains("król"))) {
                event.getChannel().sendMessage(":crown: jahumen :crown:").queue();
            }


            if (EmojiUtil.containsCustomPremiumEmoji(event.getMessage().getContentRaw())) {
                Emote em = EmojiUtil.getEmojiFromText(event.getMessage().getContentRaw(), event.getGuild());
                Role r = event.getGuild().getRolesByName("donator", true).get(0);
                Role v = event.getGuild().getRolesByName("vip", true).get(0);
                if (!event.getMember().getRoles().contains(r) && !event.getMember().getRoles().contains(v) && !event.getMember().getPermissions().contains(Permission.MANAGE_CHANNEL)) {
                    if (event.getMessage().getChannel().getName().contains("en-")) {
                        event.getMember().getUser().openPrivateChannel().complete().sendMessage(EmojiUtil.getPremiumEmoteMessage(em, event.getMessage().getContentRaw(), true).build()).queue();
                    } else {
                        event.getMember().getUser().openPrivateChannel().complete().sendMessage(EmojiUtil.getPremiumEmoteMessage(em, event.getMessage().getContentRaw(), false).build()).queue();
                    }
                    event.getMessage().delete().queue();
                }
            }

            if (event.getMessage().getContentRaw().contains("cdn.discordapp.com/emojis")) {
                event.getMessage().delete().queue();
                for (CustomEmojiPremium emojiPremium : EmojiUtil.customPremiumEmojis) {
                    String s = emojiPremium.getFullName();
                    Emote em = EmojiUtil.getEmojiFromText(s, event.getGuild());
                    if (event.getMessage().getContentRaw().contains(em.getImageUrl())) {
                        Role r = event.getGuild().getRolesByName("donator", true).get(0);
                        Role v = event.getGuild().getRolesByName("vip", true).get(0);
                        if (!event.getMember().getRoles().contains(r) && !event.getMember().getRoles().contains(v) && !event.getMember().getPermissions().contains(Permission.MANAGE_CHANNEL)) {
                            if (event.getMessage().getChannel().getName().contains("en-")) {
                                event.getMember().getUser().openPrivateChannel().complete().sendMessage(EmojiUtil.getPremiumEmoteMessage(em, event.getMessage().getContentRaw(), true).build()).queue();
                            } else {
                                event.getMember().getUser().openPrivateChannel().complete().sendMessage(EmojiUtil.getPremiumEmoteMessage(em, event.getMessage().getContentRaw(), false).build()).queue();
                            }
                        }
                        event.getMessage().delete().queue();
                    }
                }
            }

        } catch (Exception e) {
            try {
                String timeStamp = Long.toString(System.currentTimeMillis());
                File f = new File("ERROR-" + timeStamp + ".txt");
                PrintWriter out = new PrintWriter(f);
                e.printStackTrace(out);
                out.close();

                EmbedBuilder eb = new EmbedBuilder();
                eb.setTitle("DPBOT SIĘ PALI :fire: :fire: :fire:");
                eb.setDescription(StringUtils.abbreviate(ExceptionUtils.getStackTrace(e), 600));
                eb.addField("Kanał", "<#" + event.getChannel().getId() + ">", true);
                eb.addField("Kontekst", "[klik](" + event.getMessage().getJumpUrl() + ")", true);
                eb.addField("Zapisano jako", "ERROR-" + timeStamp + ".txt", false);
                HardcodedTextChannel.ERROR_LOG_CHANNEL.getChannel(event.getJDA()).sendMessage(eb.build()).queue();
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
    }
}
