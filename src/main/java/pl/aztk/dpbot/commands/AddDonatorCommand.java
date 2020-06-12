package pl.aztk.dpbot.commands;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import pl.aztk.dpbot.users.DPUser;
import pl.aztk.dpbot.users.donator.Donator;
import pl.aztk.dpbot.util.CommandUtil;

public class AddDonatorCommand implements Command{
    private String name;
    private int requiredVerificationLevel;

    public AddDonatorCommand(){
        this.name = "adddonator";
        this.requiredVerificationLevel = 2;
        CommandUtil.commandList.add(this);
    }

    @Override
    public void execute(String[] args, MessageReceivedEvent event) {
        if(event.getMember().getPermissions().contains(Permission.ADMINISTRATOR)){
            if(args.length == 1){
                event.getTextChannel().sendMessage("Użyie dp!adddonator addNewDonation @wzmianka kwota").queue();
                return;
            }
            //dp!adddonator addNewDonation @wzmianka kwota
            if(args.length == 4){
                if(args[1].equalsIgnoreCase("addnewdonation")){
                    if(event.getMessage().getMentionedUsers().size() == 1){
                        DPUser dpUser = DPUser.fromID(event.getMessage().getMentionedUsers().get(0).getId());
                        Donator donator = Donator.fromID(dpUser.getID());
                        donator.addDonation(Integer.parseInt(args[3]));
                    }
                }
            }
        }
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getRequiredVerificationLevel() {
        return this.requiredVerificationLevel;
    }

}
