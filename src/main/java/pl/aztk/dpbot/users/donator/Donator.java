package pl.aztk.dpbot.users.donator;

import com.google.gson.annotations.Expose;
import pl.aztk.dpbot.emote.donator.DonatorEmoji;
import pl.aztk.dpbot.io.JSONSaver;
import pl.aztk.dpbot.users.DPUser;

import java.util.*;

public class Donator {

    public static List<Donator> donatorsList = new ArrayList<>();

    private DPUser dpUser;

    //INFO IO ONLY
    @Expose
    private String userID;
    @Expose
    private int amountDonated;
    @Expose
    private DonatorLevel donatorLevel;
    @Expose
    private long nextChangeColor;
    @Expose
    private long nextChangeName;

    private List<DonatorEmoji> donatorEmojis;

    public Donator(DPUser user, int amountDonated){
        this.dpUser = user;
        this.amountDonated = amountDonated;
        donatorEmojis = new ArrayList<>();
        this.nextChangeColor = System.currentTimeMillis();
        this.nextChangeName = System.currentTimeMillis();
        this.donatorLevel = calculateDonatorLevel();
        donatorsList.add(this);
        JSONSaver.saveOne(this);
    }

    public static Donator fromID(String id) {
        for (Donator d : donatorsList) {
            if (d.getDPUser().getID().equalsIgnoreCase(id)) {
                return d;
            }else if(d.getUserID().equalsIgnoreCase(id)){
                return d;
            }
        }
        return null;
    }

    public int getAmountDonated(){
        return this.amountDonated;
    }


    private DonatorLevel calculateDonatorLevel(){

        //po prostu sprawdzic limity procentowe osob + 1

        int tens = (donatorsList.size() / 10) + 1;

        donatorsList.sort((d1, d2) -> d2.getAmountDonated() - d1.getAmountDonated());
        int top10 = (int) (donatorsList.size() * 0.1);
        if(donatorsList.size() == 0){
            return DonatorLevel.TOP1;
        }

        StringBuilder sb = new StringBuilder();
        int i = 1;
        for(Donator d : donatorsList){
            sb.append("#").append(i).append(": " ).append(d.getAmountDonated()).append(System.lineSeparator());
            i++;
        }

        if(this.amountDonated > donatorsList.get(top10).getAmountDonated()){
            return DonatorLevel.TOP1;
        }else if(this.amountDonated == donatorsList.get(top10).getAmountDonated()){
            int top1_amount = 0;
            for(Donator donator : donatorsList){
                if(donator.getDonatorLevel() == DonatorLevel.TOP1){
                    if(this.amountDonated == donator.getAmountDonated()) {
                        top1_amount++;
                    }
                }
            }
            if(this.getAmountDonated() > (top1_amount + 1) * donatorsList.get(top10 + 1).getAmountDonated()){
                return DonatorLevel.TOP1;
            }
            if(top1_amount < ((int) (donatorsList.size() * 0.1))){
                return DonatorLevel.TOP1;
            }
        }
        int half = (int) (donatorsList.size() * 0.5);
        if(donatorsList.size() == 1){
            return DonatorLevel.TOP10;
        }
        if(this.amountDonated > donatorsList.get(half).getAmountDonated()){
            return  DonatorLevel.TOP10;
        }else if(this.amountDonated == donatorsList.get(half).getAmountDonated()) {
            int top10_amount = 0;
            for (Donator donator : donatorsList) {
                if (donator.getDonatorLevel() == DonatorLevel.TOP10) {
                    top10_amount++;
                }
            }
            if (top10_amount < ((int) (donatorsList.size() * 0.5))) {
                return DonatorLevel.TOP10;
            }
        }
        return DonatorLevel.NORMAL;
    }

    public void addDonation(int newDonation){
        this.amountDonated += newDonation;
        donatorsList.remove(this);
        this.donatorLevel = calculateDonatorLevel();
        donatorsList.add(this);
        JSONSaver.saveOne(this);
    }

    public void prepareToSave(){
        this.userID = dpUser.getID();
    }

    public void afterLoading(){
        this.dpUser = DPUser.fromID(this.userID);
        //this.userID = null;
        this.donatorEmojis = new ArrayList<>();
        donatorsList.add(this);
    }

    public DPUser getDPUser(){
        return this.dpUser;
    }

    public String getUserID(){
        return this.userID;
    }

    public DonatorLevel getDonatorLevel(){
        return this.donatorLevel;
    }

    public long getNextChangeColor() {
        return nextChangeColor;
    }

    public void setNextChangeColor(long nextChangeColor) {
        this.nextChangeColor = nextChangeColor;
        JSONSaver.saveOne(this);
    }

    public long getNextChangeName() {
        return nextChangeName;
    }

    public void setNextChangeName(long nextChangeName) {
        this.nextChangeName = nextChangeName;
        JSONSaver.saveOne(this);
    }

    public List<DonatorEmoji> getDonatorEmojis() {
        return donatorEmojis;
    }

    public void setDonatorEmojis(List<DonatorEmoji> donatorEmojis) {
        this.donatorEmojis = donatorEmojis;
        JSONSaver.saveOne(this);
    }

    public void addDonatorEmojis(DonatorEmoji emoji){
        this.donatorEmojis.add(emoji);
        JSONSaver.saveOne(this);
    }
}
