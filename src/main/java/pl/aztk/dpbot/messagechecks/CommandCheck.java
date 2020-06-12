package pl.aztk.dpbot.messagechecks;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.apache.commons.lang3.StringUtils;
import pl.aztk.dpbot.commands.Command;
import pl.aztk.dpbot.util.CommandUtil;

public class CommandCheck implements MessageCheck {
    @Override
    public boolean passed(MessageReceivedEvent event) {
        if (event.getMessage().getContentRaw().startsWith(CommandUtil.cmdPrefix)) {
            String[] args = event.getMessage().getContentRaw().split("\\s+");
            if (CommandUtil.getCommand(StringUtils.replaceOnce(args[0], CommandUtil.cmdPrefix, "")) != null) {
                Command c = CommandUtil.getCommand(StringUtils.replaceOnce(args[0], CommandUtil.cmdPrefix, ""));
                c.execute(args, event);
                return false;
            }
        }else{
            return true;
        }
        return true;
    }
}
