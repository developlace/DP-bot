package pl.aztk.dpbot.profilead;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import pl.aztk.dpbot.quicksettings.QuickSetting;

public enum ProfileAdText {

    RANK_COLOR1("Chcesz własny kolor rangi?", false),
    RANK_COLOR2("Chcesz taki kolor rangi?", false),
    RANK_COLOR3("Też chcesz zmienić kolor rangi?", false),

    DONATOR_GENERAL1("Zyskaj serwerowy prestiż", false),
    DONATOR_GENERAL2("Zyskaj dodatkowe funkcje", false),
    DONATOR_GENERAL3("Podoba ci się nasza działalność?", false),


    EMOTES_GENERAL1("Chcesz mieć dostęp do zewnętrznych emoji?", false),
    EMOTES_GENERAL2("Chcesz mieć dostęp do emoji premium?", false),
    EMOTES_GENERAL3("Pochwal się swoimi emotko-memami z innymi", false),

    EMOTES_NITRO1("Odblokuj wszystkie możliwości Nitro", false),
    EMOTES_NITRO2("Odblokuj globalne emoji", false),

    EMOTES_FREE("Korzystaj z ulubionych emoji do woli", false),

    FOUND_ANSWER1("Znalazłeś odpowiedź na swoje pytanie?", false),
    FOUND_ANSWER2("Czy znalazłeś tutaj kiedyś pomoc?", false),

    SUPPORT1("Wesprzyj największą społeczność programistów w Polsce", false),
    SUPPORT2("Wspomóż dalszy rozwój naszego serwera", false),

    DONATE_SUPPORT1("Wesprzyj nas dotacją", true),
    DONATE_SUPPORT2("Wesprzyj nas - zostań donatorem", true),
    DONATE_SUPPORT3("Wspomóż nasz rozwój dotacją", true),

    DONATOR_BUY1("Zostań donatorem", true),
    DONATOR_BUY2("Zostań donatorem już dziś", true);

    private boolean bottomText;
    private String text;

    ProfileAdText(String text, boolean bottomText){
       this.text = text;
       this.bottomText = bottomText;
    }

    public String getText(){
        return this.text;
    }

    public boolean isBottomText(){
        return bottomText;
    }

    public boolean isTopText(){
        return !bottomText;
    }

    public void addToMember(Member member, JDA jda){
        QuickSetting quickSettingNOfRoles = new QuickSetting("#role#" + this.name() + "#roles_amount");
        int amount = Integer.parseInt(quickSettingNOfRoles.getValue());
        for(int i = 1; i<= amount; i++){
            Role r = jda.getRoleById(new QuickSetting("#role#" + this.name() + "#role_" + i).getValue());
            member.getGuild().getController().addRolesToMember(member, r).queue();
        }
    }

}
