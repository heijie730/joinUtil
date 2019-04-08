/**
 * 时间： 2019年04月05日  12时23分
 * 作者： lcx
 * 备注：测试实体
 **/
public class ReturnOrder {
    String id;
    String orderId;
    String returnOrderName;
    String userId;

    public String getUserId() {
        return userId;
    }

    public ReturnOrder setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getId() {
        return id;
    }

    public ReturnOrder setId(String id) {
        this.id = id;
        return this;
    }

    public String getOrderId() {
        return orderId;
    }

    public ReturnOrder setOrderId(String orderId) {
        this.orderId = orderId;
        return this;
    }

    public String getReturnOrderName() {
        return returnOrderName;
    }

    public ReturnOrder setReturnOrderName(String returnOrderName) {
        this.returnOrderName = returnOrderName;
        return this;
    }
}
