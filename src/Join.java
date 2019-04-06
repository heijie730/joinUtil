import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 时间： 2019年04月05日  10时04分
 * 作者： lcx
 * 备注：
 **/
public class Join<L, R, J> {

    //有新的join操作时要赋值
    private Map<String, Map<String, Object>> joinResMap;

    private Map<String, Map<String, Object>> newLeftMap;


    //每join完一次要赋值
    private List<Map<String, Object>> resList;


    public Join leftJoin(String lastJoinTableName,
                         Function<Object, Optional<String>> lastJoinKeyFunction,
                         Function<List<String>, List<R>> rightListMappingFunction,
                         Function<R, String> rightKeyFunction,
                         String rightTableName) {
        //重组joinResMap的key

        Map<String, Map<String, Object>> newResMap = reBuildLeftJoinMap(lastJoinTableName, this.joinResMap, lastJoinKeyFunction);

        List<String> keyList = newResMap.keySet().stream().collect(Collectors.toList());
        List<R> rightList = rightListMappingFunction.apply(keyList);
        return leftJoin(newResMap, rightList, rightKeyFunction, rightTableName);
//        return leftJoin(lastJoinTableName, lastJoinKeyFunction, rightIdTableElementMap);
    }

    /**
     * @param lastJoinTableName   左表别名
     * @param lastJoinKeyFunction 左表外键映射函数,返回映射了左外键的optional
     * @param rightList           右表集合
     * @param rightKeyFunction    右表外键映射函数
     * @param rightTableName      右表别名
     * @return
     */
    public Join leftJoin(Map<String, Map<String, Object>> newResMap,
                         List<R> rightList,
                         Function<R, String> rightKeyFunction,
                         String rightTableName) {
        //重组joinResMap的key

//        Map<String, Map<String, Object>> newResMap = reBuildLeftJoinMap(lastJoinTableName, this.joinResMap, lastJoinKeyFunction);

        Map<String, Map<String, Object>> rightIdTableElementMap = build(rightList, rightKeyFunction, rightTableName);
        this.newLeftMap = leftPutAllRight(this.newLeftMap, rightIdTableElementMap);
        return leftJoin(newResMap, rightIdTableElementMap);
//        return leftJoin(lastJoinTableName, lastJoinKeyFunction, rightIdTableElementMap);
    }

//    private Join leftJoin(String lastJoinTableName,
//                          Function<Object, Optional<String>> lastJoinKeyFunction,
//                          Map<String, Map<String, Object>> rightIdTableElementMap) {
//        //重组joinResMap的key
//        Function<Map<String, Object>, Optional<String>> keyFunction = map -> {
//            Object o = map.get(lastJoinTableName);
//            //o可能为null
//            return lastJoinKeyFunction.apply(o);
//        };
//        Map<String, Map<String, Object>> newResMap = reBuildLeftJoinMap(lastJoinTableName, this.joinResMap, keyFunction);
//
//        return leftJoin(newResMap, rightIdTableElementMap);
//    }

    private Map<String, Map<String, Object>> reBuildLeftJoinMap(String lastJoinTableName,
                                                                Map<String, Map<String, Object>> oldMap,
                                                                Function<Object, Optional<String>> lastJoinKeyFunction) {
        Function<Map<String, Object>, Optional<String>> keyFunction = map -> {
            Object o = map.get(lastJoinTableName);
            //o可能为null
            return lastJoinKeyFunction.apply(o);
        };
//        List<Map<String, Object>> mapList = oldMap.values().stream().collect(Collectors.toList());
        List<Map<String, Object>> mapList = this.resList;
        //mapList可能有table的值为null
        //有要以这个table为join,只能过滤掉为null的记录
        Map<String, Map<String, Object>> newResMap = mapList.stream()
// 这里map.get(lastJoinTableName) 可能为null给方法调用者
//  .filter(map -> map.get(lastJoinTableName) != null)
//                .filter(map -> map != null)
                //toMap 时会遇到两个key相同的情况
                .filter(map -> keyFunction.apply(map).orElse(null) != null)
                .collect(Collectors.toMap(map -> keyFunction.apply(map).orElse(null), map -> map, (a, b) -> b));
        return newResMap;
    }

    public Join innerJoin(String lastJoinTableName,
                          Function<Object, String> lastJoinKeyFunction,
                          List<R> rightList,
                          Function<R, String> rightKeyFunction,
                          String rightTableName) {
        Map<String, Map<String, Object>> rightIdTableElementMap = build(rightList, rightKeyFunction, rightTableName);
        return innerJoin(lastJoinTableName, lastJoinKeyFunction, rightIdTableElementMap);
    }

    private Join innerJoin(String lastJoinTableName,
                           Function<Object, String> lastJoinKeyFunction,
                           Map<String, Map<String, Object>> rightIdTableElementMap) {
        //重组joinResMap的key
        Function<Map<String, Object>, String> keyFunction = map -> {
            Object o = map.get(lastJoinTableName);
            //o 可能为null
            return lastJoinKeyFunction.apply(o);
        };
        Map<String, Map<String, Object>> newResMap = reBuildInnerJoinMap(lastJoinTableName, this.joinResMap, keyFunction);
        return innerJoin(newResMap, rightIdTableElementMap);
    }


    private Map<String, Map<String, Object>> reBuildInnerJoinMap(String lastJoinTableName,
                                                                 Map<String, Map<String, Object>> oldMap,
                                                                 Function<Map<String, Object>, String> keyFunction) {

        List<Map<String, Object>> mapList = oldMap.values().stream().collect(Collectors.toList());
        //mapList可能有tableName的值为null
        //有要以这个table为join,只能过滤掉为null的记录
        Map<String, Map<String, Object>> newResMap = mapList.stream().filter(map -> map.get(lastJoinTableName) != null)
                .collect(Collectors.toMap(map -> keyFunction.apply(map), map -> map));
        return newResMap;
    }

    public Join leftJoin(List<L> leftList,
                         Function<L, String> leftKeyFunction,
                         String leftTableName,
                         List<R> rightList,
                         Function<R, String> rightKeyFunction,
                         String rightTableName) {
        Map<String, Map<String, Object>> leftIdTableElementMap = build(leftList, leftKeyFunction, leftTableName);
        Map<String, Map<String, Object>> rightIdTableElementMap = build(rightList, rightKeyFunction, rightTableName);
        this.newLeftMap = leftPutAllRight(leftIdTableElementMap, rightIdTableElementMap);
        return leftJoin(leftIdTableElementMap, rightIdTableElementMap);
    }

    public Join innerJoin(List<L> leftList,
                          Function<L, String> leftKeyFunction,
                          String leftTableName,
                          List<R> rightList,
                          Function<R, String> rightKeyFunction,
                          String rightTableName) {
        Map<String, Map<String, Object>> leftIdTableElementMap = build(leftList, leftKeyFunction, leftTableName);
        Map<String, Map<String, Object>> rightIdTableElementMap = build(rightList, rightKeyFunction, rightTableName);
        return innerJoin(leftIdTableElementMap, rightIdTableElementMap);
    }


    private Map<String, Map<String, Object>> build(List list,
                                                   Function function,
                                                   String tableName) {
//        long count = list.stream().map(k -> function.apply(k)).distinct().count();
        Map<String, List<Object>> map = (Map) list.stream().collect(Collectors.groupingBy(k -> function.apply(k), Collectors.toList()));
        if (map.size() < list.size()) {
            //表示有重复的,需要把重复的收集为list
            Map<String, Map<String, Object>> idTableElementListMap = map.keySet().stream().collect(Collectors.toMap(key -> key, key -> {
                List<Object> objectList = map.get(key);
                Map<String, Object> tableElementMap = new HashMap(2);
                tableElementMap.put(tableName, objectList);
                return tableElementMap;
            }));
            return idTableElementListMap;
        } else {
            Map<String, Map<String, Object>> idTableElementMap =
                    (Map<String, Map<String, Object>>) list.stream().collect(Collectors.toMap(k -> function.apply(k), v -> {
                        Map<String, Object> tableElementMap = new HashMap(2);
                        tableElementMap.put(tableName, v);
                        return tableElementMap;
                    }));
            return idTableElementMap;
        }
    }


    private Join leftJoin(Map<String, Map<String, Object>> leftIdTableElementMap,
                          Map<String, Map<String, Object>> rightIdTableElementMap) {

        this.joinResMap = MergeUtil.leftJoin(leftIdTableElementMap, rightIdTableElementMap);
//        this.resList = this.joinResMap.values().stream().collect(Collectors.toList());
        //leftJoin永远时左边putAll右边
        this.resList = this.newLeftMap.values().stream().collect(Collectors.toList());
        return this;
    }

    private Map<String, Map<String, Object>> leftPutAllRight(Map<String, Map<String, Object>> leftIdTableElementMap,
                                                             Map<String, Map<String, Object>> rightIdTableElementMap) {
        Set<String> leftKeys = leftIdTableElementMap.keySet();
        Map<String, Map<String, Object>> newLeftMap = leftKeys.stream().collect(Collectors.toMap(key -> key, key -> {
            Map<String, Object> leftMap = leftIdTableElementMap.get(key);
            Map<String, Object> rightMap = rightIdTableElementMap.get(key);
            if (rightMap != null) {
                leftMap.putAll(rightMap);
            }
            return leftMap;
        }));
        return newLeftMap;
    }

    private Join innerJoin(Map<String, Map<String, Object>> leftIdTableElementMap,
                           Map<String, Map<String, Object>> rightIdTableElementMap) {

        this.joinResMap = MergeUtil.innerJoin(leftIdTableElementMap, rightIdTableElementMap);
        return this;
    }

    public Map<String, Map<String, Object>> getResMap() {
        return this.joinResMap;
    }

    public List<J> mapping(Function<Map<String, Object>, J> mappingFunction) {
//        List<Map<String, Object>> mapList = this.joinResMap.values().stream().collect(Collectors.toList());
        List<Map<String, Object>> mapList = this.resList;
        return mapList.stream().map(mappingFunction::apply).collect(Collectors.toList());
    }

    public List<Map<String, Object>> getResList() {
        return resList;
    }
}