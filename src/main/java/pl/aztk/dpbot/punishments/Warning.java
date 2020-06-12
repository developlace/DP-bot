package pl.aztk.dpbot.punishments;

import com.google.gson.annotations.Expose;
import net.dv8tion.jda.core.entities.User;
import pl.aztk.dpbot.io.JSONSaver;
import pl.aztk.dpbot.users.DPUser;
import pl.aztk.dpbot.util.WarnUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

public class Warning {

    @Expose
    private final String userWarnedID;
    @Expose
    private final String timeGivenUTC;
    @Expose
    private final WarnPredefinedReason predefinedReason;
    @Expose
    private final String givenBy;
    @Expose
    private int warningPoints;
    @Expose
    private String reason;
    @Expose
    private long expirationDate;
    @Expose
    private boolean hasExpired;

    private DPUser userWarned;

    public Warning(DPUser userWarned, WarnPredefinedReason predefinedReason, User givenBy){
        if(predefinedReason == WarnPredefinedReason.CUSTOM) {
            throw new IllegalArgumentException("For custom reason, use other constructor!");
        }
        this.userWarned = userWarned;
        this.userWarnedID = userWarned.getID();
        this.predefinedReason = predefinedReason;
        this.timeGivenUTC = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).withLocale(Locale.getDefault()).format(LocalDateTime.now());
        this.warningPoints = predefinedReason.getWarnPoints();
        this.reason = predefinedReason.name();
        this.expirationDate = System.currentTimeMillis() + ((long) predefinedReason.getDaysExpirationTime() * (long) 86400000);
        if(predefinedReason == WarnPredefinedReason.TEST){
            this.expirationDate = System.currentTimeMillis() + (long) 300000;
        }
        this.givenBy = givenBy.getName();
        this.hasExpired = false;
        userWarned.addWarn(this);
        JSONSaver.saveOne(this);
        WarnUtil.yetToExpire.add(this);
    }

    public Warning(DPUser userWarned, WarnPredefinedReason predefinedReason, int warningPoints, int expirationDate, String reason, User givenBy){
        if(predefinedReason != WarnPredefinedReason.CUSTOM){
            throw new IllegalArgumentException("For non-custom reasons, use other constructor!");
        }
        this.userWarned = userWarned;
        this.userWarnedID = userWarned.getID();
        this.predefinedReason = predefinedReason;
        this.timeGivenUTC = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).withLocale(Locale.getDefault()).format(LocalDateTime.now());
        this.warningPoints = warningPoints;
        this.expirationDate = System.currentTimeMillis() + ((long) expirationDate * (long) 86400000);
        this.reason = reason;
        this.hasExpired = false;
        this.givenBy = givenBy.getName();
        userWarned.addWarn(this);
        JSONSaver.saveOne(this);
        WarnUtil.yetToExpire.add(this);
    }

    public DPUser getUserWarned() {
        return userWarned;
    }

    public void setUserWarned(DPUser user){
        this.userWarned = user;
    }

    public String getUserWarnedID() {
        return userWarnedID;
    }

    public String getTimeGivenUTC() {
        return timeGivenUTC;
    }

    public WarnPredefinedReason getPredefinedReason() {
        return predefinedReason;
    }

    public int getWarningPoints() {
        return warningPoints;
    }

    public String getReason() {
        return reason;
    }

    public long getExpirationDate() {
        return expirationDate;
    }

    public String getGivenBy() {
        return givenBy;
    }

    public boolean hasExpired() {
        return hasExpired;
    }

    public void setHasExpired(boolean hasExpired) {
        this.hasExpired = hasExpired;
        JSONSaver.saveOne(this);
    }
}
