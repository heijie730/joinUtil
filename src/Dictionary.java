import java.util.HashMap;
import java.util.Map;

/**
 * 作者 林超绪 2019/4/22 16:13
 * 备注
 */
public class Dictionary<O> {
    private Map<Integer, Map<Integer, O>> value = new HashMap<>();

    public O get(Integer row, Integer column) {
        Map<Integer, O> map = this.value.get(row);
        if (map == null) {
            return null;
        }
        return map.get(column);
    }

    public void put(Integer row, Integer column, O o) {
        Map<Integer, O> map = this.value.getOrDefault(row, new HashMap<>(2));
        map.put(column, o);
    }

}
