package pl.aztk.dpbot.commands;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import pl.aztk.dpbot.hardcoded.HardcodedRole;
import pl.aztk.dpbot.help.HelpResource;
import pl.aztk.dpbot.help.HelpType;
import pl.aztk.dpbot.util.CommandUtil;
import pl.aztk.dpbot.util.HelpUtil;

import java.util.stream.Collectors;

public class HelpCommand implements Command, HelpResource {
    private String name;
    private int requiredVerificationLevel;

    //TODO REWORK

    public HelpCommand(){
        this.name = "help";
        this.requiredVerificationLevel = 0;
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
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("**Komendy**" + System.lineSeparator());
        eb.appendDescription(System.lineSeparator());
        eb.setColor(event.getMember().getRoles().get(0).getColor());
        int maxVerificationLevel = 0;
        if(event.getMember().getRoles().contains(HardcodedRole.MOD_EN.getRole(event.getJDA()))|| event.getMember().getRoles().contains(HardcodedRole.MOD_PL.getRole(event.getJDA())) || event.getMember().hasPermission(Permission.MESSAGE_MANAGE)){
            maxVerificationLevel = 1;
        }
        if(event.getMember().getRoles().contains(HardcodedRole.ADMIN.getRole(event.getJDA())) || event.getMember().getRoles().contains(HardcodedRole.ADMIN_EN.getRole(event.getJDA()))|| event.getMember().getRoles().contains(HardcodedRole.ADMIN_PL.getRole(event.getJDA()))){
            maxVerificationLevel = 2;
        }
        for(HelpResource hr : HelpUtil.helpResources.stream().filter(hr -> hr.getHelpResourceType() == HelpType.COMMAND).collect(Collectors.toList())){
            if(hr instanceof Command){
                if(maxVerificationLevel >= ((Command) hr).getRequiredVerificationLevel()){
                    eb.appendDescription(hr.getHelpMessage() + System.lineSeparator());
                }
            }
        }
        event.getTextChannel().sendMessage(eb.build()).queue();
    }

    @Override
    public String getHelpMessage() {
        return CommandUtil.cmdPrefix + name + " **Opis:** To, co wpisałeś - Lista komend";
    }

    @Override
    public String getEnglishHelpMessage() {
        return CommandUtil.cmdPrefix + name + " **Opis:** Command list";
    }

    @Override
    public HelpType getHelpResourceType() {
        return HelpType.COMMAND;
    }
}
