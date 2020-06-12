package pl.aztk.dpbot.messagechecks;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class BotCheck implements MessageCheck {
    @Override
    public boolean passed(MessageReceivedEvent event) {
        return !(event.getAuthor().isBot());
    }
}
