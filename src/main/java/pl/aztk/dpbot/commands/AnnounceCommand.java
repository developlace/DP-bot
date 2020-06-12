package pl.aztk.dpbot.commands;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import pl.aztk.dpbot.hardcoded.HardcodedTextChannel;
import pl.aztk.dpbot.help.HelpResource;
import pl.aztk.dpbot.help.HelpType;
import pl.aztk.dpbot.util.AnnouncementUtil;
import pl.aztk.dpbot.util.CommandUtil;
import pl.aztk.dpbot.util.HelpUtil;

import java.io.*;

public class AnnounceCommand implements Command, HelpResource{
    private String name;
    private int requiredVerificationLevel;

    public AnnounceCommand(){
        this.name = "announce";
        this.requiredVerificationLevel = 2;
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
        try{
            if(event.getMember().getPermissions().contains(Permission.ADMINISTRATOR)) {
                if (args.length >= 6) {
                    String lang = args[1];
                    HardcodedTextChannel selectedTextChannel;
                    if (lang.equalsIgnoreCase("pl")) {
                        selectedTextChannel = HardcodedTextChannel.ANNOUNCEMENT_PL;
                    } else if (lang.equalsIgnoreCase("en")) {
                        selectedTextChannel = HardcodedTextChannel.ANNOUNCEMENT_EN;
                    } else {
                        selectedTextChannel = HardcodedTextChannel.TEST_CHANNEL;
                    }
                    if (args[2].equalsIgnoreCase("true")) {
                        selectedTextChannel.getChannel(event.getJDA()).sendMessage("@everyone").queue();
                    }
                    EmbedBuilder eb = new EmbedBuilder();
                    eb.setColor(AnnouncementUtil.hex2RGB(args[3]));
                    if (args[4].contains("#T#")) {
                        String name = args[4].replace("#T#", "");
                        File f = new File("sent" + File.separator + name + ".txt");
                        BufferedReader fr = new BufferedReader(new FileReader(f.getPath()));
                        eb.setThumbnail(fr.readLine());
                        fr.close();
                    }
                    if (args[4].contains("#I#")) {
                        String name = args[4].replace("#I#", "");
                        File f = new File("sent" + File.separator + name + ".txt");
                        BufferedReader fr = new BufferedReader(new FileReader(f.getPath()));
                        eb.setImage(fr.readLine());
                        fr.close();
                    }
                    if (args[4].contains("#S#")) {
                        String name = args[4].replace("#S#", "");
                        File f = new File("sent" + File.separator + name);
                        selectedTextChannel.getChannel(event.getJDA()).sendFile(f, (Message) null).queue();
                    }
                    StringBuilder content = new StringBuilder();
                    for (int i = 5; i < args.length; i++) {
                        content.append(args[i].replace("#LINE_BREAK#", System.lineSeparator()));
                        if (!args[i].contains("#LINE_BREAK#")) {
                            content.append(" ");
                        }
                    }
                    eb.appendDescription(content.toString());
                    eb.setAuthor(event.getMember().getEffectiveName(), null, event.getMember().getUser().getAvatarUrl());
                    selectedTextChannel.getChannel(event.getJDA()).sendMessage(eb.build()).queue();
                } else {
                    event.getChannel().sendMessage(":warning: Za mało informacji!").queue();
                }
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public String getHelpMessage() {
        return CommandUtil.cmdPrefix + name + " **Opis**: Ogłoszenia" + " **Uzycie**: dp!announce [pl/en] [everyone true/false] [kolor HEX] [treść]...";
    }

    @Override
    public String getEnglishHelpMessage() {
        return CommandUtil.cmdPrefix + name + " **Description**: Announcements" + " **Usage**: dp!announce [pl/en] [everyone true/false] [HEX color] [content]...";
    }

    @Override
    public HelpType getHelpResourceType() {
        return HelpType.COMMAND;
    }
}