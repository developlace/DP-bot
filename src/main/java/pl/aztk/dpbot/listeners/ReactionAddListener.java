package pl.aztk.dpbot.listeners;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import pl.aztk.dpbot.DoProgramowaniaBot;
import pl.aztk.dpbot.blacklist.BlacklistReport;
import pl.aztk.dpbot.hardcoded.HardcodedGuild;
import pl.aztk.dpbot.hardcoded.HardcodedRole;
import pl.aztk.dpbot.hardcoded.HardcodedTextChannel;
import pl.aztk.dpbot.util.EmojiUtil;
import pl.aztk.dpbot.util.PermissionsUtil;

import java.awt.*;

public class ReactionAddListener extends ListenerAdapter {
    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event){
        if(EmojiUtil.containsCustomPremiumEmoji(":" + event.getReactionEmote().getName() + ":")){
            Emote em = EmojiUtil.getEmojiFromText(":" + event.getReactionEmote().getName() + ":", event.getGuild());
            Role r = event.getGuild().getRolesByName("donator", true).get(0);
            Role v = event.getGuild().getRolesByName("vip", true).get(0);
            if(!event.getMember().getRoles().contains(r) && !event.getMember().getRoles().contains(v) && !event.getMember().getPermissions().contains(Permission.MANAGE_CHANNEL)){
                event.getReaction().removeReaction(event.getUser()).queue();
                EmbedBuilder eb = new EmbedBuilder();
                eb.setThumbnail(em.getImageUrl());
                eb.setTitle("Emoji premium");
                if(event.getReaction().getChannel().getName().contains("en-")){
                    eb.setDescription(":warning: This emoji is only avaible for the patrons!" + System.lineSeparator() + System.lineSeparator() + "Details: https://patronite.pl/doprogramowania");
                }else {
                    eb.setDescription(":warning: To emoji jest dostępne tylko dla patronów!" + System.lineSeparator() + System.lineSeparator() + "Szczegóły: https://patronite.pl/doprogramowania");
                }
                eb.setColor(Color.RED);
                event.getMember().getUser().openPrivateChannel().complete().sendMessage(eb.build()).queue();
                //event.getChannel().sendMessage(":warning: To emoji jest dostępne tylko dla patronów! Więcej szczegółów na kanale: #informacje").queue();
            }
        }
        if(event.getChannel().getId().equals(HardcodedTextChannel.LANGUAGE_CHANNEL.getChannel(event.getJDA()).getId())){
            if(EmojiUtil.unicodeToText(event.getReactionEmote().getName()).equalsIgnoreCase("flag_pl")){
                if(!event.getMember().getRoles().contains(HardcodedRole.PL.getRole(event.getJDA())) && !event.getMember().getRoles().contains(HardcodedRole.PL_EN.getRole(event.getJDA()))){
                    if(event.getMember().getRoles().contains(HardcodedRole.NO_LANG.getRole(event.getJDA()))){
                        HardcodedGuild.DP_GUILD.getGuild(event.getJDA()).getController().removeSingleRoleFromMember(event.getMember(), HardcodedRole.NO_LANG.getRole(event.getJDA())).queue();
                        HardcodedGuild.DP_GUILD.getGuild(event.getJDA()).getController().addSingleRoleToMember(event.getMember(), HardcodedRole.PL.getRole(event.getJDA())).queue();
                    }else if(event.getMember().getRoles().contains(HardcodedRole.EN.getRole(event.getJDA()))){
                        HardcodedGuild.DP_GUILD.getGuild(event.getJDA()).getController().removeSingleRoleFromMember(event.getMember(), HardcodedRole.EN.getRole(event.getJDA())).queue();
                        HardcodedGuild.DP_GUILD.getGuild(event.getJDA()).getController().addSingleRoleToMember(event.getMember(), HardcodedRole.PL_EN.getRole(event.getJDA())).queue();
                    }
                }
            }
            if(EmojiUtil.unicodeToText(event.getReactionEmote().getName()).equalsIgnoreCase("flag_gb")){
                if(!event.getMember().getRoles().contains(HardcodedRole.EN.getRole(event.getJDA())) && !event.getMember().getRoles().contains(HardcodedRole.PL_EN.getRole(event.getJDA()))){
                    if(event.getMember().getRoles().contains(HardcodedRole.NO_LANG.getRole(event.getJDA()))){
                        HardcodedGuild.DP_GUILD.getGuild(event.getJDA()).getController().removeSingleRoleFromMember(event.getMember(), HardcodedRole.NO_LANG.getRole(event.getJDA())).queue();
                        HardcodedGuild.DP_GUILD.getGuild(event.getJDA()).getController().addSingleRoleToMember(event.getMember(), HardcodedRole.EN.getRole(event.getJDA())).queue();
                    }else if(event.getMember().getRoles().contains(HardcodedRole.PL.getRole(event.getJDA()))){
                        HardcodedGuild.DP_GUILD.getGuild(event.getJDA()).getController().removeSingleRoleFromMember(event.getMember(), HardcodedRole.PL.getRole(event.getJDA())).queue();
                        HardcodedGuild.DP_GUILD.getGuild(event.getJDA()).getController().addSingleRoleToMember(event.getMember(), HardcodedRole.PL_EN.getRole(event.getJDA())).queue();
                    }
                }
            }
        }
        if(event.getMember().getUser().equals(DoProgramowaniaBot.jda.getSelfUser())){
            if(BlacklistReport.exists(event.getMessageId())){
                BlacklistReport br = BlacklistReport.getFromMessageID(event.getMessageId());
                if(event.getReactionEmote().getName().equals("\u2705")){
                    /*ReactionAction reactionAction = new ReactionAction(event.getMessageId(), event.getReactionEmote(), () -> {
                        //event.getChannel().sendMessage("kliknales  :white_check_mark:").queue();
                        //br.acceptPunishment(event.getMember().getUser());
                    }, br);
                    br.addReactionAction(reactionAction);*/
                }
            }
        }
        if(BlacklistReport.exists(event.getMessageId()) && event.getChannel().equals(HardcodedTextChannel.LOGS_CHANNEL.getChannel(event.getJDA())) && !(event.getMember().getUser().equals(DoProgramowaniaBot.jda.getSelfUser()))){
            BlacklistReport br = BlacklistReport.getFromMessageID(event.getMessageId());
            switch (event.getReactionEmote().getName()) {
                case "\u2705":
                    //br.denyPunishment(event.getUser());
                    event.getTextChannel().getMessageById(event.getMessageId()).queue(message -> {
                        if (message.getReactions().get(1).getCount() > 1) {
                            message.getReactions().get(1).getUsers().queue(users -> {
                                for (User u : users) {
                                    if (!u.equals(DoProgramowaniaBot.jda.getSelfUser())) {
                                        if (u.equals(event.getUser())) {
                                            message.getReactions().get(1).removeReaction(u).queue();
                                        } else {
                                            if (PermissionsUtil.hasHigherPermissions(event.getGuild().getMemberById(u.getId()), event.getMember())) {
                                                message.getReactions().get(1).removeReaction(u).queue();
                                                br.denyPunishment(event.getUser());
                                            } else {
                                                event.getReaction().removeReaction(event.getUser()).queue();
                                            }
                                        }
                                    }
                                }
                            });
                        } else {
                            br.denyPunishment(event.getUser());
                        }
                    });
                    break;
                case "\u26D4":
                    //br.acceptPunishment(event.getUser());
                    event.getTextChannel().getMessageById(event.getMessageId()).queue(message -> {
                        if (message.getReactions().get(0).getCount() > 1) {
                            message.getReactions().get(0).getUsers().queue(users -> {
                                for (User u : users) {
                                    if (!u.equals(DoProgramowaniaBot.jda.getSelfUser())) {
                                        if (u.equals(event.getUser())) {
                                            message.getReactions().get(0).removeReaction(u).queue();
                                        } else {
                                            if (PermissionsUtil.hasHigherPermissions(event.getGuild().getMemberById(u.getId()), event.getMember())) {
                                                message.getReactions().get(0).removeReaction(u).queue();
                                                br.acceptPunishment(event.getUser());
                                            } else {
                                                event.getReaction().removeReaction(event.getUser()).queue();
                                            }
                                        }
                                    }
                                }
                            });
                        } else {
                            br.acceptPunishment(event.getUser());
                        }
                    });
                    break;
                case "\u2934":
                    //TODO Include in blacklist
                    break;
                case "\u2935":
                    //TODO Exclude in blacklist (include in whitelist)
                    break;
            }
        }
    }
}
