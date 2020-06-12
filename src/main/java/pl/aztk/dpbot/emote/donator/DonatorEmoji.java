package pl.aztk.dpbot.emote.donator;

import com.google.gson.annotations.Expose;
import pl.aztk.dpbot.io.JSONSaver;
import pl.aztk.dpbot.users.donator.Donator;

public class DonatorEmoji {
    @Expose
    private String donatorID;
    @Expose
    private String name;
    @Expose
    private String messageID;
    @Expose
    private String originalName;

    public DonatorEmoji(String donatorID){
        this.donatorID = donatorID;
        Donator.fromID(donatorID).addDonatorEmojis(this);
        JSONSaver.saveOne(this);
    }

    public void afterLoading() {
        Donator.fromID(donatorID).addDonatorEmojis(this);
    }

    public String getDonatorID() {
        return donatorID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.replaceAll("[^a-zA-Z\\s]", "");
        JSONSaver.saveOne(this);
    }

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
        JSONSaver.saveOne(this);
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
        JSONSaver.saveOne(this);
    }
}
