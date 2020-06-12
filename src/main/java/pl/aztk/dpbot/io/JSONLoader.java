package pl.aztk.dpbot.io;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import pl.aztk.dpbot.blacklist.BlacklistReport;
import pl.aztk.dpbot.emote.CustomEmoji;
import pl.aztk.dpbot.emote.CustomEmojiPremium;
import pl.aztk.dpbot.emote.donator.DonatorEmoji;
import pl.aztk.dpbot.hardcoded.HardcodedTextChannel;
import pl.aztk.dpbot.punishments.Warning;
import pl.aztk.dpbot.roles.DPRole;
import pl.aztk.dpbot.users.DPUser;
import pl.aztk.dpbot.users.donator.Donator;
import pl.aztk.dpbot.util.EmojiUtil;
import pl.aztk.dpbot.util.WarnUtil;

import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.util.HashSet;

public class JSONLoader {

    public static Gson gson = new Gson();

   public static void load(){
       File currFile = null;
       try {
           File dir = new File("users" + File.separator);

           if (!dir.exists()) {
               dir.mkdirs();
               File tmp = new File(dir, "tmp.json");
               tmp.createNewFile();
               Files.delete(tmp.toPath());
           }
           int i = 0;
           for (File f : dir.listFiles()) {
               currFile = f;
               JsonReader reader = new JsonReader(new FileReader(f.getPath()));
               DPUser u = gson.fromJson(reader, DPUser.class);
               u.setWarnCount(0);
               u.allWarnings = new HashSet<>();
               DPUser.dpUserList.remove(u);
               DPUser.dpUserList.add(u);
           }
           System.out.println(i + " users loaded!");

           dir = new File("warnings" + File.separator);
           if (!dir.exists()) {
               dir.mkdirs();
               File tmp = new File(dir, "tmp.json");
               tmp.createNewFile();
               Files.delete(tmp.toPath());
           }

           for (File f : dir.listFiles()) {
               currFile = f;
               JsonReader reader = new JsonReader(new FileReader(f.getPath()));
               Warning u = gson.fromJson(reader, Warning.class);
               DPUser dpUser = DPUser.fromID(u.getUserWarnedID());
               u.setUserWarned(dpUser);
               dpUser.softAddBan(u);
               if (!u.hasExpired()) {
                   WarnUtil.yetToExpire.add(u);
               }
               //TODO why this is not showing on /warnings correctly
           }
           dir = new File("emotes" + File.separator);
           for (File f : dir.listFiles()) {
               currFile = f;
               JsonReader reader = new JsonReader(new FileReader(f.getPath()));
               if (f.getName().contains("premium")) {
                   CustomEmojiPremium customEmojiPremium = gson.fromJson(reader, CustomEmojiPremium.class);
                   EmojiUtil.customPremiumEmojis.add(customEmojiPremium);
                   customEmojiPremium.setImageFile(new File("sent" + File.separator + customEmojiPremium.getFileName()));
               } else {
                   CustomEmoji customEmoji = gson.fromJson(reader, CustomEmoji.class);
                   EmojiUtil.customEmojis.add(customEmoji);
                   customEmoji.setImageFile(new File("sent" + File.separator + customEmoji.getFileName()));
               }
           }

           //TODO Do something with annotations to do nice I/O, this might become ugly, repeatable and boring shit
           dir = new File("reports" + File.separator);
           if (!dir.exists()) {
               dir.mkdirs();
               File tmp = new File(dir, "tmp.json");
               tmp.createNewFile();
               Files.delete(tmp.toPath());
           }
           for (File f : dir.listFiles()) {
               currFile = f;
               JsonReader reader = new JsonReader(new FileReader(f.getPath()));
               BlacklistReport br = gson.fromJson(reader, BlacklistReport.class);
               if (br.getArchiveTime() > System.currentTimeMillis()) {
                   br.afterLoading();
               } else {
                   br.setArchived(true);
                   br = null;
               }
           }

           dir = new File("donators" + File.separator);
           if (!dir.exists()) {
               dir.mkdirs();
               File tmp = new File(dir, "tmp.json");
               tmp.createNewFile();
               Files.delete(tmp.toPath());
           }
           for (File f : dir.listFiles()) {
               currFile = f;
               JsonReader reader = new JsonReader(new FileReader(f.getPath()));
               Donator d = gson.fromJson(reader, Donator.class);
               d.afterLoading();
           }

           dir = new File("donatorsemojis" + File.separator);
           if (!dir.exists()) {
               dir.mkdirs();
               File tmp = new File(dir, "tmp.json");
               tmp.createNewFile();
               Files.delete(tmp.toPath());
           }
           for (File f : dir.listFiles()) {
               currFile = f;
               JsonReader reader = new JsonReader(new FileReader(f.getPath()));
               DonatorEmoji de = gson.fromJson(reader, DonatorEmoji.class);
               de.afterLoading();
               if((de.getName() == null) || (de.getMessageID() == null) || (de.getOriginalName() == null)){
                   f.delete();
               }
           }

           dir = new File("roles" + File.separator);
           if (!dir.exists()) {
               dir.mkdirs();
               File tmp = new File(dir, "tmp.json");
               tmp.createNewFile();
               Files.delete(tmp.toPath());
           }
           for (File f : dir.listFiles()) {
               currFile = f;
               JsonReader reader = new JsonReader(new FileReader(f.getPath()));
               DPRole role = gson.fromJson(reader, DPRole.class);
               role.afterLoading();
           }
       }catch(Exception e){
           HardcodedTextChannel.ERROR_LOG_CHANNEL.getChannel().sendMessage("Błąd podczas wczytywania " + currFile.getPath()).queue();
           e.printStackTrace();
       }

   }
}
