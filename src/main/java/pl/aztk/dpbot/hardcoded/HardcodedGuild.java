package pl.aztk.dpbot.hardcoded;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import pl.aztk.dpbot.DoProgramowaniaBot;

public enum HardcodedGuild {

    DP_GUILD("261551481117736960"),
    EMOTE_GUILD("495964558813429760");

    private String id;

    HardcodedGuild(String id){
        this.id = id;
    }

    public Guild getGuild(JDA jda){
        return jda.getGuildById(this.id);
    }

    public Guild getGuild(){
        return DoProgramowaniaBot.jda.getGuildById(this.id);
    }
}
