package pl.aztk.dpbot.util;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;
import pl.aztk.dpbot.hardcoded.HardcodedGuild;
import pl.aztk.dpbot.users.DPUser;

import java.util.ArrayList;
import java.util.List;

public class UserUtil {
    public static List<DPUser> users = new ArrayList<>();
    public static void firstUserConfig(){
        for(Member m : HardcodedGuild.DP_GUILD.getGuild().getMembers()){
            User u = m.getUser();
            new DPUser(u.getId());
        }
    }
}
