package pl.aztk.dpbot.quicksettings;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class QuickSetting {
    public static Map<String, String> quickSettings = new HashMap<>();

    private String key;

    public QuickSetting(String key){
        this.key = key;
    }

    public void setValue(String value){
        quickSettings.put(this.key, value);
        save(value);
    }

    public String getValue(){
        return quickSettings.get(this.key);
    }

    public static void load() {
        try {
            quickSettings.clear();
            File dir = new File("quicksettings" + File.separator);
            if (dir.listFiles().length > 0) {
                for (File f : dir.listFiles()) {
                    String key = f.getName().replace(".txt", "");
                    String value;
                    BufferedReader fr = new BufferedReader(new FileReader(f.getPath()));
                    value = fr.readLine();
                    fr.close();
                    quickSettings.put(key, value);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void save(String value){
        try {
            File f = new File("quicksettings" + File.separator + this.key + ".txt");
            PrintWriter out = new PrintWriter(f);
            out.println(value);
            out.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
