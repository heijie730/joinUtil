import java.util.List;
import java.util.function.Function;

/**
 * 时间： 2019年04月05日  10时10分
 * 作者： lcx
 * 备注：方法入口
 **/
public class JoinUtil {
    /**
     *
     * @param leftList 左表
     * @param leftKeyFunction 出参:key  左表某个字段   用于关联右表
     * @param leftTableName 左表别名
     * @param rightListFunction 入参:左表key的列表  出参:右表
     * @param rightKeyFunction  出参:key  右表某个字段   用于关联左表
     * @param rightTableName  右表别名
     * @param <L>  左表实体
     * @param <R>  右表实体
     * @return
     */
    public static <L, R> Join leftJoin(List<L> leftList,
                                          Function<L, String> leftKeyFunction,
                                          String leftTableName,
                                          Function<List<String>, List<R>> rightListFunction,
                                          Function<R, String> rightKeyFunction,
                                          String rightTableName) {
        return new Join<L, R>().join(leftList, leftKeyFunction, leftTableName, rightListFunction, rightKeyFunction, rightTableName, false);
    }

    public static <L, R> Join innerJoin(List<L> leftList,
                                           Function<L, String> leftKeyFunction,
                                           String leftTableName,
                                           Function<List<String>, List<R>> rightListFunction,
                                           Function<R, String> rightKeyFunction,
                                           String rightTableName) {
        return new Join<L, R>().join(leftList, leftKeyFunction, leftTableName, rightListFunction, rightKeyFunction, rightTableName, true);
    }
}
