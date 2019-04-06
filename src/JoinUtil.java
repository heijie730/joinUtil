import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * 时间： 2019年04月05日  10时10分
 * 作者： lcx
 * 备注：
 **/
public class JoinUtil {
    public static <L, R, J> Join leftJoin(List<L> leftList,
                                          Function<L, String> leftKeyFunction,
                                          String leftTableName,
                                          List<R> rightList,
                                          Function<R, String> rightKeyFunction,
                                          String rightTableName) {
        return new Join<L, R, J>().leftJoin(leftList, leftKeyFunction, leftTableName, rightList, rightKeyFunction, rightTableName);
    }

    public static <L, R, J> Join innerJoin(List<L> leftList,
                                           Function<L, String> leftKeyFunction,
                                           String leftTableName,
                                           List<R> rightList,
                                           Function<R, String> rightKeyFunction,
                                           String rightTableName) {
        return new Join<L, R, J>().innerJoin(leftList, leftKeyFunction, leftTableName, rightList, rightKeyFunction, rightTableName);
    }
}
