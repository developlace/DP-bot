package pl.aztk.dpbot.commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public interface Command {
    String getName();
    int getRequiredVerificationLevel();
    void execute(String[] args, MessageReceivedEvent event);
}
