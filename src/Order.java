/**
 * 时间： 2019年04月05日  12时22分
 * 作者： lcx
 * 备注：
 **/
public class Order {
    String id;
    String userId;
    String orderName;

    public String getId() {
        return id;
    }

    public Order setId(String id) {
        this.id = id;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public Order setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getOrderName() {
        return orderName;
    }

    public Order setOrderName(String orderName) {
        this.orderName = orderName;
        return this;
    }
}
