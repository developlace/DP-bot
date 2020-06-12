package pl.aztk.dpbot.commands;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import pl.aztk.dpbot.emote.donator.DonatorEmoji;
import pl.aztk.dpbot.hardcoded.HardcodedGuild;
import pl.aztk.dpbot.hardcoded.HardcodedRole;
import pl.aztk.dpbot.quicksettings.QuickSetting;
import pl.aztk.dpbot.users.donator.Donator;
import pl.aztk.dpbot.users.donator.DonatorLevel;
import pl.aztk.dpbot.util.AnnouncementUtil;
import pl.aztk.dpbot.util.CommandUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Optional;

public class CustomiseCommand implements Command {
    private String name;
    private int requiredVerificationLevel;

    public CustomiseCommand(){
        this.name = "customise";
        this.requiredVerificationLevel = 0;
        CommandUtil.commandList.add(this);
    }

    @Override
    public void execute(String[] args, MessageReceivedEvent event) {
       if(event.getMember().getRoles().contains(HardcodedRole.DONATOR.getRole(event.getJDA()))){
           Donator d = Donator.fromID(event.getAuthor().getId());
           doItsThing(args, event, d.getDonatorLevel());
       } else if(event.getMember().getRoles().contains(HardcodedRole.ELITE.getRole(event.getJDA()))){
           doItsThing(args, event, DonatorLevel.TOP1);
       } else if(event.getMember().getPermissions().contains(Permission.ADMINISTRATOR)){
           doItsThing(args, event, DonatorLevel.TOP1);
       }else{
           event.getChannel().sendMessage(":warning: Żeby korzystać z komendy **dp!customise** wesprzyj nas finansowo i otrzymaj rangę **Donator** Za każde wsparcie bardzo dziękujemy, pomoże ono rozwijać dalej serwer :smiley:").queue();
       }
    }

    public String getName() {
        return this.name;
    }

    @Override
    public int getRequiredVerificationLevel() {
        return this.requiredVerificationLevel;
    }

    //  /customise setColor numb
    //  /customise setColorHex #333333
    //  /customise setName gg
    //  /customise setEmoji name

    private void doItsThing(String[] args, MessageReceivedEvent event, DonatorLevel level) {
        if (args.length == 1) {
            EmbedBuilder eb = new EmbedBuilder();
            eb.appendDescription("**Komendy dostępne dla Ciebie:**");
            eb.appendDescription(System.lineSeparator());
            eb.appendDescription("- dp!customise setColor <numer> (1-");
            if(level == DonatorLevel.NORMAL){
                eb.appendDescription("8)");
            }else{
                eb.appendDescription("16)");
                if(level==DonatorLevel.TOP1){
                    eb.appendDescription("- dp!customise setColorHex <hex>");
                    eb.appendDescription(System.lineSeparator());
                }
            }
            eb.appendDescription(System.lineSeparator());
            eb.appendDescription("- dp!customise clearColor");
            eb.appendDescription(System.lineSeparator());
            eb.appendDescription("- dp!customise setName nick");
            eb.appendDescription(System.lineSeparator());
            eb.appendDescription("- dp!customise clearName");
            eb.appendDescription(System.lineSeparator());
            eb.appendDescription("- dp!customise setEmoji <nazwa> (wpisywanie komendy podczas wrzucania pliku, limit ");
            if(level==DonatorLevel.NORMAL){
                eb.appendDescription("5)");
            }else if(level==DonatorLevel.TOP10){
                eb.appendDescription("10)");
            }else if(level==DonatorLevel.TOP1){
                eb.appendDescription("15)");
            }
            eb.appendDescription(System.lineSeparator());
            eb.appendDescription("- dp!customise emojiList");
            eb.appendDescription(System.lineSeparator());
            eb.appendDescription("- dp!customise deleteEmoji");
            eb.appendDescription(System.lineSeparator());
            eb.appendDescription("- dp!customise limits");
            eb.setColor(event.getMember().getRoles().get(0).getColor());
            event.getChannel().sendMessage(eb.build()).queue();
        }
        if(args.length == 2){
            if(args[1].equalsIgnoreCase("clearColor")){
                for (Role r : event.getMember().getRoles()) {
                    if (r.getName().contains("kolor")) {
                        r.delete().queue();
                        event.getChannel().sendMessage(":ok_hand: Wyczyszczono twój kolor!").queue();
                        return;
                    }
                }
                event.getChannel().sendMessage(":warning: Hmm, wygląda na to, że nie miałeś żadnego koloru!").queue();
            }
            if(args[1].equalsIgnoreCase("clearName")){
                event.getGuild().getController().setNickname(event.getMember(), null).queue();
                event.getTextChannel().sendMessage(":ok_hand:").queue();
            }
            if(args[1].equalsIgnoreCase("emojiList")){
                Donator d = Donator.fromID(event.getMember().getUser().getId());
                StringBuilder sb = new StringBuilder();
                for(DonatorEmoji de:d.getDonatorEmojis()){
                    sb.append("`").append(de.getName()).append("`, ");
                }
                if(sb.length() > 0) {
                    event.getChannel().sendMessage("Twoje emoji to: " + sb.toString()).queue();
                }else{
                    event.getChannel().sendMessage("Nie masz swoich emoji!").queue();
                }

            }
            if(args[1].equalsIgnoreCase("limits")){
                Donator d = Donator.fromID(event.getMember().getUser().getId());
                StringBuilder sb = new StringBuilder();
                if(d.getNextChangeName() <= System.currentTimeMillis()){
                    sb.append("Zmiana nicku: :white_check_mark:");
                    sb.append(System.lineSeparator());
                }else{
                    long between = d.getNextChangeName() - System.currentTimeMillis();
                    long days = (between / (1000* 60* 60* 24));
                    long hours = (between / (1000* 60* 60)) - (days * 24);
                    sb.append("Zmiana nicku: :clock4: za **").append(days).append("** dni i **").append(hours).append("** godzin");
                    sb.append(System.lineSeparator());
                }
                if(d.getNextChangeColor() <= System.currentTimeMillis()){
                    sb.append("Zmiana koloru rangi: :white_check_mark:");
                    sb.append(System.lineSeparator());
                }else{
                    long between = d.getNextChangeColor() - System.currentTimeMillis();
                    long days = (between / (1000* 60* 60* 24));
                    long hours = (between / (1000* 60* 60)) - (days * 24);
                    sb.append("Zmiana koloru rangi: :clock4: za **").append(days).append("** dni i **").append(hours).append("** godzin");
                    sb.append(System.lineSeparator());
                }
                int limit = 5;
                if(d.getDonatorLevel() == DonatorLevel.TOP10){ limit = 10; }
                if(d.getDonatorLevel() == DonatorLevel.TOP1){ limit = 15; }
                int remaining = limit - d.getDonatorEmojis().size();
                sb.append("Customowe emoji: pozostało **").append(remaining).append("**");
                event.getChannel().sendMessage(sb.toString()).queue();

            }
        }
        if (args.length == 3) {
            if (args[1].equalsIgnoreCase("setColor")) {
                if(event.getMember().getRoles().contains(HardcodedRole.DONATOR.getRole(event.getJDA()))){
                    Donator d = Donator.fromID(event.getAuthor().getId());
                    if(d.getNextChangeColor() > System.currentTimeMillis()){
                        long currTime = System.currentTimeMillis();
                        long between = d.getNextChangeColor() - currTime;
                        long days = (between / (1000* 60* 60* 24));
                        long hours = (between / (1000* 60* 60)) - (days * 24);
                        event.getChannel().sendMessage(":warning: Musisz jeszcze odczekać " + days + " dni i "+ hours + " godzin zanim ponownie zmienisz kolor rangi!").queue();
                        return;
                    }
                }
                if (level == DonatorLevel.NORMAL) {
                    if (Integer.parseInt(args[2]) > 8 && Integer.parseInt(args[2]) <= 0) {
                        event.getChannel().sendMessage(":warning: Nie masz dostępu do tego koloru!").queue();
                        return;
                    }
                } else {
                    if (Integer.parseInt(args[2]) > 16 && Integer.parseInt(args[2]) <= 0) {
                        event.getChannel().sendMessage(":warning: Taki kolor nie istnieje!").queue();
                        return;
                    }
                }
                for (Role r : event.getMember().getRoles()) {
                    if (r.getName().contains("kolor")) {
                        r.delete().queue();
                    }
                }
                QuickSetting color = new QuickSetting("color-" + Integer.parseInt(args[2]));
                event.getGuild().getController().createRole().setColor(AnnouncementUtil.hex2RGB(color.getValue()).getRGB()).setName("kolor" + color.getValue()).queue(role ->{
                    event.getGuild().getController().addRolesToMember(event.getMember(), role).queue();
                    event.getGuild().getController().modifyRolePositions().selectPosition(role).moveTo(HardcodedRole.MOD_EN.getRole(event.getJDA()).getPosition() - 1).queue();
                });
                event.getChannel().sendMessage(":ok_hand:").queue();
                if(event.getMember().getRoles().contains(HardcodedRole.DONATOR.getRole(event.getJDA()))) {
                    Donator d = Donator.fromID(event.getAuthor().getId());
                    d.setNextChangeColor(System.currentTimeMillis() + (1728000000 / ((d.getAmountDonated() / 10) + 1)));
                }
            }
            if (args[1].equalsIgnoreCase("setColorHEX")) {
                if (level != DonatorLevel.TOP1) {
                    event.getChannel().sendMessage(":warning: Nie masz dostępu do ustawiania koloru HEX!").queue();
                    return;
                }
                if(event.getMember().getRoles().contains(HardcodedRole.DONATOR.getRole(event.getJDA()))){
                    Donator d = Donator.fromID(event.getAuthor().getId());
                    if(d.getNextChangeColor() > System.currentTimeMillis()){
                        long currTime = System.currentTimeMillis();
                        long between = d.getNextChangeColor() - currTime;
                        long days = (between / (1000* 60* 60* 24));
                        long hours = (between / (1000* 60* 60)) - (days * 24);
                        event.getChannel().sendMessage(":warning: Musisz jeszcze odczekać " + days + " dni i "+ hours + " godzin zanim ponownie zmienisz kolor rangi!").queue();
                        return;
                    }
                }
                for (Role r : event.getMember().getRoles()) {
                    if (r.getName().contains("kolor")) {
                        r.delete().queue();
                    }
                }
                event.getGuild().getController().createRole().setColor(AnnouncementUtil.hex2RGB(args[2]).getRGB()).setName("kolor" + args[2]).queue(role -> {
                    event.getGuild().getController().addRolesToMember(event.getMember(), role).queue();
                    event.getGuild().getController().modifyRolePositions().selectPosition(role).moveTo(HardcodedRole.MOD_EN.getRole(event.getJDA()).getPosition() - 1).queue();
                });
                event.getTextChannel().sendMessage(":ok_hand:").queue();
                if(event.getMember().getRoles().contains(HardcodedRole.DONATOR.getRole(event.getJDA()))) {
                    Donator d = Donator.fromID(event.getAuthor().getId());
                    d.setNextChangeColor(System.currentTimeMillis() + (1728000000 / ((d.getAmountDonated() / 10) + 1)));
                }
            }
            if(args[1].equalsIgnoreCase("setName")){
                if(event.getMember().getRoles().contains(HardcodedRole.DONATOR.getRole(event.getJDA()))){
                    Donator d = Donator.fromID(event.getAuthor().getId());
                    if(System.currentTimeMillis() < d.getNextChangeName()){
                        long currTime = System.currentTimeMillis();
                        long between = d.getNextChangeName() - currTime;
                        long days = (between / (1000* 60* 60* 24));
                        long hours = (between / (1000* 60* 60)) - (days * 24);
                        event.getChannel().sendMessage(":warning: Musisz jeszcze odczekać " + days + " dni i "+ hours + " godzin zanim ponownie zmienisz nick!").queue();
                        return;
                    }
                }
                event.getGuild().getController().setNickname(event.getMember(), args[2]).queue();
                event.getTextChannel().sendMessage(":ok_hand:").queue();
                if(event.getMember().getRoles().contains(HardcodedRole.DONATOR.getRole(event.getJDA()))) {
                    Donator d = Donator.fromID(event.getAuthor().getId());
                    d.setNextChangeName(System.currentTimeMillis() + (1728000000 / ((d.getAmountDonated() / 10) + 1)));
                }
            }
            if(args[1].equalsIgnoreCase("setEmoji")){
                if(event.getMember().getRoles().contains(HardcodedRole.DONATOR.getRole(event.getJDA()))){
                    if(event.getMessage().getAttachments().size() == 1){
                        Donator donator = Donator.fromID(event.getAuthor().getId());
                        for(DonatorEmoji em : donator.getDonatorEmojis()){
                            if(em.getName().equalsIgnoreCase(args[2])){
                                event.getTextChannel().sendMessage(":warning: Twoje emoji o tej nazwie już istnieje!").queue();
                                return;
                            }
                        }
                        if(level == DonatorLevel.NORMAL && donator.getDonatorEmojis().size() == 5){
                            event.getTextChannel().sendMessage(":warning: Wszystkie twoje miejsca na customowe emoji są już zajęte!").queue();
                            return;
                        }
                        if(level == DonatorLevel.TOP10 && donator.getDonatorEmojis().size() == 10){
                            event.getTextChannel().sendMessage(":warning: Wszystkie twoje miejsca na customowe emoji są już zajęte!").queue();
                            return;
                        }
                        if(level == DonatorLevel.TOP1 && donator.getDonatorEmojis().size() == 15){
                            event.getTextChannel().sendMessage(":warning: Wszystkie twoje miejsca na customowe emoji są już zajęte!").queue();
                            return;
                        }
                        String s =  System.currentTimeMillis() + event.getMessage().getAttachments().get(0).getFileName();
                        File f = new File("temp" + File.separator + s);

                        //TODO it creates a json of useremojis even when an error ocurrs
                        //TODO possibly prevent anything other than pics from downloading at all

                        try {
                            if(event.getMessage().getAttachments().get(0).getHeight() > 0 || event.getMessage().getAttachments().get(0).getWidth() > 0) {
                                if (event.getMessage().getAttachments().get(0).download(f)) {
                                    if (ImageIO.read(f) != null) {
                                        BufferedImage bi = ImageIO.read(f);
                                        int newHeight = 64;
                                        double newWidth = (double) newHeight / (double) bi.getHeight() * (double) bi.getWidth();
                                        int newWidthFinal = (int) newWidth;
                                        Image i = bi.getScaledInstance(newWidthFinal, newHeight, Image.SCALE_SMOOTH);
                                        File nf = new File("small_" + f.getName());
                                        ImageIO.write(imageToBufferedImage(i), "png", nf);
                                        MessageBuilder mb = new MessageBuilder();
                                        mb.append(event.getAuthor().getName());
                                        mb.append(" ");
                                        mb.append(args[2]);
                                        DonatorEmoji de = new DonatorEmoji(event.getAuthor().getId());
                                        de.setName(args[2]);
                                        HardcodedGuild.EMOTE_GUILD.getGuild(event.getJDA()).getTextChannels().get(0).sendFile(nf, mb.build()).queue(message -> {
                                            de.setMessageID(message.getId());
                                            de.setOriginalName(event.getMessage().getAttachments().get(0).getFileName());
                                            f.delete();
                                            nf.delete();
                                        });
                                    } else {
                                        f.delete();
                                    }
                                }
                            }
                        }catch(Exception e){
                            f.delete();
                            e.printStackTrace();
                            return;
                        }
                    }else{
                        event.getTextChannel().sendMessage(":warning: Potrzeba jednego obrazka w wiadomości jako załącznik!").queue();
                    }
                }else{
                    event.getTextChannel().sendMessage(":warning: Niestety, tymczasowo funkcja dostępna tylko dla donatorów, ze względu na to, jak to wewnętrznie działa. Elity, nie zapominamy o was, tylko potrzeba trochę za dużo zmian pod maską ;)").queue();
                    return;
                }
            }
            if(args[1].equalsIgnoreCase("deleteEmoji")){
                Donator donator = Donator.fromID(event.getAuthor().getId());
                Optional<DonatorEmoji> toDelete = donator.getDonatorEmojis().stream().filter(de -> de.getName().equalsIgnoreCase(args[2].replace(":", ""))).findFirst();
                if(toDelete.isPresent()){
                    DonatorEmoji de = toDelete.get();
                    donator.getDonatorEmojis().remove(de);
                    HardcodedGuild.EMOTE_GUILD.getGuild(event.getJDA()).getTextChannels().get(0).getMessageById(de.getMessageID()).queue(message -> message.delete().queue(q -> event.getChannel().sendMessage(":ok_hand: ").queue()));
                    de.setMessageID(null);
                    de.setName(null);
                    de.setOriginalName(null);
                }
            }
        }
    }
    private static BufferedImage imageToBufferedImage(Image im) {
        BufferedImage bi = new BufferedImage(im.getWidth(null),im.getHeight(null),BufferedImage.TYPE_INT_RGB);
        Graphics bg = bi.getGraphics();
        bg.drawImage(im, 0, 0, null);
        bg.dispose();
        return bi;
    }
}
