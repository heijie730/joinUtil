import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 时间： 2019年04月05日  10时04分
 * 作者： lcx
 * 备注：
 **/
public class Join<L, R, J> {

    //join的中介map
    private Map<String, Map<String, Object>> joinResMap;
    //join的结果amp
    private Map<String, Map<String, Object>> newLeftMap;

    //join的结果list
    //每join完一次要赋值
    public List<Map<String, Object>> resList;

    public Join leftJoin(String lastJoinTableName,
                         Function<Object, String> lastJoinKeyFunction,
                         Function<List<String>, List<R>> rightListMappingFunction,
                         Function<R, String> rightKeyFunction,
                         String rightTableName) {
        return join(lastJoinTableName, lastJoinKeyFunction, rightListMappingFunction, rightKeyFunction, rightTableName, false);
    }

    public Join join(String lastJoinTableName,
                     Function<Object, String> lastJoinKeyFunction,
                     Function<List<String>, List<R>> rightListMappingFunction,
                     Function<R, String> rightKeyFunction,
                     String rightTableName,
                     boolean innerJoin) {
        //重组joinResMap的key
        Map<String, Map<String, Object>> newResMap = reBuildLeftJoinMap(lastJoinTableName, this.joinResMap, lastJoinKeyFunction);
        List<String> keyList = newResMap.keySet().stream().collect(Collectors.toList());
        System.out.println("[leftKeys]="+keyList.toString());
        List<R> rightList = rightListMappingFunction.apply(keyList);
        return join(newResMap, rightList, rightKeyFunction, rightTableName, innerJoin);
    }

    public Join innerJoin(String lastJoinTableName,
                          Function<Object, String> lastJoinKeyFunction,
                          Function<List<String>, List<R>> rightListMappingFunction,
                          Function<R, String> rightKeyFunction,
                          String rightTableName) {
        Join join = leftJoin(lastJoinTableName, lastJoinKeyFunction, rightListMappingFunction, rightKeyFunction, rightTableName);
        int max = this.resList.stream().mapToInt(map -> map.size()).max().orElse(0);
        List<Map<String, Object>> collect = resList.stream().filter(map -> map.size() == max).collect(Collectors.toList());
        this.resList = collect;
        return join;

    }

    public Join join(Map<String, Map<String, Object>> newResMap,
                     List<R> rightList,
                     Function<R, String> rightKeyFunction,
                     String rightTableName,
                     boolean innerJoin) {
        //重组joinResMap的key
        Map<String, Map<String, Object>> rightIdTableElementMap = buildMap(rightList, rightKeyFunction, rightTableName);
        leftPutAllRight(this.newLeftMap, rightIdTableElementMap, innerJoin);
        return join(newResMap, rightIdTableElementMap);
    }

    private Map<String, Map<String, Object>> reBuildLeftJoinMap(String lastJoinTableName,
                                                                Map<String, Map<String, Object>> oldMap,
                                                                Function<Object, String> lastJoinKeyFunction) {
        Map<String, String> relationMap = new HashMap();
        oldMap.forEach((k, v) -> {
            Object o = v.get(lastJoinTableName);
            if (o != null) {
                if (o instanceof List) {
                    List list = (List) o;
                    list.forEach(e -> relationMap.put(lastJoinKeyFunction.apply(e), k));
                } else {
                    relationMap.put(lastJoinKeyFunction.apply(o), k);
                }
            }
        });
        Map<String, Map<String, Object>> newResMap = new HashMap<>();
        relationMap.forEach((k, v) ->
                newResMap.put(k, oldMap.get(v))
        );
        return newResMap;
    }

    public Join join(List<L> leftList,
                     Function<L, String> leftKeyFunction,
                     String leftTableName,
                     Function<List<String>, List<R>> rightListFunction,
                     Function<R, String> rightKeyFunction,
                     String rightTableName,
                     boolean innerJoin) {
        List<String> leftKeys = leftList.stream().map(leftKeyFunction::apply).collect(Collectors.toList());
        System.out.println("[leftKeys]= " + leftKeys.toString());
        List<R> rightList = rightListFunction.apply(leftKeys);
        return join(leftList, leftKeyFunction, leftTableName, rightList, rightKeyFunction, rightTableName, innerJoin);
    }

    public Join join(List<L> leftList,
                     Function<L, String> leftKeyFunction,
                     String leftTableName,
                     List<R> rightList,
                     Function<R, String> rightKeyFunction,
                     String rightTableName,
                     boolean isInnerJoin) {
        Map<String, Map<String, Object>> leftMap = buildMap(leftList, leftKeyFunction, leftTableName);
        Map<String, Map<String, Object>> rightMap = buildMap(rightList, rightKeyFunction, rightTableName);
        leftPutAllRight(leftMap, rightMap, isInnerJoin);
        return join(leftMap, rightMap);
    }

    private Map<String, Map<String, Object>> buildMap(List list,
                                                      Function function,
                                                      String tableName) {
        Map<String, List<Object>> map = (Map) list.stream().collect(Collectors.groupingBy(k -> function.apply(k), Collectors.toList()));
        Map<String, Map<String, Object>> resMap;
//        if (map.size() < list.size()) {
        //表示有重复的,需要把重复的收集为list
        resMap = map.keySet().stream().collect(Collectors.toMap(key -> key, key -> {
            List<Object> objectList = map.get(key);
            Map<String, Object> objectListMap = new HashMap(2);
            objectListMap.put(tableName, objectList);
            return objectListMap;
        }));
//        } else {
//            //表示没有重复,list里面只有一个元素
//            resMap = map.keySet().stream().collect(Collectors.toMap(key -> key, key -> {
//                List<Object> objectList = map.get(key);
//                Map<String, Object> objectListMap = new HashMap(2);
//                objectListMap.put(tableName, objectList.get(0));
//                return objectListMap;
//
//            }));

//                    (Map<String, Map<String, Object>>) list.stream().collect(Collectors.toMap(object -> function.apply(object), object -> {
//                        Map<String, Object> objectMap = new HashMap(2);
//                        objectMap.put(tableName, object);
//                        return objectMap;
//                    }));
//        }
        return resMap;
    }

    private void leftPutAllRight(Map<String, Map<String, Object>> leftIdTableElementMap,
                                 Map<String, Map<String, Object>> rightIdTableElementMap,
                                 boolean innerJoin) {
        Set<String> leftKeys = leftIdTableElementMap.keySet();
        Map<String, Map<String, Object>> newLeftMap = leftKeys.stream().collect(Collectors.toMap(key -> key, key -> {
            Map<String, Object> leftMap = leftIdTableElementMap.get(key);
            Map<String, Object> rightMap = rightIdTableElementMap.get(key);
            if (rightMap != null) {
                leftMap.putAll(rightMap);
            }
            return leftMap;
        }));
        this.newLeftMap = newLeftMap;
        this.resList = this.newLeftMap.values().stream().collect(Collectors.toList());
        if (innerJoin) {
            int max = this.resList.stream().mapToInt(map -> map.size()).max().orElse(0);
            List<Map<String, Object>> collect = this.resList.stream().filter(map -> map.size() == max).collect(Collectors.toList());
            this.resList = collect;
            int max2 = this.newLeftMap.values().stream().mapToInt(map -> map.values().size()).max().orElse(0);
            Set<String> keys = newLeftMap.keySet();
            Map<String, Map<String, Object>> newMap = keys.stream().filter(key -> newLeftMap.get(key).values().size() == max2).collect(Collectors.toMap(key -> key, key -> newLeftMap.get(key)));
            this.newLeftMap = newMap;
        }
    }

    //join底层方法
    private Join join(Map<String, Map<String, Object>> leftMap,
                      Map<String, Map<String, Object>> rightMap) {
        Set<String> leftKeys = leftMap.keySet();
        //  id --> tableName -->  element
        Map<String, Map<String, Object>> joinResMap = leftKeys.stream().collect(Collectors.toMap(k -> k, v -> {
            Map<String, Object> leftRow = leftMap.get(v);
            Map<String, Object> rightRow = rightMap.get(v);
            if (rightRow != null) {
                leftRow.putAll(rightRow);
            }
            return leftRow;
        }));
        this.joinResMap = joinResMap;
        return this;
    }

    public Map<String, Map<String, Object>> getResMap() {
        return this.joinResMap;
    }

    public List<J> mapping(Function<Map<String, Object>, J> mappingFunction) {
        List<Map<String, Object>> mapList = this.resList;
        return mapList.stream().map(mappingFunction::apply).collect(Collectors.toList());
    }

    public List<Map<String, Object>> getResList() {
        return resList;
    }
}