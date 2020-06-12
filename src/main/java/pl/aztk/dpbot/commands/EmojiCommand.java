package pl.aztk.dpbot.commands;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Icon;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import pl.aztk.dpbot.emote.CustomEmoji;
import pl.aztk.dpbot.emote.CustomEmojiPremium;
import pl.aztk.dpbot.hardcoded.HardcodedGuild;
import pl.aztk.dpbot.util.CommandUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class EmojiCommand implements Command{
    private String name;

    public EmojiCommand(){
        this.name = "emoji";
        CommandUtil.commandList.add(this);
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
            // dp!emoji add thonk thonk.png thonk_premium.png
            // dp!emoji add_premium thonk thonk_premium.png
            // dp!emoji add_standard thonk thonk.png
            // dp!emoji add_premium_noncycle zima zima.png
            if(args.length == 5){
                File standard = new File("sent"  + File.separator + args[3]);
                File premium = new File("sent"  + File.separator + args[4]);
                if(standard.exists() && premium.exists()){
                    try {
                        event.getChannel().sendMessage(":ok_hand:").queue();

                        CustomEmoji customEmoji = new CustomEmoji(args[2], standard);
                        BufferedReader fr = new BufferedReader(new FileReader(standard.getPath() + ".txt"));
                        customEmoji.setImageUrl(fr.readLine());
                        fr.close();
                        customEmoji.setActive(true);

                        CustomEmojiPremium customEmojiPremium = new CustomEmojiPremium(args[2]+"_premium", premium);
                        fr = new BufferedReader(new FileReader(premium.getPath() + ".txt"));
                        customEmojiPremium.setImageUrl(fr.readLine());
                        fr.close();
                        customEmojiPremium.setActive(true);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }else if(args.length == 4) {
                try {
                    if (args[1].equalsIgnoreCase("add_premium")) {
                        File premium = new File("sent" + File.separator + args[3]);
                        CustomEmojiPremium customEmojiPremium = new CustomEmojiPremium(args[2] + "_premium", premium);
                        BufferedReader fr = new BufferedReader(new FileReader(premium.getPath() + ".txt"));
                        customEmojiPremium.setImageUrl(fr.readLine());
                        fr.close();
                        customEmojiPremium.setActive(true);
                    }
                    if (args[1].equalsIgnoreCase("add_standard")) {
                        File standard = new File("sent" + File.separator + args[3]);
                        CustomEmoji customEmoji = new CustomEmoji(args[2], standard);
                        BufferedReader fr = new BufferedReader(new FileReader(standard.getPath() + ".txt"));
                        customEmoji.setImageUrl(fr.readLine());
                        fr.close();
                        customEmoji.setActive(true);
                    }
                    if (args[1].equalsIgnoreCase("add_premium_noncycle")) {
                        File premium = new File("sent" + File.separator + args[3]);
                        CustomEmojiPremium customEmojiPremium = new CustomEmojiPremium(args[2] + "_premium", premium);
                        BufferedReader fr = new BufferedReader(new FileReader(premium.getPath() + ".txt"));
                        customEmojiPremium.setImageUrl(fr.readLine());
                        fr.close();
                        customEmojiPremium.setActive(true);
                    }
                    // dp!emoji add_ranked thonk thonk.png @Donator
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else if(args.length >= 5){
                if(args[1].equalsIgnoreCase("add_ranked") && event.getMessage().getMentionedRoles().size() > 0){
                    File ranked = new File("sent" + File.separator + args[3]);
                    try {
                        Role[] roles = event.getMessage().getMentionedRoles().toArray(new Role[0]);
                        HardcodedGuild.DP_GUILD.getGuild(event.getJDA()).getController().createEmote(args[2], Icon.from(ranked), roles).queue();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }else{
                event.getChannel().sendMessage(":warning: Format: dp!emoji add <name> (bez `::` !) <name of file_standard> <name of file_premium> **albo** dp!emoji add_premium/add_standard/add_premium_noncycle <name> <file>"  + System.lineSeparator() + "" ).queue();
            }
        }
    }
}
