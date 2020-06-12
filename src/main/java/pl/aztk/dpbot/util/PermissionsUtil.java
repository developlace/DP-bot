package pl.aztk.dpbot.util;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;

public class PermissionsUtil {
    public static boolean hasHigherPermissions(Member previous, Member override){
        if(!previous.getPermissions().contains(Permission.ADMINISTRATOR) && override.getPermissions().contains(Permission.ADMINISTRATOR)){
            return true;
        }
        return !previous.getPermissions().contains(Permission.BAN_MEMBERS) && override.getPermissions().contains(Permission.BAN_MEMBERS);
    }
}
