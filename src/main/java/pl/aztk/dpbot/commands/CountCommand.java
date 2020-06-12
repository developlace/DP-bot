package pl.aztk.dpbot.commands;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageHistory;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import pl.aztk.dpbot.hardcoded.HardcodedGuild;
import pl.aztk.dpbot.util.CommandUtil;
import pl.aztk.dpbot.util.Table;
import pl.aztk.dpbot.util.TableTextTooLongException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CountCommand implements Command{

    public CountCommand(){
        CommandUtil.commandList.add(this);
    }
    @Override
    public String getName() {
        return "count";

    }

    @Override
    public int getRequiredVerificationLevel() {
        return 2;
    }

    @Override
    public void execute(String[] args, MessageReceivedEvent event) {
        if(event.getMember().hasPermission(Permission.ADMINISTRATOR)){
            TextChannel lookingFor = HardcodedGuild.DP_GUILD.getGuild(event.getJDA()).getTextChannelById("458592482175746058");
            MessageHistory mHistory = lookingFor.getHistory();
            Map<String, List<String>> wyniki = new HashMap<>();
            for(Message m : mHistory.retrievePast(100).complete()){
                String wynik = m.getContentRaw().toUpperCase().replace(" ", "").replace("-", ":")
                        .replace("PL", "").replace("JP", "").replace("POL", "")
                        .replace(":frowning:", "").replace("\uD83D\uDE26", "");
                if(wyniki.get(wynik) == null){
                    ArrayList<String> arrayList = new ArrayList<>();
                    arrayList.add(m.getAuthor().getName());
                    wyniki.put(wynik, arrayList);
                }else{
                    wyniki.get(wynik).add(m.getAuthor().getName());
                }
            }

            Table wTable = new Table();
            StringBuilder sb = new StringBuilder();
            for(String s : wyniki.keySet()){
                for(String str : wyniki.get(s)){
                    sb.append(str).append(", ");
                }
                wTable.addRow(s, sb.toString());
                sb.setLength(0);
            }
            System.out.println(wyniki.toString());
            try {
                event.getChannel().sendMessage(wTable.build()).queue();
            }catch(TableTextTooLongException e){
                e.printStackTrace();
                System.out.println("długieee " + e.getText().length());
                event.getChannel().sendMessage(e.getText()).queue();
                sendNextTables(event.getTextChannel(), e);
            }
        }
    }
    private void sendNextTables(TextChannel channel, TableTextTooLongException exception){
        try{
            channel.sendMessage(exception.getTable().buildFrom(exception.getCurrentRowNumber())).queue();
        }catch(TableTextTooLongException e){
            channel.sendMessage(e.getText()).queue();
            sendNextTables(channel, e);
        }
    }
}
