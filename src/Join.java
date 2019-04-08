
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 时间： 2019年04月05日  10时04分
 * 作者： lcx
 * 备注：
 **/
public class Join<L, R> {

    private List<JoinNode> joinNodeList;

    public Join leftJoin(String lastJoinTableName,
                         Function<Object, String> lastJoinKeyFunction,
                         Function<List<String>, List<R>> rightListMappingFunction,
                         Function<R, String> rightKeyFunction,
                         String rightTableName) {
        return join(lastJoinTableName, lastJoinKeyFunction, rightListMappingFunction, rightKeyFunction, rightTableName, false);
    }

    public Join innerJoin(String lastJoinTableName,
                          Function<Object, String> lastJoinKeyFunction,
                          Function<List<String>, List<R>> rightListMappingFunction,
                          Function<R, String> rightKeyFunction,
                          String rightTableName) {
        return join(lastJoinTableName, lastJoinKeyFunction, rightListMappingFunction, rightKeyFunction, rightTableName, true);
    }

    private Join join(String lastJoinTableName,
                      Function<Object, String> lastJoinKeyFunction,
                      Function<List<String>, List<R>> rightListMappingFunction,
                      Function<R, String> rightKeyFunction,
                      String rightTableName,
                      boolean innerJoin) {
        //重新定义外键
        this.joinNodeList.stream().forEach(node -> {
            Map<String, Object> objectMap = node.getJoinRes();
            Object o = objectMap.get(lastJoinTableName);
            if (o != null) {
                node.setKey(lastJoinKeyFunction.apply(o));
            } else {
                node.setKey(null);
            }
        });
        List<String> keyList = this.joinNodeList.stream().map(node -> node.getKey()).filter(key -> key != null).collect(Collectors.toList());
        List<R> rightList = rightListMappingFunction.apply(keyList);
        return join(this.joinNodeList, rightList, rightKeyFunction, rightTableName, innerJoin);
    }

    private Join join(List<JoinNode> leftJoinNodeList,
                      List<R> rightList,
                      Function<R, String> rightKeyFunction,
                      String rightTableName,
                      boolean innerJoin) {
        //重组joinResMap的key
        List<JoinNode> rightJoinNodeList = buildJoinList(rightList, rightKeyFunction, rightTableName);
        return join(leftJoinNodeList, rightJoinNodeList, innerJoin);
    }


    public Join join(List<L> leftList,
                     Function<L, String> leftKeyFunction,
                     String leftTableName,
                     Function<List<String>, List<R>> rightListFunction,
                     Function<R, String> rightKeyFunction,
                     String rightTableName,
                     boolean innerJoin) {
        this.joinNodeList = buildJoinList(leftList, leftKeyFunction, leftTableName);
        List<String> leftKeys = this.joinNodeList.stream().filter(node -> node.getKey() != null).map(node -> node.getKey()).collect(Collectors.toList());
        List<R> rightList = rightListFunction.apply(leftKeys);
        return join(rightList, rightKeyFunction, rightTableName, innerJoin);
    }

    private Join join(List<R> rightList,
                      Function<R, String> rightKeyFunction,
                      String rightTableName,
                      boolean isInnerJoin) {
        List<JoinNode> rightJoinNodeList = buildJoinList(rightList, rightKeyFunction, rightTableName);
        return join(this.joinNodeList, rightJoinNodeList, isInnerJoin);
    }

    private List<JoinNode> buildJoinList(List list,
                                         Function function,
                                         String tableName) {
        List<JoinNode> joinNodeList = (List) list.stream().map(o -> {
            String key = String.valueOf(function.apply(o));
            Map map = new HashMap();
            map.put(tableName, o);
            JoinNode joinNode = new JoinNode().setKey(key).setJoinRes(map);
            return joinNode;
        }).collect(Collectors.toList());
        return joinNodeList;
    }

    //join底层方法
    private Join join(List<JoinNode> leftList,
                      List<JoinNode> rightList,
                      boolean innerJoin) {
        List<JoinNode> mergeJoinNodeList = new ArrayList<>();
        leftList.forEach(leftNode -> {
            String key = leftNode.getKey();
            List<JoinNode> rightNodeList = findByKey(rightList, key);
            if (rightNodeList.size() > 1) {
                rightNodeList.forEach(rightNode -> {
                    JoinNode joinNode = new JoinNode().setKey(key);
                    Map<String, Object> rightNodeMap = rightNode.getJoinRes();
                    Map<String, Object> map = new HashMap<>();
                    map.putAll(leftNode.getJoinRes());
                    if (rightNodeMap != null) {
                        map.putAll(rightNodeMap);
                    }
                    joinNode.setJoinRes(map);
                    mergeJoinNodeList.add(joinNode);
                });
            } else {
                JoinNode joinNode = new JoinNode().setKey(key);
                Map<String, Object> map = new HashMap<>();
                map.putAll(leftNode.getJoinRes());
                if (rightNodeList.size() == 1) {
                    JoinNode joinNode1 = rightNodeList.get(0);
                    Map<String, Object> rightMap = joinNode1.getJoinRes();
                    if (rightMap != null) {
                        map.putAll(rightMap);
                    }
                }
                joinNode.setJoinRes(map);
                mergeJoinNodeList.add(joinNode);
            }
        });
        this.joinNodeList = mergeJoinNodeList;
        if (innerJoin) {
            // 对结果裁剪
            int max = this.joinNodeList.stream().map(JoinNode::getJoinRes).mapToInt(map -> map.size()).max().orElse(0);
            List<JoinNode> collect1 = this.joinNodeList.stream().filter(node -> node.getJoinRes().size() == max).collect(Collectors.toList());
            this.joinNodeList = collect1;
        }
        return this;
    }

    private List<JoinNode> findByKey(List<JoinNode> joinNodeList, String key) {
        if (key == null) {
            return new ArrayList<>();
        }
        return joinNodeList.stream().filter(node -> key.equals(node.getKey())).collect(Collectors.toList());
    }

    private Map<String, Object> buildByTableName(List<Map<String, Object>> mapList, String tableName) {
        Map<String, Object> map = new HashMap();
        for (int i = 0; i < mapList.size(); i++) {
            Map<String, Object> map1 = mapList.get(i);
            map.put(i + "", map1.get(tableName));
        }
        return map;
    }

    public List<JoinNode> getJoinNodeList() {
        return this.joinNodeList;
    }

//    public List<Map<String, Object>> getResLsit(List<String> orderTableName) {
//        //todo 对一对多的记录重组为list
//        //给每个table的记录赋予坐标
//        List<Map<String, Object>> mapList = this.joinNodeList.stream().map(node -> node.getJoinRes()).collect(Collectors.toList());
//        //初始化 表名  坐标 对象
//        List<JoinNode> nodeList = new ArrayList<>();
//        for (String tableName : orderTableName) {
//            nodeList.add(new JoinNode().setKey(tableName).setJoinRes(buildByTableName(mapList, tableName)));
//        }
//        return mapList;
//    }

//    private Map<String, Object> findBy(JoinNode joinNode) {
//        Map<String, Object> joinRes = joinNode.getJoinRes();
//        Map<Object, List<Object>> collect = joinRes.values().stream().collect(Collectors.groupingBy(x -> x, Collectors.toList()));
//        //找到重复对象
//        List<List<Object>> collect1 = collect.values().stream().filter(x -> x.size() > 1).collect(Collectors.toList());
//        return collect1;
//
//    }

}