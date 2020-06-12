package pl.aztk.dpbot.commands;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import pl.aztk.dpbot.DoProgramowaniaBot;
import pl.aztk.dpbot.users.donator.Donator;
import pl.aztk.dpbot.users.donator.DonatorLevel;
import pl.aztk.dpbot.util.CommandUtil;

public class DonatorInfoCommand implements Command{
    private String name;
    private int requiredVerificationLevel;

    public DonatorInfoCommand(){
        this.name = "donatorinfo";
        this.requiredVerificationLevel = 0;
        CommandUtil.commandList.add(this);
    }

    @Override
    public void execute(String[] args, MessageReceivedEvent event) {
        if(event.getMember().getPermissions().contains(Permission.ADMINISTRATOR)){
            if(args.length == 1){
                event.getTextChannel().sendMessage(":warning: Użyie dp!donatorinfo @wzmianka ").queue();
                return;
            }
            if(args.length == 2){
                if(event.getMessage().getMentionedUsers().size() == 1){
                    try{
                        Donator d = Donator.fromID(event.getMessage().getMentionedUsers().get(0).getId());
                        event.getTextChannel().sendMessage("Donator " + event.getMessage().getMentionedUsers().get(0).getAsMention() + ": kwota " + d.getAmountDonated() + " poziom " + d.getDonatorLevel().toString()).queue();
                    }catch(NullPointerException e){
                        event.getTextChannel().sendMessage(":warning: Ten użytkownik nie ma donatora!").queue();
                    }

                }
            }
        }else{
            if(args.length == 1){
                Donator d = Donator.fromID(event.getMessage().getAuthor().getId());
                event.getTextChannel().sendMessage("Donator " + event.getMessage().getAuthor().getAsMention() + ": kwota " + d.getAmountDonated() + "zł, poziom " + getDonatorNameFromDevName(d.getDonatorLevel())).queue();
            }
            if(args.length == 2){
                if(event.getMessage().getMentionedUsers().size() == 1){
                    try {
                        Donator d = Donator.fromID(event.getMessage().getAuthor().getId());
                        if (d.getDonatorLevel() == DonatorLevel.TOP1) {
                            try {
                                Donator target = Donator.fromID(event.getMessage().getMentionedUsers().get(0).getId());
                                event.getTextChannel().sendMessage("Donator " + event.getMessage().getMentionedUsers().get(0).getAsMention() + ": kwota " + target.getAmountDonated() + "zł, poziom " + getDonatorNameFromDevName(target.getDonatorLevel())).queue();

                            } catch (NullPointerException e) {
                                event.getTextChannel().sendMessage(":warning: Ten użytkownik nie ma donatora!").queue();
                            }
                        } else {
                            event.getTextChannel().sendMessage(":warning: Musisz być mecenasem by sprawdzać innych donatorów!").queue();
                        }
                    } catch (NullPointerException e) {
                        event.getTextChannel().sendMessage(":warning: Nie masz donatora!").queue();
                    }
                }else if(event.getMessage().getMentionedUsers().size() == 0 && args[1].equalsIgnoreCase("topdonators")){
                    int i = 1;
                    StringBuilder sb = new StringBuilder();
                    for(Donator d : Donator.donatorsList){
                        sb.append("#").append(i).append(" ").append(DoProgramowaniaBot.jda.getUserById(d.getDPUser().getID()).getName());
                        sb.append(System.lineSeparator());
                        i++;
                    }
                    event.getTextChannel().sendMessage(sb.toString()).queue();
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

    private String getDonatorNameFromDevName(DonatorLevel dt){
        if(dt == DonatorLevel.TOP1){
            return "Mecenas";
        }else if(dt == DonatorLevel.TOP10){
            return "Patron";
        }else if(dt == DonatorLevel.NORMAL){
            return "Donator";
        }
        return "";
    }

}
