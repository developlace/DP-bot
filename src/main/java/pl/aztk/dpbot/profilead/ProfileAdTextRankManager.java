package pl.aztk.dpbot.profilead;

import net.dv8tion.jda.core.entities.Guild;
import pl.aztk.dpbot.quicksettings.QuickSetting;
import pl.aztk.dpbot.util.AnnouncementUtil;

public class ProfileAdTextRankManager {
    public void createRanks(Guild guild){
        for(ProfileAdText profileAdText : ProfileAdText.values()){
            if(new QuickSetting("#role#" + profileAdText.name() + "#roles_amount").getValue() == null){
                String text = profileAdText.getText();
                int length = text.length();
                if(length <= 30){
                    StringBuilder sb = new StringBuilder();
                    for(int i = 1; i<=multiplier(length); i++){
                        sb.append(" 󠀀󠀀");
                    }
                    sb.append(text);
                    guild.getController().createRole().setColor(AnnouncementUtil.hex2RGB("#2f3136")).setName(sb.toString()).setMentionable(false).setHoisted(false).queue(role -> {
                        new QuickSetting("#role#" + profileAdText.name() + "#roles_amount").setValue("1");
                        new QuickSetting("#role#" + profileAdText.name() + "#role_1").setValue(role.getId());
                    });
                }
            }
        }
    }
    private int multiplier(int length){
        if(length < 20){
            return 16;
        }
        if(length < 25){
            return 13;
        }
        if(length < 30){
            return 10;
        }else{
            return 7;
        }
    }

}
