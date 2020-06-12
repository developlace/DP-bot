package pl.aztk.dpbot.io;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import pl.aztk.dpbot.blacklist.BlacklistReport;
import pl.aztk.dpbot.emote.CustomEmoji;
import pl.aztk.dpbot.emote.CustomEmojiPremium;
import pl.aztk.dpbot.emote.donator.DonatorEmoji;
import pl.aztk.dpbot.punishments.Warning;
import pl.aztk.dpbot.roles.DPRole;
import pl.aztk.dpbot.users.DPUser;
import pl.aztk.dpbot.users.donator.Donator;
import pl.aztk.dpbot.util.UserUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class JSONSaver{

    public static Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    public static void saveAll() throws IOException{
        for(DPUser u : UserUtil.users){
            String json = gson.toJson(u);
            FileWriter fw = new FileWriter("users" + File.separator + u.getID());
            fw.write(json);
            fw.close();
        }
    }
    public static <T> void saveOne(T t){
        try {
            if (t instanceof DPUser) {
                DPUser u = (DPUser) t;
                String json = gson.toJson(u);
                FileWriter fw = new FileWriter("users" + File.separator + u.getID());
                fw.write(json);
                fw.close();
            }
            if (t instanceof Warning) {
                Warning u = (Warning) t;
                String json = gson.toJson(u);
                FileWriter fw = new FileWriter("warnings" + File.separator + u.getUserWarned().getID() + "#" + u.getExpirationDate());
                fw.write(json);
                fw.close();
            }
            if(t instanceof CustomEmojiPremium){
                CustomEmojiPremium customEmojiPremium = (CustomEmojiPremium) t;
                String json = gson.toJson(customEmojiPremium);
                FileWriter fw = new FileWriter("emotes" + File.separator + customEmojiPremium.getRawName());
                fw.write(json);
                fw.close();
            }
            if(t instanceof CustomEmoji){
                CustomEmoji customEmoji = (CustomEmoji) t;
                String json = gson.toJson(customEmoji);
                FileWriter fw = new FileWriter("emotes" + File.separator + customEmoji.getRawName());
                fw.write(json);
                fw.close();
            }
            if(t instanceof BlacklistReport){
                BlacklistReport report = (BlacklistReport) t;
                report.prepareToSave();
                String json = gson.toJson(report);
                //TODO find a more reasonable way to name report file
                //TODO exclude archived reports
                FileWriter fw = new FileWriter("reports" + File.separator  + report.getConnectedMessageID());
                fw.write(json);
                fw.close();
            }
            if(t instanceof Donator){
                Donator donator = (Donator) t;
                donator.prepareToSave();
                String json = gson.toJson(donator);
                FileWriter fw = new FileWriter("donators" + File.separator  + donator.getUserID());
                fw.write(json);
                fw.close();
            }
            if(t instanceof DonatorEmoji){
                DonatorEmoji de = (DonatorEmoji) t;
                String json = gson.toJson(de);
                FileWriter fw = new FileWriter("donatorsemojis" + File.separator  + de.getMessageID());
                fw.write(json);
                fw.close();
            }
            if(t instanceof DPRole){
                DPRole role = (DPRole) t;
                String json = gson.toJson(role);
                FileWriter fw = new FileWriter("roles" + File.separator  + role.getRoleID());
                fw.write(json);
                fw.close();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
