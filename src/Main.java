import com.alibaba.fastjson.JSON;


import java.util.*;

public class Main {

    public static void main(String[] args) {
        test1();
//        test2();
//        test3();
    }
    public static void test1() {
        User user1 = new User().setId("u1").setName("小明");
        User user2 = new User().setId("u2").setName("小红");
        User user3 = new User().setId("u3").setName("小张");
        List<User> userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);
        userList.add(user3);

        Order order1 = new Order().setId("o1").setUserId("u1").setOrderName("小明的订单");
        Order order2 = new Order().setId("o2").setUserId("u2").setOrderName("小红的订单");
        Order order3 = new Order().setId("o33").setUserId("u3").setOrderName("小张的订单");
        List<Order> orderList = new ArrayList<>();
        orderList.add(order1);
        orderList.add(order2);
        orderList.add(order3);

        ReturnOrder returnOrder1 = new ReturnOrder().setId("r1").setOrderId("o1").setUserId("u1").setReturnOrderName("小明的退单");
        ReturnOrder returnOrder2 = new ReturnOrder().setId("r2").setOrderId("o2").setUserId("u2").setReturnOrderName("小红的退单");
        ReturnOrder returnOrder3 = new ReturnOrder().setId("r3").setOrderId("o3").setUserId("u3").setReturnOrderName("小张的退单");
        ReturnOrder returnOrder4 = new ReturnOrder().setId("r4").setOrderId("o4").setUserId("u4").setReturnOrderName("小吴的退单");
        List<ReturnOrder> returnOrderList = new ArrayList<>();
        returnOrderList.add(returnOrder1);
        returnOrderList.add(returnOrder2);
        returnOrderList.add(returnOrder3);
        returnOrderList.add(returnOrder4);

//        Join join = JoinUtil.leftJoin(userList, x -> x.getId(), "user", orderList, x -> x.getUserId(), "order")
////                .leftJoin("order",
////                        x -> Optional.ofNullable(x)
////                        .map(e->(List)e)
////                        .map(e -> (Order) e.get(0))
////                        .map(Order::getId),
////                        x->returnOrderList, x -> ((ReturnOrder) x).getOrderId(), "returnOrder");
//                .leftJoin("order", x -> Optional.ofNullable(x).map(e -> (Order) e).map(Order::getId), x->returnOrderList, x -> ((ReturnOrder) x).getOrderId(), "returnOrder");

        Join join = JoinUtil.leftJoin(userList, x -> x.getId(), "user", orderList, x -> x.getUserId(), "order")
                .leftJoin("order", x -> Optional.ofNullable(x).map(e -> (Order) e).map(Order::getId), x->returnOrderList, x -> ((ReturnOrder) x).getOrderId(), "returnOrder");
//                .leftJoin("user", x -> Optional.ofNullable(x).map(e -> (User) e).map(User::getId), x->returnOrderList, x -> ((ReturnOrder) x).getUserId(), "returnOrder");

        System.out.println("[test1 - list] --> [join1 -> user.id = order.userId , join2 -> order.id = returnOrder.orderId ]  --->");
        join.getResList().forEach(x -> System.out.println(JSON.toJSON(x)));
        Map resMap = join.getResMap();
        System.out.println("[test1 - map] --> [join1 -> user.id = order.userId , join2 -> order.id = returnOrder.orderId ]  --->");
        resMap.forEach((k, v) -> System.out.println(JSON.toJSON(v)));
    }

    public static void test2() {
        User user1 = new User().setId("u1").setName("小明");
        User user2 = new User().setId("u2").setName("小红");
        List<User> userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);

        Order order1 = new Order().setId("o1").setUserId("u1").setOrderName("小明的订单");
        Order order2 = new Order().setId("o2").setUserId("u2").setOrderName("小红的订单");
        List<Order> orderList = new ArrayList<>();
        orderList.add(order1);
        orderList.add(order2);

        ReturnOrder returnOrder1 = new ReturnOrder().setId("r1").setOrderId("o1").setUserId("u1").setReturnOrderName("小明的退单");
        ReturnOrder returnOrder2 = new ReturnOrder().setId("r2").setOrderId("o42").setUserId("u2").setReturnOrderName("小红的退单");
        List<ReturnOrder> returnOrderList = new ArrayList<>();
        returnOrderList.add(returnOrder1);
        returnOrderList.add(returnOrder2);

        Join join = JoinUtil.innerJoin(userList, x -> x.getId(), "user", orderList, x -> x.getUserId(), "order")
                .innerJoin("order", x -> ((Order) x).getId(), returnOrderList, x -> ((ReturnOrder) x).getOrderId(), "returnOrder");
        List mappingList = join.mapping((x) -> {
            Map map = (Map) x;
            User user = (User) map.getOrDefault("user", new User());
            Order order = (Order) map.getOrDefault("order", new Order());
            ReturnOrder returnOrder = (ReturnOrder) map.getOrDefault("returnOrder", new ReturnOrder());
            Map resMap = new HashMap();
            resMap.put("名字", user.getName());
            resMap.put("订单名称", order.getOrderName());
            resMap.put("退单名称", returnOrder.getReturnOrderName());
            return resMap;
        });
        System.out.println("[test1 - list] --> [join1 -> user.id = order.userId , join2 -> order.id = returnOrder.orderId ]  --->");
        mappingList.forEach(x -> System.out.println(JSON.toJSON(x)));
        Map resMap = join.getResMap();
        System.out.println("[test1 - map] --> [join1 -> user.id = order.userId , join2 -> order.id = returnOrder.orderId ]  --->");
        resMap.forEach((k, v) -> System.out.println(JSON.toJSON(v)));
    }


}
