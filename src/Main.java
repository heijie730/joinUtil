

import com.alibaba.fastjson.JSON;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        //一对一
        test_1_leftJoin_1();
        //一对多
        test_1_leftJoin_D();
        test_1_leftJoin_1_leftJoin_1();
        test_1_leftJoin_D_leftJoin_1();
        test_1_leftJoin_D_leftJoin_D();
        test_1_leftJoin_1_innerJoin_1();
        test_1_leftJoin_D_innerJoin_1();
        test_1_leftJoin_D_innerJoin_D();
        test_1_innerJoin_1();
        test_1_innerJoin_D();
        test_1_innerJoin_1_innerJoin_1();
        test_1_innerJoin_D_innerJoin_1();
        test_1_innerJoin_D_innerJoin_D();
        test_1_innerJoin_1_leftJoin_1();
        test_1_innerJoin_D_leftJoin_1();
        test_1_innerJoin_D_leftJoin_D();

    }

    public static void test_1_leftJoin_1() {
        User user1 = new User().setId("u1").setName("小明");
        User user2 = new User().setId("u2").setName("小红");
        User user3 = new User().setId("u3").setName("小张");
        List<User> userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);
        userList.add(user3);

        Order order1 = new Order().setId("o1").setUserId("u1").setOrderName("小明的订单");
        Order order2 = new Order().setId("o2").setUserId("u2").setOrderName("小红的订单");
        Order order3 = new Order().setId("o3").setUserId("u3").setOrderName("小张的订单");
        List<Order> orderList = new ArrayList<>();
        orderList.add(order1);
        orderList.add(order2);
//        orderList.add(order3);
        Join join = JoinUtil.leftJoin(userList, x -> x.getId(), "user", x -> orderList, x -> x.getUserId(), "order");
        System.out.println("[test_1_leftJoin_1] --->");
        join.getJoinNodeList().forEach(x -> System.out.println(JSON.toJSON(x)));
    }

    public static void test_1_leftJoin_D() {
        User user1 = new User().setId("u1").setName("小明");
        User user2 = new User().setId("u2").setName("小红");
        User user3 = new User().setId("u3").setName("小张");
        List<User> userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);
        userList.add(user3);

        Order order1 = new Order().setId("o1").setUserId("u1").setOrderName("小明的订单");
        Order order2 = new Order().setId("o2").setUserId("u2").setOrderName("小红的订单");
        Order order3 = new Order().setId("o3").setUserId("u3").setOrderName("小张的订单");
        Order order4 = new Order().setId("o4").setUserId("u3").setOrderName("小张的订单2");
        List<Order> orderList = new ArrayList<>();
        orderList.add(order1);
//        orderList.add(order2);
        orderList.add(order3);
        orderList.add(order4);

        Join join = JoinUtil.leftJoin(userList, x -> x.getId(), "user", x -> orderList, x -> x.getUserId(), "order");
        System.out.println("[test_1_leftJoin_D] --->");
        join.getJoinNodeList().forEach(x -> System.out.println(JSON.toJSON(x)));
    }

    public static void test_1_leftJoin_1_leftJoin_1() {
        User user1 = new User().setId("u1").setName("小明");
        User user2 = new User().setId("u2").setName("小红");
        User user3 = new User().setId("u3").setName("小张");
        List<User> userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);
        userList.add(user3);

        Order order1 = new Order().setId("o1").setUserId("u1").setOrderName("小明的订单");
        Order order2 = new Order().setId("o2").setUserId("u2").setOrderName("小红的订单");
        Order order3 = new Order().setId("o3").setUserId("u3").setOrderName("小张的订单");
        List<Order> orderList = new ArrayList<>();
        orderList.add(order1);
        orderList.add(order2);
//        orderList.add(order3);

        ReturnOrder returnOrder1 = new ReturnOrder().setId("r1").setOrderId("o1").setUserId("u1").setReturnOrderName("小明的退单");
        ReturnOrder returnOrder2 = new ReturnOrder().setId("r2").setOrderId("o2").setUserId("u2").setReturnOrderName("小红的退单");
        ReturnOrder returnOrder3 = new ReturnOrder().setId("r3").setOrderId("o3").setUserId("u3").setReturnOrderName("小张的退单");
        List<ReturnOrder> returnOrderList = new ArrayList<>();
        returnOrderList.add(returnOrder1);
        returnOrderList.add(returnOrder2);
        returnOrderList.add(returnOrder3);
        Join join = JoinUtil.leftJoin(userList, x -> x.getId(), "user", x -> orderList, x -> x.getUserId(), "order")
                .leftJoin("order", x -> ((Order) x).getId(), x -> returnOrderList, x -> ((ReturnOrder) x).getOrderId(), "returnOrder");
        System.out.println("[test_1_leftJoin_1_leftJoin_1]--->");
        join.getJoinNodeList().forEach(x -> System.out.println(JSON.toJSON(x)));
    }

    public static void test_1_leftJoin_D_leftJoin_1() {
        User user1 = new User().setId("u1").setName("小明");
        User user2 = new User().setId("u2").setName("小红");
        User user3 = new User().setId("u3").setName("小张");
        List<User> userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);
        userList.add(user3);

        Order order1 = new Order().setId("o1").setUserId("u1").setOrderName("小明的订单");
        Order order2 = new Order().setId("o2").setUserId("u2").setOrderName("小红的订单");
        Order order3 = new Order().setId("o3").setUserId("u3").setOrderName("小张的订单");
        Order order4 = new Order().setId("o4").setUserId("u3").setOrderName("小张的订单2");
        List<Order> orderList = new ArrayList<>();
        orderList.add(order1);
        orderList.add(order2);
        orderList.add(order3);
        orderList.add(order4);

        ReturnOrder returnOrder1 = new ReturnOrder().setId("r1").setOrderId("o1").setUserId("u1").setReturnOrderName("小明的退单");
        ReturnOrder returnOrder2 = new ReturnOrder().setId("r2").setOrderId("o2").setUserId("u2").setReturnOrderName("小红的退单");
        ReturnOrder returnOrder3 = new ReturnOrder().setId("r3").setOrderId("o3").setUserId("u3").setReturnOrderName("小张的退单");
        ReturnOrder returnOrder4 = new ReturnOrder().setId("r4").setOrderId("o4").setUserId("u3").setReturnOrderName("小张的退单222");
        List<ReturnOrder> returnOrderList = new ArrayList<>();
        returnOrderList.add(returnOrder1);
        returnOrderList.add(returnOrder2);
        returnOrderList.add(returnOrder3);
        returnOrderList.add(returnOrder4);

        Join join = JoinUtil.leftJoin(userList, x -> x.getId(), "user", x -> orderList, x -> x.getUserId(), "order")
                .leftJoin("order", x -> ((Order) x).getId(), x -> returnOrderList, x -> ((ReturnOrder) x).getOrderId(), "returnOrder");
        System.out.println("[test_1_leftJoin_D_leftJoin_1]--->");
        join.getJoinNodeList().forEach(x -> System.out.println(JSON.toJSON(x)));
    }

    public static void test_1_leftJoin_D_leftJoin_D() {
        User user1 = new User().setId("u1").setName("小明");
        User user2 = new User().setId("u2").setName("小红");
        User user3 = new User().setId("u3").setName("小张");
        List<User> userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);
        userList.add(user3);

        Order order1 = new Order().setId("o1").setUserId("u1").setOrderName("小明的订单");
        Order order2 = new Order().setId("o2").setUserId("u2").setOrderName("小红的订单");
        Order order3 = new Order().setId("o3").setUserId("u3").setOrderName("小张的订单");
        Order order4 = new Order().setId("o4").setUserId("u3").setOrderName("小张的订单22");
        List<Order> orderList = new ArrayList<>();
        orderList.add(order1);
        orderList.add(order2);
        orderList.add(order3);
        orderList.add(order4);

        ReturnOrder returnOrder1 = new ReturnOrder().setId("r1").setOrderId("o1").setUserId("u1").setReturnOrderName("小明的退单");
        ReturnOrder returnOrder2 = new ReturnOrder().setId("r2").setOrderId("o3").setUserId("u3").setReturnOrderName("小张的退单");
        ReturnOrder returnOrder3 = new ReturnOrder().setId("r3").setOrderId("o3").setUserId("u3").setReturnOrderName("小张的退单22");
        ReturnOrder returnOrder4 = new ReturnOrder().setId("r4").setOrderId("o4").setUserId("u3").setReturnOrderName("小张的退单111");
        ReturnOrder returnOrder5 = new ReturnOrder().setId("r5").setOrderId("o4").setUserId("u3").setReturnOrderName("小张的退单2222");
        List<ReturnOrder> returnOrderList = new ArrayList<>();
        returnOrderList.add(returnOrder1);
        returnOrderList.add(returnOrder2);
        returnOrderList.add(returnOrder3);
        returnOrderList.add(returnOrder4);
        returnOrderList.add(returnOrder5);

        Join join = JoinUtil.leftJoin(userList, x -> x.getId(), "user", x -> orderList, x -> x.getUserId(), "order")
                .leftJoin("order", x -> ((Order) x).getId(), x -> returnOrderList, x -> ((ReturnOrder) x).getOrderId(), "returnOrder");
        System.out.println("[test_1_leftJoin_D_leftJoin_D]--->");
        join.getJoinNodeList().forEach(x -> System.out.println(JSON.toJSON(x)));
    }
    public static void test_1_leftJoin_1_innerJoin_1() {
        User user1 = new User().setId("u1").setName("小明");
        User user2 = new User().setId("u2").setName("小红");
        User user3 = new User().setId("u3").setName("小张");
        List<User> userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);
        userList.add(user3);

        Order order1 = new Order().setId("o1").setUserId("u1").setOrderName("小明的订单");
        Order order2 = new Order().setId("o2").setUserId("u2").setOrderName("小红的订单");
        Order order3 = new Order().setId("o3").setUserId("u3").setOrderName("小张的订单");
        List<Order> orderList = new ArrayList<>();
        orderList.add(order1);
        orderList.add(order2);
        orderList.add(order3);

        ReturnOrder returnOrder1 = new ReturnOrder().setId("r1").setOrderId("o1").setUserId("u1").setReturnOrderName("小明的退单");
        ReturnOrder returnOrder2 = new ReturnOrder().setId("r2").setOrderId("o2").setUserId("u2").setReturnOrderName("小红的退单");
        ReturnOrder returnOrder3 = new ReturnOrder().setId("r3").setOrderId("o3").setUserId("u3").setReturnOrderName("小张的退单");
        List<ReturnOrder> returnOrderList = new ArrayList<>();
        returnOrderList.add(returnOrder1);
        returnOrderList.add(returnOrder2);
        returnOrderList.add(returnOrder3);
        Join join = JoinUtil.leftJoin(userList, x -> x.getId(), "user", x -> orderList, x -> x.getUserId(), "order")
                .innerJoin("order", x -> ((Order) x).getId(), x -> returnOrderList, x -> ((ReturnOrder) x).getOrderId(), "returnOrder");
        System.out.println("[test_1_leftJoin_1_innerJoin_1]--->");
        join.getJoinNodeList().forEach(x -> System.out.println(JSON.toJSON(x)));
    }

    public static void test_1_leftJoin_D_innerJoin_1() {
        User user1 = new User().setId("u1").setName("小明");
        User user2 = new User().setId("u2").setName("小红");
        User user3 = new User().setId("u3").setName("小张");
        List<User> userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);
        userList.add(user3);

        Order order1 = new Order().setId("o1").setUserId("u1").setOrderName("小明的订单");
        Order order2 = new Order().setId("o2").setUserId("u2").setOrderName("小红的订单");
        Order order3 = new Order().setId("o3").setUserId("u3").setOrderName("小张的订单");
        Order order4 = new Order().setId("o4").setUserId("u3").setOrderName("小张的订单222");
        List<Order> orderList = new ArrayList<>();
        orderList.add(order1);
        orderList.add(order2);
        orderList.add(order3);
        orderList.add(order4);

        ReturnOrder returnOrder1 = new ReturnOrder().setId("r1").setOrderId("o1").setUserId("u1").setReturnOrderName("小明的退单");
        ReturnOrder returnOrder2 = new ReturnOrder().setId("r2").setOrderId("o2").setUserId("u2").setReturnOrderName("小红的退单");
        ReturnOrder returnOrder3 = new ReturnOrder().setId("r3").setOrderId("o3").setUserId("u3").setReturnOrderName("小张的退单");
        ReturnOrder returnOrder4 = new ReturnOrder().setId("r4").setOrderId("o4").setUserId("u3").setReturnOrderName("小张的退单22");
        List<ReturnOrder> returnOrderList = new ArrayList<>();
        returnOrderList.add(returnOrder1);
        returnOrderList.add(returnOrder2);
        returnOrderList.add(returnOrder3);
        returnOrderList.add(returnOrder4);

        Join join = JoinUtil.leftJoin(userList, x -> x.getId(), "user", x -> orderList, x -> x.getUserId(), "order")
                .innerJoin("order", x -> ((Order) x).getId(), x -> returnOrderList, x -> ((ReturnOrder) x).getOrderId(), "returnOrder");
        System.out.println("[test_1_leftJoin_D_innerJoin_1]--->");
        join.getJoinNodeList().forEach(x -> System.out.println(JSON.toJSON(x)));
    }

    public static void test_1_leftJoin_D_innerJoin_D() {
        User user1 = new User().setId("u1").setName("小明");
        User user2 = new User().setId("u2").setName("小红");
        User user3 = new User().setId("u3").setName("小张");
        List<User> userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);
        userList.add(user3);

        Order order1 = new Order().setId("o1").setUserId("u1").setOrderName("小明的订单");
        Order order2 = new Order().setId("o2").setUserId("u2").setOrderName("小红的订单");
        Order order3 = new Order().setId("o3").setUserId("u3").setOrderName("小张的订单");
        Order order4 = new Order().setId("o4").setUserId("u3").setOrderName("小张的订单222");
        List<Order> orderList = new ArrayList<>();
//        orderList.add(order1);
        orderList.add(order2);
        orderList.add(order3);
        orderList.add(order4);

        ReturnOrder returnOrder1 = new ReturnOrder().setId("r1").setOrderId("o1").setUserId("u1").setReturnOrderName("小明的退单");
        ReturnOrder returnOrder2 = new ReturnOrder().setId("r2").setOrderId("o2").setUserId("u2").setReturnOrderName("小红的退单");
        ReturnOrder returnOrder3 = new ReturnOrder().setId("r3").setOrderId("o3").setUserId("u3").setReturnOrderName("小张的退单");
        ReturnOrder returnOrder4 = new ReturnOrder().setId("r4").setOrderId("o3").setUserId("u3").setReturnOrderName("小张的退单222");
        ReturnOrder returnOrder5 = new ReturnOrder().setId("r5").setOrderId("o4").setUserId("u3").setReturnOrderName("小张的退单333");
        ReturnOrder returnOrder6 = new ReturnOrder().setId("r6").setOrderId("o4").setUserId("u3").setReturnOrderName("小张的退单444");

        List<ReturnOrder> returnOrderList = new ArrayList<>();
        returnOrderList.add(returnOrder1);
        returnOrderList.add(returnOrder2);
        returnOrderList.add(returnOrder3);
        returnOrderList.add(returnOrder4);
        returnOrderList.add(returnOrder5);
        returnOrderList.add(returnOrder6);

        Join join = JoinUtil.leftJoin(userList, x -> x.getId(), "user", x -> orderList, x -> x.getUserId(), "order")
                .innerJoin("order", x -> ((Order) x).getId(), x -> returnOrderList, x -> ((ReturnOrder) x).getOrderId(), "returnOrder");
        System.out.println("[test_1_leftJoin_D_innerJoin_D]--->");
        join.getJoinNodeList().forEach(x -> System.out.println(JSON.toJSON(x)));
    }


    public static void test_1_innerJoin_1() {
        User user1 = new User().setId("u1").setName("小明");
        User user2 = new User().setId("u2").setName("小红");
        User user3 = new User().setId("u3").setName("小张");
        List<User> userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);
        userList.add(user3);

        Order order1 = new Order().setId("o1").setUserId("u1").setOrderName("小明的订单");
        Order order2 = new Order().setId("o2").setUserId("u2").setOrderName("小红的订单");
        Order order3 = new Order().setId("o3").setUserId("u3").setOrderName("小张的订单");
        List<Order> orderList = new ArrayList<>();
        orderList.add(order1);
        orderList.add(order2);
//        orderList.add(order3);

        Join join = JoinUtil.innerJoin(userList, x -> x.getId(), "user", x -> orderList, x -> x.getUserId(), "order");
        System.out.println("[test_1_innerJoin_1] --->");
        join.getJoinNodeList().forEach(x -> System.out.println(JSON.toJSON(x)));
    }

    public static void test_1_innerJoin_D() {
        User user1 = new User().setId("u1").setName("小明");
        User user2 = new User().setId("u2").setName("小红");
        User user3 = new User().setId("u3").setName("小张");
        List<User> userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);
        userList.add(user3);

        Order order1 = new Order().setId("o1").setUserId("u1").setOrderName("小明的订单");
        Order order2 = new Order().setId("o2").setUserId("u2").setOrderName("小红的订单");
        Order order3 = new Order().setId("o3").setUserId("u3").setOrderName("小张的订单");
        Order order4 = new Order().setId("o4").setUserId("u3").setOrderName("小张的订单22");
        List<Order> orderList = new ArrayList<>();
        orderList.add(order1);
//        orderList.add(order2);
        orderList.add(order3);
        orderList.add(order4);

        Join join = JoinUtil.innerJoin(userList, x -> x.getId(), "user", x -> orderList, x -> x.getUserId(), "order");
        System.out.println("[test_1_innerJoin_D] --->");
        join.getJoinNodeList().forEach(x -> System.out.println(JSON.toJSON(x)));
    }

    public static void test_1_innerJoin_1_innerJoin_1() {
        User user1 = new User().setId("u1").setName("小明");
        User user2 = new User().setId("u2").setName("小红");
        User user3 = new User().setId("u3").setName("小张");
        List<User> userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);
        userList.add(user3);

        Order order1 = new Order().setId("o1").setUserId("u1").setOrderName("小明的订单");
        Order order2 = new Order().setId("o2").setUserId("u2").setOrderName("小红的订单");
        Order order3 = new Order().setId("o3").setUserId("u3").setOrderName("小张的订单");
        List<Order> orderList = new ArrayList<>();
        orderList.add(order1);
        orderList.add(order2);
        orderList.add(order3);

        ReturnOrder returnOrder1 = new ReturnOrder().setId("r1").setOrderId("o1").setUserId("u1").setReturnOrderName("小明的退单");
        ReturnOrder returnOrder2 = new ReturnOrder().setId("r2").setOrderId("o2").setUserId("u2").setReturnOrderName("小红的退单");
        ReturnOrder returnOrder3 = new ReturnOrder().setId("r3").setOrderId("o3").setUserId("u3").setReturnOrderName("小张的退单");
        List<ReturnOrder> returnOrderList = new ArrayList<>();
        returnOrderList.add(returnOrder1);
        returnOrderList.add(returnOrder2);
        returnOrderList.add(returnOrder3);

        Join join = JoinUtil.innerJoin(userList, x -> x.getId(), "user", x -> orderList, x -> x.getUserId(), "order")
                .innerJoin("order", x -> ((Order) x).getId(), x -> returnOrderList, x -> ((ReturnOrder) x).getOrderId(), "returnOrder");
        System.out.println("[test_1_innerJoin_1_innerJoin_1]--->");
        join.getJoinNodeList().forEach(x -> System.out.println(JSON.toJSON(x)));
    }

    public static void test_1_innerJoin_D_innerJoin_1() {
        User user1 = new User().setId("u1").setName("小明");
        User user2 = new User().setId("u2").setName("小红");
        User user3 = new User().setId("u3").setName("小张");
        List<User> userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);
        userList.add(user3);

        Order order1 = new Order().setId("o1").setUserId("u1").setOrderName("小明的订单");
        Order order2 = new Order().setId("o2").setUserId("u2").setOrderName("小红的订单");
        Order order3 = new Order().setId("o3").setUserId("u3").setOrderName("小张的订单");
        Order order4 = new Order().setId("o4").setUserId("u3").setOrderName("小张的订单22");
        List<Order> orderList = new ArrayList<>();
        orderList.add(order1);
        orderList.add(order2);
        orderList.add(order3);
        orderList.add(order4);

        ReturnOrder returnOrder1 = new ReturnOrder().setId("r1").setOrderId("o1").setUserId("u1").setReturnOrderName("小明的退单");
        ReturnOrder returnOrder2 = new ReturnOrder().setId("r2").setOrderId("o2").setUserId("u2").setReturnOrderName("小红的退单");
        ReturnOrder returnOrder3 = new ReturnOrder().setId("r3").setOrderId("o3").setUserId("u3").setReturnOrderName("小张的退单");
        ReturnOrder returnOrder4 = new ReturnOrder().setId("r4").setOrderId("o4").setUserId("u3").setReturnOrderName("小张的退单22");
        List<ReturnOrder> returnOrderList = new ArrayList<>();
        returnOrderList.add(returnOrder1);
        returnOrderList.add(returnOrder2);
        returnOrderList.add(returnOrder3);
        returnOrderList.add(returnOrder4);

        Join join = JoinUtil.innerJoin(userList, x -> x.getId(), "user", x -> orderList, x -> x.getUserId(), "order")
                .innerJoin("order", x -> ((Order) x).getId(), x -> returnOrderList, x -> ((ReturnOrder) x).getOrderId(), "returnOrder");
        System.out.println("[test_1_innerJoin_D_innerJoin_1]--->");
        join.getJoinNodeList().forEach(x -> System.out.println(JSON.toJSON(x)));
    }

    public static void test_1_innerJoin_D_innerJoin_D() {
        User user1 = new User().setId("u1").setName("小明");
        User user2 = new User().setId("u2").setName("小红");
        User user3 = new User().setId("u3").setName("小张");
        List<User> userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);
        userList.add(user3);

        Order order1 = new Order().setId("o1").setUserId("u1").setOrderName("小明的订单");
        Order order2 = new Order().setId("o2").setUserId("u2").setOrderName("小红的订单");
        Order order3 = new Order().setId("o3").setUserId("u3").setOrderName("小张的订单");
        Order order4 = new Order().setId("o4").setUserId("u3").setOrderName("小张的订单222");
        List<Order> orderList = new ArrayList<>();
        orderList.add(order1);
        orderList.add(order2);
        orderList.add(order3);
        orderList.add(order4);

        ReturnOrder returnOrder1 = new ReturnOrder().setId("r1").setOrderId("o1").setUserId("u1").setReturnOrderName("小明的退单");
        ReturnOrder returnOrder2 = new ReturnOrder().setId("r2").setOrderId("o2").setUserId("u2").setReturnOrderName("小红的退单");
        ReturnOrder returnOrder3 = new ReturnOrder().setId("r3").setOrderId("o3").setUserId("u3").setReturnOrderName("小张的退单");
        ReturnOrder returnOrder4 = new ReturnOrder().setId("r4").setOrderId("o3").setUserId("u3").setReturnOrderName("小张的退单222");
        List<ReturnOrder> returnOrderList = new ArrayList<>();
        returnOrderList.add(returnOrder1);
        returnOrderList.add(returnOrder2);
        returnOrderList.add(returnOrder3);
        returnOrderList.add(returnOrder4);

        Join join = JoinUtil.innerJoin(userList, x -> x.getId(), "user", x -> orderList, x -> x.getUserId(), "order");
//                .innerJoin("order", x -> ((Order) x).getId(), x -> returnOrderList, x -> ((ReturnOrder) x).getOrderId(), "returnOrder");
        System.out.println("[test_1_innerJoin_D_innerJoin_D]--->");
        join.getJoinNodeList().forEach(x -> System.out.println(JSON.toJSON(x)));
    }


    public static void test_1_innerJoin_1_leftJoin_1() {
        User user1 = new User().setId("u1").setName("小明");
        User user2 = new User().setId("u2").setName("小红");
        User user3 = new User().setId("u3").setName("小张");
        List<User> userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);
        userList.add(user3);

        Order order1 = new Order().setId("o1").setUserId("u1").setOrderName("小明的订单");
        Order order2 = new Order().setId("o2").setUserId("u2").setOrderName("小红的订单");
        Order order3 = new Order().setId("o3").setUserId("u3").setOrderName("小张的订单");
        List<Order> orderList = new ArrayList<>();
        orderList.add(order1);
        orderList.add(order2);
        orderList.add(order3);

        ReturnOrder returnOrder1 = new ReturnOrder().setId("r1").setOrderId("o1").setUserId("u1").setReturnOrderName("小明的退单");
        ReturnOrder returnOrder2 = new ReturnOrder().setId("r2").setOrderId("o2").setUserId("u2").setReturnOrderName("小红的退单");
        ReturnOrder returnOrder3 = new ReturnOrder().setId("r3").setOrderId("o3").setUserId("u3").setReturnOrderName("小张的退单");
        List<ReturnOrder> returnOrderList = new ArrayList<>();
        returnOrderList.add(returnOrder1);
        returnOrderList.add(returnOrder2);
        returnOrderList.add(returnOrder3);

        Join join = JoinUtil.innerJoin(userList, x -> x.getId(), "user", x -> orderList, x -> x.getUserId(), "order")
                .leftJoin("order", x -> ((Order) x).getId(), x -> returnOrderList, x -> ((ReturnOrder) x).getOrderId(), "returnOrder");
        System.out.println("[test_1_innerJoin_1_leftJoin_1]--->");
        join.getJoinNodeList().forEach(x -> System.out.println(JSON.toJSON(x)));
    }

    public static void test_1_innerJoin_D_leftJoin_1() {
        User user1 = new User().setId("u1").setName("小明");
        User user2 = new User().setId("u2").setName("小红");
        User user3 = new User().setId("u3").setName("小张");
        List<User> userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);
        userList.add(user3);

        Order order1 = new Order().setId("o1").setUserId("u1").setOrderName("小明的订单");
        Order order2 = new Order().setId("o2").setUserId("u2").setOrderName("小红的订单");
        Order order3 = new Order().setId("o3").setUserId("u3").setOrderName("小张的订单");
        Order order4 = new Order().setId("o4").setUserId("u3").setOrderName("小张的订单2");
        List<Order> orderList = new ArrayList<>();
        orderList.add(order1);
        orderList.add(order2);
        orderList.add(order3);
        orderList.add(order4);

        ReturnOrder returnOrder1 = new ReturnOrder().setId("r1").setOrderId("o1").setUserId("u1").setReturnOrderName("小明的退单");
        ReturnOrder returnOrder2 = new ReturnOrder().setId("r2").setOrderId("o2").setUserId("u2").setReturnOrderName("小红的退单");
        ReturnOrder returnOrder3 = new ReturnOrder().setId("r3").setOrderId("o3").setUserId("u3").setReturnOrderName("小张的退单");
        List<ReturnOrder> returnOrderList = new ArrayList<>();
        returnOrderList.add(returnOrder1);
        returnOrderList.add(returnOrder2);
        returnOrderList.add(returnOrder3);

        Join join = JoinUtil.leftJoin(userList, x -> x.getId(), "user", x -> orderList, x -> x.getUserId(), "order")
                .leftJoin("order", x -> ((Order) x).getId(), x -> returnOrderList, x -> ((ReturnOrder) x).getOrderId(), "returnOrder");
        System.out.println("[test_1_innerJoin_D_leftJoin_1]--->");
        join.getJoinNodeList().forEach(x -> System.out.println(JSON.toJSON(x)));
    }

    public static void test_1_innerJoin_D_leftJoin_D() {
        User user1 = new User().setId("u1").setName("小明");
        User user2 = new User().setId("u2").setName("小红");
        User user3 = new User().setId("u3").setName("小张");
        List<User> userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);
        userList.add(user3);

        Order order1 = new Order().setId("o1").setUserId("u1").setOrderName("小明的订单");
        Order order2 = new Order().setId("o2").setUserId("u2").setOrderName("小红的订单");
        Order order3 = new Order().setId("o3").setUserId("u3").setOrderName("小张的订单");
        Order order4 = new Order().setId("o4").setUserId("u3").setOrderName("小张的订单22");
        List<Order> orderList = new ArrayList<>();
        orderList.add(order1);
        orderList.add(order2);
        orderList.add(order3);
        orderList.add(order4);

        ReturnOrder returnOrder1 = new ReturnOrder().setId("r1").setOrderId("o1").setUserId("u1").setReturnOrderName("小明的退单");
        ReturnOrder returnOrder2 = new ReturnOrder().setId("r2").setOrderId("o2").setUserId("u2").setReturnOrderName("小红的退单");
        ReturnOrder returnOrder3 = new ReturnOrder().setId("r3").setOrderId("o3").setUserId("u3").setReturnOrderName("小张的退单");
        ReturnOrder returnOrder5 = new ReturnOrder().setId("r5").setOrderId("o3").setUserId("u3").setReturnOrderName("小张的退单222");
        List<ReturnOrder> returnOrderList = new ArrayList<>();
        returnOrderList.add(returnOrder1);
        returnOrderList.add(returnOrder2);
        returnOrderList.add(returnOrder3);
//        returnOrderList.add(returnOrder4);
        returnOrderList.add(returnOrder5);


        Join join = JoinUtil.leftJoin(userList, x -> x.getId(), "user", x -> orderList, x -> x.getUserId(), "order")
                .leftJoin("order", x -> ((Order) x).getId(), x -> returnOrderList, x -> ((ReturnOrder) x).getOrderId(), "returnOrder");
        System.out.println("[test_1_innerJoin_D_leftJoin_D]--->");
        join.getJoinNodeList().forEach(x -> System.out.println(JSON.toJSON(x)));
    }



}
