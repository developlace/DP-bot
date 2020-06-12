package pl.aztk.dpbot.messagechecks;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import pl.aztk.dpbot.emote.donator.DonatorEmoji;
import pl.aztk.dpbot.hardcoded.HardcodedGuild;
import pl.aztk.dpbot.hardcoded.HardcodedRole;
import pl.aztk.dpbot.users.donator.Donator;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DonatorEmojiCheck implements MessageCheck {

    @Override
    public boolean passed(MessageReceivedEvent event) {
        if (event.getMember().getRoles().contains(HardcodedRole.DONATOR.getRole(event.getJDA()))) {
            Donator donator = Donator.fromID(event.getAuthor().getId());
            if (donator.getDonatorEmojis() != null && donator.getDonatorEmojis().size() > 0) {
                if(!event.getMessage().getContentRaw().contains(":")) {
                    return false;
                }

                String[] args = event.getMessage().getContentRaw().split("\\s+");

                List <String> possibleDonatorEmojiString = Arrays.stream(args).filter(s -> s.startsWith(":")).filter(s -> s.endsWith(":")).collect(Collectors.toList());
                if (possibleDonatorEmojiString.size() > 0) {
                    List<DonatorEmoji> possibleDonatorEmoji = donator.getDonatorEmojis().stream().filter(de -> possibleDonatorEmojiString.contains(":" + de.getName() +":")).collect(Collectors.toList());
                        if (possibleDonatorEmoji.size() > 0) {
                        for(DonatorEmoji de : possibleDonatorEmoji) {
                            if (de.getMessageID() != null) {
                                HardcodedGuild.EMOTE_GUILD.getGuild(event.getJDA()).getTextChannels().get(0).getMessageById(de.getMessageID()).queue(message -> {
                                    String s = "c_" + System.currentTimeMillis() + de.getOriginalName();
                                    File f = new File("temp" + File.separator + s);
                                    try {
                                        if (message.getAttachments().get(0).download(f)) {
                                            event.getTextChannel().sendFile(f, (Message) null).queue(mssbs -> f.delete());
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                });
                            }
                        }
                        return true;
                    }else{ return false; }
                }else{ return false; }
            }else{ return false; }
        }else { return false; }
    }
}
