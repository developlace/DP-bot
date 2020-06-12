package pl.aztk.dpbot.listeners;

import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.events.user.update.UserUpdateOnlineStatusEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import pl.aztk.dpbot.quicksettings.QuickSetting;

public class UserCountListener extends ListenerAdapter {
    public static int lastMaxPeople;
    public static int online;
    @Override
    public void onUserUpdateOnlineStatus(UserUpdateOnlineStatusEvent event){
        if(event.getGuild().getId().equals("261551481117736960")){
            online = (int) event.getGuild().getTextChannelById("363384653530267648").getMembers().stream().filter(m -> m.getOnlineStatus() != OnlineStatus.OFFLINE).count();
            if(online > lastMaxPeople){
                lastMaxPeople = online;
                QuickSetting lmp = new QuickSetting("maxonline");
                lmp.setValue(Integer.toString(lastMaxPeople));
                event.getGuild().getTextChannelById("289830927251668994").sendMessage(":warning: Nowy rekord użytkowników online: " + lastMaxPeople + " :tada:").queue();
            }
        }
    }
}
