/**
 * 时间： 2019年04月06日  22时33分
 * 作者： lcx
 * 备注：测试实体
 **/
public class payRecord {
    String returnOrderId;
    String amount;

    public String getReturnOrderId() {
        return returnOrderId;
    }

    public payRecord setReturnOrderId(String returnOrderId) {
        this.returnOrderId = returnOrderId;
        return this;
    }

    public String getAmount() {
        return amount;
    }

    public payRecord setAmount(String amount) {
        this.amount = amount;
        return this;
    }
}
