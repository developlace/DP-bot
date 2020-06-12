package pl.aztk.dpbot.hardcoded;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.TextChannel;
import pl.aztk.dpbot.DoProgramowaniaBot;

public enum HardcodedTextChannel {

    //some of these channels also have been archived as a result of recent changes, but i'm not bothered with that

    LOGS_CHANNEL("423948442172391435"),
    ERROR_LOG_CHANNEL("593058422820175911"),
    LANGUAGE_CHANNEL("363384653530267648"),
    LEAVING_CHANNEL("344961085092397068"),
    ANNOUNCEMENT_PL("329341288384036865"),
    ANNOUNCEMENT_EN("362704104306245636"),
    TEST_CHANNEL("385511407460024320");

    private String id;

    HardcodedTextChannel(String id){
        this.id = id;
    }

    public TextChannel getChannel(JDA jda){
        return jda.getTextChannelById(this.id);
    }

    public TextChannel getChannel(){
        return DoProgramowaniaBot.jda.getTextChannelById(this.id);
    }
}
