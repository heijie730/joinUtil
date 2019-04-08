import java.util.Map;

/**
 * 时间： 2019年04月07日  17时05分
 * 作者： lcx
 * 备注：
 **/
public class JoinNode {

    private String key;  //关联外键

    private Map<String, Object>  joinRes;//key 是 表名(别名)  value 是相应实体

    public String getKey() {
        return key;
    }

    public JoinNode setKey(String key) {
        this.key = key;
        return this;
    }

    public Map<String, Object> getJoinRes() {
        return joinRes;
    }

    public JoinNode setJoinRes(Map<String, Object> joinRes) {
        this.joinRes = joinRes;
        return this;
    }
}
