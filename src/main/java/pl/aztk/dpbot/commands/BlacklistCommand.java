package pl.aztk.dpbot.commands;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import pl.aztk.dpbot.help.HelpResource;
import pl.aztk.dpbot.help.HelpType;
import pl.aztk.dpbot.util.BlacklistUtil;
import pl.aztk.dpbot.util.ChannelUtil;
import pl.aztk.dpbot.util.CommandUtil;
import pl.aztk.dpbot.util.HelpUtil;

public class BlacklistCommand implements Command, HelpResource{

    private String name;

    public BlacklistCommand(){
        this.name = "blacklist";
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
        //dp!blacklist
        //dp!blacklist add <slowo>
        //dp!blacklist remove <number>
        if(event.getMember().getPermissions().contains(Permission.ADMINISTRATOR)){
            if(args.length == 1){
                String message = "";
                StringBuilder sb = new StringBuilder(message);
                for(String s : BlacklistUtil.blacklistedWords){
                    sb.append(s).append(System.lineSeparator());
                }
                if(ChannelUtil.isEnglish(event.getMessage().getTextChannel())){
                    event.getTextChannel().sendMessage("Blacklisted words:").queue();
                }else{
                    event.getTextChannel().sendMessage("Słowa na czarnej liście:").queue();
                }
                event.getTextChannel().sendMessage(sb.toString()).queue();
            }else if(args.length == 3){
                if(args[1].equalsIgnoreCase("add")){
                    event.getMessage().delete().queue();
                    BlacklistUtil.addWord(args[2]);
                    event.getMessage().getTextChannel().sendMessage(":ok_hand:").queue();
                }
                if(args[1].equalsIgnoreCase("remove")){
                    event.getMessage().delete().queue();
                    BlacklistUtil.removeWord(args[2]);
                    event.getMessage().getTextChannel().sendMessage(":ok_hand:").queue();
                }
            }
        }
    }

    @Override
    public String getHelpMessage() {
        return CommandUtil.cmdPrefix + name + " **Opis**: Czarna lista słów **Użycie:** dp!blacklist <add/remove> <slowo>";
    }

    @Override
    public String getEnglishHelpMessage() {
        return  CommandUtil.cmdPrefix + name + " **Description**: Word blacklist **Usage:** dp!blacklist <add/remove> <word>";
    }

    @Override
    public HelpType getHelpResourceType() {
        return HelpType.COMMAND;
    }
}
