package pl.aztk.dpbot.hardcoded;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Role;
import pl.aztk.dpbot.DoProgramowaniaBot;

public enum HardcodedRole{

    //some of these roles have been removed as a result of recent changes, but i'm not bothered with that

    ADMIN("298893980559736843"),
    ADMIN_PL("362702330975748107"),
    ADMIN_EN("362702330975748107"),
    MOD_PL("344783938956427264"),
    MOD_EN("384280401847386113"),
    ELITE("495937843483377665"),
    DONATOR("362595800393580545"),
    VIP("263031184047603723"),
    SLOWMODE("593145768408514560"),
    MAINTENANCE("402522508949192746"),
    USER("321713278466392066"),
    BOT("281512892438675456"),
    PL("363005792162676767"),
    EN("363005848609619968"),
    PL_EN("402578640061202442"),
    NO_LANG("402560956904898600");

    private String id;

    HardcodedRole(String id){
        this.id = id;
    }

    public Role getRole(JDA jda){
        return jda.getRoleById(this.id);
    }

    public Role getRole(){
        return DoProgramowaniaBot.jda.getRoleById(this.id);
    }
}
