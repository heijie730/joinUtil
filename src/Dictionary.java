import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class Dictionary {
    private Map<Integer, Map<Integer, String>> table = new HashMap<>();

    public String get(Integer rowNo, Integer columnNo) {
        Map<Integer, String> map = this.table.get(columnNo);
        if (map == null) {
            return null;
        }
        return map.get(rowNo);
    }

    public String getOrDefault(Integer rowNo, Integer columnNo, String defaultValue) {
        String v;
        return (((v = get(rowNo, columnNo)) != null))
                ? v
                : defaultValue;
    }

    public void put(Integer rowNo, Integer columnNo, String o) {
        Map<Integer, String> map = this.table.getOrDefault(columnNo, new HashMap<>(2));
        map.put(rowNo, o);
        this.table.put(columnNo, map);
    }


    public void print() {
        //找到最长的列号
        int maxColumnNo = this.table.keySet().stream().mapToInt(x -> x).max().getAsInt();
        Collection<Map<Integer, String>> rowMapList = this.table.values();
        //最长的行号
        int maxRowNo = rowMapList.stream().flatMap(x -> x.keySet().stream()).mapToInt(x -> x).max().getAsInt();
        //每列的长度map
        Map<Integer, Integer> sizeMap = new HashMap(maxColumnNo);
        for (int columnNo = 1; columnNo <= maxColumnNo; columnNo++) {
            int maxColunmSize = 0;
            for (int rowNo = 1; rowNo <= maxRowNo; rowNo++) {
                int valueLength = this.getOrDefault(rowNo, columnNo, "").length();
                if (valueLength > maxColunmSize) {
                    maxColunmSize = valueLength;
                }
            }
            sizeMap.put(columnNo, maxColunmSize);
        }
        //最长行号
        sizeMap.put(0, (maxRowNo + "").length());
        //打印
        String rowString2 = " " + " | ";
        for (int columnNo = 1; columnNo <= maxColumnNo; columnNo++) {
            String s1 = fixString(columnNo + "", sizeMap.get(columnNo));
            rowString2 = rowString2 + s1 + " | ";
        }
        System.out.println(rowString2);
        for (int rowNo = 1; rowNo <= maxRowNo; rowNo++) {
            String rowString = fixString(rowNo + "", sizeMap.get(0)) + " | ";
            for (int columnNo = 1; columnNo <= maxColumnNo; columnNo++) {
                String s = this.getOrDefault(rowNo, columnNo, "");
                String s1 = fixString(s, sizeMap.get(columnNo));
                rowString = rowString + s1 + " | ";
            }
            System.out.println(rowString);
        }
    }

    private String fixString(String s, int size) {
        int length = s.length();
        if (length == size) {
            return s;
        }
        if (length < size) {
            int needAdd = size - length;
            for (int i = 0; i < needAdd; i++) {
                s = s + " ";
            }
            return s;
        }
        if (length > size) {
            return s.substring(0, size);
        }
        throw new RuntimeException("");
    }
}
