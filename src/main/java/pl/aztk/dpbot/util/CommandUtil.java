package pl.aztk.dpbot.util;

import pl.aztk.dpbot.commands.Command;

import java.util.*;

public class CommandUtil {
    public static List<Command> commandList = new ArrayList<>();
    public static String cmdPrefix = "dp!";
    public static Command getCommand(String name){
        for(Command c : commandList){
            if(c.getName().equals(name)){
                return c;
            }
        }
        return null;
    }
    public static <K,V extends Comparable<? super V>> List<Map.Entry<K, V>> entriesSortedByValues(Map<K,V> map) {
        List<Map.Entry<K,V>> sortedEntries = new ArrayList<>(map.entrySet());
        sortedEntries.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));
        return sortedEntries;
    }

}
