package pl.aztk.dpbot.commands;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import pl.aztk.dpbot.help.HelpResource;
import pl.aztk.dpbot.help.HelpType;
import pl.aztk.dpbot.users.DPUser;
import pl.aztk.dpbot.util.CommandUtil;
import pl.aztk.dpbot.util.HelpUtil;

public class SlowmodeCommand implements Command, HelpResource {
    private String name;

    public SlowmodeCommand(){
        this.name = "slowmode";
        CommandUtil.commandList.add(this);
        HelpUtil.helpResources.add(this);
    }

    @Override
    public String getName() {
        return this.name; }

    @Override
    public int getRequiredVerificationLevel() {
        return 1;
    }

    @Override
    public void execute(String[] args, MessageReceivedEvent event) {
        if(event.getMember().getPermissions().contains(Permission.MESSAGE_MANAGE)){
            if(event.getMessage().getMentionedUsers().size() == 1){
                if(args.length == 3){
                    int amount = Integer.parseInt(args[2]);
                    DPUser dpUser = DPUser.fromID(event.getMessage().getMentionedMembers().get(0).getUser().getId());
                    dpUser.setSlowMode(amount);
                    event.getChannel().sendMessage(":ok_hand: Slowmode dla **"+ event.getMessage().getMentionedUsers().get(0).getName() + "** ustawiono na " + amount + "s!").queue();
                }
            }else{
                event.getChannel().sendMessage(":warning: Musisz użyć dokładnie jednej wzmianki!").queue();
            }
        }
    }

    @Override
    public String getHelpMessage() {
        return CommandUtil.cmdPrefix + name + " **Opis:** Ograniczenie częstotliwości wysyłania wiadomości **Użycie:** dp!slowmode @wzmianka czas(sekundy)";
    }

    @Override
    public String getEnglishHelpMessage() {
        return CommandUtil.cmdPrefix + name + " **Description:** Slowmode on sending messages **Użycie:** dp!slowmode @mention duration(seconds)";
    }

    @Override
    public HelpType getHelpResourceType() {
        return HelpType.COMMAND;
    }
}
