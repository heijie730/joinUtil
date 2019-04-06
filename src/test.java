import java.util.HashMap;
import java.util.Map;

/**
 * 时间： 2019年04月06日  10时20分
 * 作者： lcx
 * 备注：
 **/
public class test {

    public static void main(String[] args) {
        Map map = new HashMap<>();
        map.put("u","123");
        map.put("u","123");
        Map map2 = new HashMap<>();
        map2.put("u","123");
        map2.put("u","123");
        map.putAll(map2);
        System.out.println(map.toString());
    }
}
