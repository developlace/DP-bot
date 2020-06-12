package pl.aztk.dpbot.roles;

import com.google.gson.annotations.Expose;
import pl.aztk.dpbot.io.JSONSaver;

import java.util.HashSet;
import java.util.Set;

public class DPRole {

    public static Set<DPRole> rolesSet = new HashSet<>();

    @Expose
    private String roleID;
    @Expose
    private String roleToMentionID;
    @Expose
    private boolean enabled;
    @Expose
    private Set<String> textChannelsMentionable;

    public DPRole(String roleID, String roleToMentionID){
        if(getByWhichever(roleID) == null && getByWhichever(roleToMentionID) == null) {
            this.roleID = roleID;
            this.roleToMentionID = roleToMentionID;
            this.textChannelsMentionable = new HashSet<>();
            rolesSet.add(this);
            JSONSaver.saveOne(this);
        }
    }

    public static DPRole getByRoleID(String roleID){
        for(DPRole role : rolesSet){
            if(role.roleID.equals(roleID)){
                return role;
            }
        }
        return null;
    }

    public static DPRole getByRoleToMentionID(String roleToMentionID){
        for(DPRole role : rolesSet){
            if(role.roleToMentionID.equals(roleToMentionID)){
                return role;
            }
        }
        return null;
    }


    public static DPRole getByWhichever(String roleID){
        for(DPRole role : rolesSet){
            if(role.getRoleID().equals(roleID) || role.getRoleToMentionID().equals(roleID)){
                return role;
            }
        }
        return null;
    }

    public static boolean exists(String roleID){
        for(DPRole role : rolesSet){
            if(role.roleID.equals(roleID)){
                return true;
            }
        }
        return false;
    }

    public static boolean existsByWhichever(String roleID){
        for(DPRole role : rolesSet){
            if(role.getRoleID().equals(roleID) || role.getRoleToMentionID().equals(roleID)){
                return true;
            }
        }
        return false;
    }

    public void afterLoading(){
        rolesSet.add(this);
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        JSONSaver.saveOne(this);
    }

    public void addTextChannelToMentionable(String textChannelID){
        textChannelsMentionable.add(textChannelID);
        JSONSaver.saveOne(this);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getRoleID() {
        return roleID;
    }

    public String getRoleToMentionID() {
        return roleToMentionID;
    }

    public Set<String> getTextChannelsMentionable() {
        return textChannelsMentionable;
    }
}
