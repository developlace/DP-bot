package pl.aztk.dpbot.commands;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import pl.aztk.dpbot.help.HelpResource;
import pl.aztk.dpbot.help.HelpType;
import pl.aztk.dpbot.roles.DPRole;
import pl.aztk.dpbot.util.AnnouncementUtil;
import pl.aztk.dpbot.util.CommandUtil;

public class RoleCommand implements Command, HelpResource {

    private String name;

    public RoleCommand(){
        this.name = "role";
        CommandUtil.commandList.add(this);
    }

    @Override
    public void execute(String[] args, MessageReceivedEvent event) {
        //dp!role @wzmianka setRestricted true
        //dp!role @wzmianka addChannel #channel
        if(args.length == 4 && event.getMember().getPermissions().contains(Permission.ADMINISTRATOR)){
            if(event.getMessage().getMentionedRoles().size() == 1){
                Role r = event.getMessage().getMentionedRoles().get(0);
                if(args[2].equalsIgnoreCase("setRestricted")){
                    if (args[3].equalsIgnoreCase("true")) {
                        if(!DPRole.existsByWhichever(r.getId())){
                            r.getManager().setMentionable(false).queue();
                            event.getGuild().getController().createRole().setName(r.getName()).setMentionable(true).setColor(AnnouncementUtil.hex2RGB("#bbfaea")).queue(createdRole -> {
                                DPRole dpRole = new DPRole(createdRole.getId(), r.getId());
                                dpRole.setEnabled(true);
                                event.getTextChannel().sendMessage(":ok_hand: Stworzono nową rolę " + createdRole.getAsMention() + " do onzaczania rangi " + r.getAsMention() + "!").queue();
                            });
                        }else{
                            DPRole role = DPRole.getByWhichever(r.getId());
                            if(!role.isEnabled()){
                                role.setEnabled(true);
                                event.getTextChannel().sendMessage(":ok_hand: Właczono ograniczenia w oznaczaniu rangi " + r.getName()).queue();
                            }else{
                                event.getTextChannel().sendMessage(":warning: Ograniczenia w oznaczaniu rangi " + r.getName() + " już są włączone!").queue();
                            }
                        }
                    }
                    //TODO add false
                }else if(args[2].equalsIgnoreCase("addChannel")){
                    if(event.getMessage().getMentionedChannels().size() == 1){
                        if(DPRole.existsByWhichever(r.getId())){
                            TextChannel tc = event.getMessage().getMentionedChannels().get(0);
                            DPRole role = DPRole.getByWhichever(r.getId());
                            role.addTextChannelToMentionable(tc.getId());
                            event.getTextChannel().sendMessage(":ok_hand: Dodano kanał " + tc.getAsMention() + " do kanałów do oznaczania rangi " + r.getAsMention()).queue();
                        }else{
                            event.getTextChannel().sendMessage(":warning: Podana ograniczona ranga nie istnieje! Utwórz ją za pomocą komendy **dp!role " + r.getAsMention() + " setRestricted true**").queue();
                        }
                    }else{
                        event.getTextChannel().sendMessage(":warning: Musisz oznaczyć dokładnie __jeden__ kanał!").queue();
                    }
                }
            }else{
                event.getTextChannel().sendMessage(":warning: Musisz oznaczyć dokładnie __jedną__ rolę!").queue();
            }
        }

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
    public String getHelpMessage() {
        return null;
    }

    @Override
    public String getEnglishHelpMessage() {
        return null;
    }

    @Override
    public HelpType getHelpResourceType() {
        return null;
    }
}
