import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 时间： 2019年04月04日  23时20分
 * 作者： lcx
 * 备注：
 **/
public class MergeUtil {

//    private static <L, R> Map<String, Map> leftJoin(List<L> leftList,
//                                                    Function<L, String> leftKey,
//                                                    String leftTableName,
//                                                    List<R> rightList,
//                                                    Function<R, String> rightKey,
//                                                    String rightTableName,
//                                                    Map<String, Map> tempMap) {
//        //收集tempMap的key
//        //
//        Map<String, L> leftMap = leftList.stream().collect(Collectors.toMap(leftKey, v -> v));
//        Map<String, R> rightMap = rightList.stream().collect(Collectors.toMap(rightKey, v -> v));
//        Map<String, Map> joinMap = leftList.stream().collect(Collectors.toMap(leftKey, v -> {
//            Map map = new HashMap();
//            map.put(leftTableName, v);
//            map.put(rightTableName, rightMap.get(leftKey.apply(v)));
//            return map;
//        }));
//        return joinMap;
//        //左连接以左表外键为key,右表没有的数据为null
//        //内连接以右表外键为key
//    }

    public static Map<String, Map<String, Object>> leftJoin(Map<String, Map<String, Object>> leftIdTableElementMap,
                                                            Map<String, Map<String, Object>> rightIdTableElementMap) {
        Set<String> leftKeys = leftIdTableElementMap.keySet();
        //  id --> tableName -->  element
        Map<String, Map<String, Object>> idTableElenemtMap = leftKeys.stream().collect(Collectors.toMap(k -> k, v -> {
            Map<String, Object> leftTableElementMap = leftIdTableElementMap.get(v);
            Map<String, Object> rightTableElementMap = rightIdTableElementMap.get(v);
            if (rightTableElementMap != null) {
                leftTableElementMap.putAll(rightTableElementMap);
            }
            return leftTableElementMap;
        }));
        return idTableElenemtMap;
    }

    public static Map<String, Map<String, Object>> innerJoin(Map<String, Map<String, Object>> leftIdTableElementMap,
                                                             Map<String, Map<String, Object>> rightIdTableElementMap) {
        Set<String> leftKeys = leftIdTableElementMap.keySet();
        Set<String> rightKeys = rightIdTableElementMap.keySet();
        //  id --> tableName -->  element
        //比较左右key的大小
        //以小的putall大的
        Map<String, Map<String, Object>> idTableElementMap;
        //找到相同的key
        //以相同的key重组左右map
        Set<String> keys = leftKeys.stream().filter(k -> rightKeys.contains(k)).collect(Collectors.toSet());
        Map<String, Map<String, Object>> newLeftIdTableElementMap = keys.stream().collect(Collectors.toMap(k -> k, v -> leftIdTableElementMap.get(v)));
        Map<String, Map<String, Object>> newRightIdTableElementMap = keys.stream().collect(Collectors.toMap(k -> k, v -> rightIdTableElementMap.get(v)));
        if (leftKeys.size() < rightKeys.size()) {
            idTableElementMap = leftJoin(newLeftIdTableElementMap, newRightIdTableElementMap);
        } else {
            idTableElementMap = leftJoin(newLeftIdTableElementMap, newRightIdTableElementMap);
        }
        return idTableElementMap;
    }


}
