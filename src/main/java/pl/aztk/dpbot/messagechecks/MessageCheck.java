package pl.aztk.dpbot.messagechecks;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public interface MessageCheck {
    boolean passed(MessageReceivedEvent event);
}
