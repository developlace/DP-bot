package pl.aztk.dpbot.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

public class BlacklistUtil {
    public static Set<String> blacklistedWords = new HashSet<>();
    public static void addWord(String word){
        try {
            if (!blacklistedWords.contains(word)) {
                blacklistedWords.add(word);
                File f = new File("config" + File.separator + "blacklist.txt");
                PrintWriter out = new PrintWriter(f);
                for (String s : blacklistedWords) {
                    out.println(s);
                }
                out.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public static void removeWord(String word){
        try {
            if (blacklistedWords.contains(word)) {
                blacklistedWords.remove(word);
                File f = new File("config" + File.separator + "blacklist.txt");
                PrintWriter out = new PrintWriter(f);
                for (String s : blacklistedWords) {
                    out.println(s);
                }
                out.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public static void loadWords(){
        try {
            File f = new File("config" + File.separator + "blacklist.txt");
            if(f.exists()){
                BufferedReader fr = new BufferedReader(new FileReader(f.getPath()));
                String line;
                while((line = fr.readLine()) != null){
                    blacklistedWords.add(line);
                }
                fr.close();
            }else{
                f.createNewFile();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static boolean isAutomaticallyDeleted(String wordUsed, String wordFromBlacklist){
        return wordUsed.equals(wordFromBlacklist);
    }

    public static boolean containsBlacklistedWord(String s){
        if(blacklistedWords.contains(s)){
            return true;
        }else {
            for (String blacklisted : blacklistedWords) {
                if (s.toLowerCase().contains(blacklisted)) {
                    return true;
                }
            }
            return false;
        }
    }

    public static String getBlacklistedWord(String s){
        if(blacklistedWords.contains(s)){
            return s;
        }else {
            for (String blacklisted : blacklistedWords) {
                if (s.toLowerCase().contains(blacklisted)) {
                    return blacklisted;
                }
                if(countsAsABlacklisted(s, blacklisted) != null){
                    return countsAsABlacklisted(s, blacklisted);
                }

            }
            return null;
        }
    }
    private static String countsAsABlacklisted(String s, String blacklisted){
            if (s.contains(blacklisted)) {
                return blacklisted;
            }
            int limit = 2;
            if (blacklisted.length() <= 4) {
                limit = 0;
            } else if (blacklisted.length() <= 10) {
                limit = 2;
            } else {
                limit = 4;
            }
            for (int i = 0; i < blacklisted.length() - limit; i++) {
                try {
                    if (s.toCharArray().length == 0) break;
                    if (i > s.length()) {
                        break;
                    }
                    if (i > blacklisted.length()) {
                        break;
                    }
                    if (s.toCharArray()[i] != blacklisted.toCharArray()[i]) {
                        return null;
                    }
                }catch(IndexOutOfBoundsException e){
                    e.printStackTrace();
                }
            }
            if (s.length() <= 2) {
                return null;
            }
            if (blacklisted.length() <= 5) {
                if (!(s.length() > blacklisted.length())) {
                    return null;
                }
            }
            return blacklisted;
    }
}
