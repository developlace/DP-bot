package pl.aztk.dpbot.commands;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import pl.aztk.dpbot.help.HelpResource;
import pl.aztk.dpbot.help.HelpType;
import pl.aztk.dpbot.util.CommandUtil;
import pl.aztk.dpbot.util.HelpUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class FileCommand implements Command, HelpResource {
    private String name;
    private int requiredVerificationLevel;

    public FileCommand(){
        this.name = "file";
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
        try {
            if (event.getMember().getPermissions().contains(Permission.ADMINISTRATOR)) {
                if (event.getMessage().getAttachments().size() > 0) {
                    if (args.length == 2) {
                        File f = new File("sent"  + File.separator + args[1]);
                        if(event.getMessage().getAttachments().get(0).download(f)) {
                            f = new File( "sent"  + File.separator + args[1] + ".txt");
                            PrintWriter out = new PrintWriter(f);
                            out.print(event.getMessage().getAttachments().get(0).getUrl());
                            out.close();
                            event.getMessage().getTextChannel().sendMessage(":ok_hand: Saved as: " + args[1] + "; saved url as: " + args[1] + ".txt").queue();
                        }
                    }
                } else {
                    event.getMessage().getTextChannel().sendMessage(":warning: Didn't send an attachement!").queue();
                }
            }
        }catch(Exception e){
            try {
                String timeStamp = Long.toString(System.currentTimeMillis());
                File f = new File("ERROR-" + timeStamp + ".txt");
                PrintWriter out = new PrintWriter(f);
                e.printStackTrace(out);
                out.close();
            }catch(FileNotFoundException ex){
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    @Override
    public String getHelpMessage() {
        return CommandUtil.cmdPrefix + name + " **Opis**: Wrzucanie plikow";
    }

    @Override
    public String getEnglishHelpMessage() {
        return CommandUtil.cmdPrefix + name + " **Description**: Uploading files";
    }

    @Override
    public HelpType getHelpResourceType() {
        return HelpType.COMMAND;
    }
}

