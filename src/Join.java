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

    public Join leftJoin(Map<String, Map<String, Object>> newResMap,
                         List<R> rightList,
                         Function<R, String> rightKeyFunction,
                         String rightTableName) {
        return join(newResMap, rightList, rightKeyFunction, rightTableName, false);
    }

    /**
     * @param lastJoinTableName   左表别名
     * @param lastJoinKeyFunction 左表外键映射函数,返回映射了左外键的optional
     * @param rightList           右表集合
     * @param rightKeyFunction    右表外键映射函数
     * @param rightTableName      右表别名
     * @return
     */
    public Join join(Map<String, Map<String, Object>> newResMap,
                     List<R> rightList,
                     Function<R, String> rightKeyFunction,
                     String rightTableName,
                     boolean innerJoin) {
        //重组joinResMap的key
        Map<String, Map<String, Object>> rightIdTableElementMap = build(rightList, rightKeyFunction, rightTableName);
        leftPutAllRight(this.newLeftMap, rightIdTableElementMap, innerJoin);
        return leftJoin(newResMap, rightIdTableElementMap);
    }

    private Map<String, Map<String, Object>> reBuildLeftJoinMap(String lastJoinTableName,
                                                                Map<String, Map<String, Object>> oldMap,
                                                                Function<Object, String> lastJoinKeyFunction) {
        Function<Map<String, Object>, String> keyFunction = map -> {
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
                .filter(map -> map.get(lastJoinTableName) != null)
//                .filter(map -> map != null)
                //toMap 时会遇到两个key相同的情况
                .filter(map -> keyFunction.apply(map) != null)
                .collect(Collectors.toMap(map -> keyFunction.apply(map), map -> map, (a, b) -> b));
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

    public Join join(List<L> leftList,
                     Function<L, String> leftKeyFunction,
                     String leftTableName,
                     List<R> rightList,
                     Function<R, String> rightKeyFunction,
                     String rightTableName,
                     boolean innerJoin) {
        Map<String, Map<String, Object>> leftIdTableElementMap = build(leftList, leftKeyFunction, leftTableName);
        Map<String, Map<String, Object>> rightIdTableElementMap = build(rightList, rightKeyFunction, rightTableName);
         leftPutAllRight(leftIdTableElementMap, rightIdTableElementMap, innerJoin);
        return leftJoin(leftIdTableElementMap, rightIdTableElementMap);
    }


//    public Join innerJoin(List<L> leftList,
//                          Function<L, String> leftKeyFunction,
//                          String leftTableName,
//                          List<R> rightList,
//                          Function<R, String> rightKeyFunction,
//                          String rightTableName) {
//        //取能join到左右元素的map
//        join(leftList, leftKeyFunction, leftTableName, rightList, rightKeyFunction, rightTableName,true);
//        return this;
//    }


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
        return this;
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
            this.newLeftMap=newMap;
        }
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