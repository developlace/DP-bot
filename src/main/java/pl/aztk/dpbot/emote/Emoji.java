package pl.aztk.dpbot.emote;

import com.google.gson.annotations.Expose;
import pl.aztk.dpbot.io.JSONSaver;

import java.awt.*;
import java.io.File;

public abstract class Emoji {

    @Expose
    protected final String name;
    protected File imageFile;
    @Expose
    private String fileName;
    @Expose
    private String imageUrl;
    @Expose
    protected boolean isActive;

    public Emoji(String name, File imageFile){
        this.name = name;
        this.imageFile = imageFile;
        this.fileName = this.imageFile.getName();
    }

    public String getFullName(){
        return ":" + name + ":";
    }
    public String getRawName(){
        return name;
    }
    public File getImageFile(){
        return this.imageFile;
    }
    public void setImageFile(File imageFile) {
        this.imageFile = imageFile;
        JSONSaver.saveOne(this);
    }
    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
        JSONSaver.saveOne(this);
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        JSONSaver.saveOne(this);
    }
    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

}
