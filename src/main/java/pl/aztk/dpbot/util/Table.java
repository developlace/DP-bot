package pl.aztk.dpbot.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Table {
    private Map<Integer, List<String>> content;

    public Table(){
        this.content = new HashMap<>();
    }

    public void addRow(String... data){
        content.put(content.size(), Arrays.asList(data));
    }

    public String build(){
        String message = "";
        StringBuilder sB = new StringBuilder(message);
        sB.append("```");
        int[] size = calculateSize();
        int totalSize = totalSize(size);
        totalSize += 3 * content.get(0).size() + 2;
        for(int d = 0; d < totalSize; d++){
            if(isCrossing(d, size, totalSize)){
                sB.append("+");
            }else{
                sB.append("-");
            }
        }
        sB.append(System.lineSeparator());
        int currentRow = 0;
        for(Integer rowNumber : content.keySet()){
            List<String> row = content.get(rowNumber);
            int current = 0;
            for(String s : row){
                sB.append("| ");
                sB.append(s);
                int magicNumber = size[current] - s.length();
                for(int d = 0; d < magicNumber; d++){
                    sB.append(" ");
                }
                sB.append(" ");
                current++;
            }
            sB.append(" |");
            sB.append(System.lineSeparator());
            for(int d = 0; d < totalSize; d++){
                if(isCrossing(d, size, totalSize)){
                    sB.append("+");
                }else{
                    sB.append("-");
                }
            }
            sB.append(System.lineSeparator());
            currentRow++;
            if(sB.length() + (sB.length() / currentRow) > 1900){
                sB.append("```");
                message = sB.toString();
                throw new TableTextTooLongException(currentRow, this, message);
            }
        }
        sB.append("```");
        message = sB.toString();
        return message;
    }

    public String buildFrom(int rowFrom){

        String message = "";
        StringBuilder sB = new StringBuilder(message);
        sB.append("```");
        int[] size = calculateSize();
        int totalSize = totalSize(size);
        totalSize += 3 * content.get(0).size() + 2;
        for(int d = 0; d < totalSize; d++){
            if(isCrossing(d, size, totalSize)){
                sB.append("+");
            }else{
                sB.append("-");
            }
        }
        sB.append(System.lineSeparator());
        int currentRow = rowFrom;
        for(int rowNumber = rowFrom; rowFrom < (content.keySet().size() - 1) ; rowNumber++){
            List<String> row = content.get(rowNumber);

            if(row == null){
                sB.append("```");
                message = sB.toString();
                return message;
            }

            int current = 0;
            for(String s : row){
                sB.append("| ");
                sB.append(s);
                int magicNumber = size[current] - s.length();
                for(int d = 0; d < magicNumber; d++){
                    sB.append(" ");
                }
                sB.append(" ");
                current++;
            }
            sB.append(" |");
            sB.append(System.lineSeparator());
            for(int d = 0; d < totalSize; d++){
                if(isCrossing(d, size, totalSize)){
                    sB.append("+");
                }else{
                    sB.append("-");
                }
            }
            sB.append(System.lineSeparator());
            currentRow++;
            if(sB.length() + (sB.length() / currentRow) > 1900){
                sB.append("```");
                message = sB.toString();
                throw new TableTextTooLongException(currentRow, this, message);
            }
        }
        sB.append("```");
        message = sB.toString();
        return message;
    }

    private int[] calculateSize(){
        //int currentPos = 0;
        int[] size = new int[content.get(0).size()];
        for(int i = 0; i < content.get(0).size(); i++){
            int currentMaxLength = 0;
            for(int x : content.keySet()){
                if(content.get(x).get(i).length() > currentMaxLength){
                    currentMaxLength = content.get(x).get(i).length();
                }
            }
            size[i] = currentMaxLength;
        }
        return size;
    }

    private int totalSize(int[] size){
        int value = 0;
        for(int i : size){
            value += i;
        }
        return value;
    }

    private boolean isCrossing(int position, int[] size, int totalLength){
        if(position == 0){
            return true;
        }
        if(position == totalLength - 1){
            return true;
        }
        if(position == totalLength - 2){
            return false;
        }

        for(int i = 0; i < size.length; i++){
            int temp = 0;
            for(int d = 0; d <= i; d++){
                temp += size[d];
                temp += 3;
            }
            if(position == temp){
                return true;
            }
        }

        return false;
    }
}
