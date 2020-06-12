package pl.aztk.dpbot.users;

import com.google.gson.annotations.Expose;
import pl.aztk.dpbot.io.JSONSaver;
import pl.aztk.dpbot.punishments.Warning;
import pl.aztk.dpbot.users.donator.Donator;
import pl.aztk.dpbot.util.UserUtil;

import java.util.HashSet;
import java.util.Set;

public class DPUser {

    public static Set<DPUser> dpUserList = new HashSet<>();

    @Expose
    private final String ID;
    @Expose
    private int warnCount;
    @Expose
    private int sentMessageCount;
    @Expose
    private int swearMessageCount;
    @Expose
    private int lastSwearingWarningMessageNumber;
    @Expose
    private int slowMode;

    public Set<Warning> allWarnings;

    public DPUser(String ID) {
        this.ID = ID;
        this.slowMode = 0;
        UserUtil.users.add(this);
        allWarnings = new HashSet<>();
        dpUserList.add(this);
        JSONSaver.saveOne(this);
    }

    public static DPUser fromID(String id) {
        for (DPUser user : dpUserList) {
            if (user.getID().equalsIgnoreCase(id)) {
                return user;
            }
        }
        return null;
    }

    public static boolean exists(String id) {
        for (DPUser user : dpUserList) {
            if (user.getID().equals(id)) {
                return true;
            }
        }
        return false;
    }

    public void addWarn(Warning warning) {
        if (allWarnings == null) {
            allWarnings = new HashSet<>();
            allWarnings.add(warning);
        }
        allWarnings.add(warning);
        this.warnCount += warning.getWarningPoints();
        JSONSaver.saveOne(this);
    }
    public void softAddBan(Warning warning) {
        DPUser.dpUserList.remove(this);
        allWarnings.add(warning);
        DPUser.dpUserList.add(this);
        if(!warning.hasExpired()) {
            this.warnCount += warning.getWarningPoints();
        }
        JSONSaver.saveOne(this);
    }

    public void decreaseWarnCount(int number) {
        this.warnCount -= number;
        JSONSaver.saveOne(this);
    }

    @Deprecated
    public boolean isDonator(){
        for(Donator d : Donator.donatorsList){
            if(d.getDPUser().equals(this)){
                return true;
            }
        }
        return false;
    }

    public boolean isFreshman(){
        return this.sentMessageCount <= 50;
    }

    public int getSentMessageCount() {
        return sentMessageCount;
    }

    public void setWarnCount(int warnCount){
        this.warnCount = warnCount;
    }

    public void setSentMessageCount(int sentMessageCount){
        this.sentMessageCount = sentMessageCount;
        JSONSaver.saveOne(this);
    }

    public void incrementSentMessageCount() {
        this.sentMessageCount++;
        JSONSaver.saveOne(this);
    }

    public void incrementSwearMessageCount() {
        this.swearMessageCount++;
        JSONSaver.saveOne(this);
    }

    public int getSwearMessageCount(){
        return this.swearMessageCount;
    }
    public int getWarnCount() {
        return this.warnCount;
    }

    public Set<Warning> getAllWarnings() {
        return this.allWarnings;
    }

    public String getID() {
        return this.ID;
    }

    public int getLastSwearingWarningMessageNumber() {
        return lastSwearingWarningMessageNumber;
    }

    public void setLastSwearingWarningMessageNumber(int lastSwearingWarningMessageNumber) {
        JSONSaver.saveOne(this);
        this.lastSwearingWarningMessageNumber = lastSwearingWarningMessageNumber;
    }

    public void setAllWarnings(Set<Warning> set) {
        this.allWarnings = set;
    }

    public int getSlowMode() {
        return slowMode;
    }

    public void setSlowMode(int slowMode) {
        this.slowMode = slowMode;
        JSONSaver.saveOne(this);
    }
}