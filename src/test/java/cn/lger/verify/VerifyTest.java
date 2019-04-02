package cn.lger.verify;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Code that Changed the World
 *
 * @author Pro
 * @date 2019-03-30.
 */
public class VerifyTest {

    private static ExecutorService executorService = Executors.newFixedThreadPool(20);

    public static void main(String[] args) {

        //初始化MapVerify，将定义好xml以数组方式传入
        final String[] xmls = {"demo.xml"};
        MapVerify.init(xmls);

        final Map<String, Object> map = new HashMap<>(5);
        map.put("username", "12312371237123778");
        map.put("age", 1);
        map.put("birthday", "2000-07-21");
        map.put("money", 149);

        List<Map<String, Object>> list = new ArrayList<>(2);
        Map<String, Object> map1 = new HashMap<>(1);
        map1.put("username", "abcab");
        Map<String, Object> map2 = new HashMap<>(1);
        map2.put("username", "cc");
        list.add(map1);
        list.add(map2);
        map.put("details", list);
        System.out.println(Arrays.toString(MapVerify.verifyByReturnArr("User01", map)));
        try {
            MapVerify.init(xmls);
        }catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(Arrays.toString(MapVerify.verifyByReturnArr("User01", map)));
//        for (int i = 0; i < 10000; i++) {
//            executorService.execute(new Runnable() {
//                @Override
//                public void run() {
////                    MapVerify.verifyByReturnArr("User01", map);
//                    System.out.println(Arrays.toString(MapVerify.verifyByReturnArr("User01", map)));
//                }
//            });
//        }
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(10);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                MapVerify.init(xmls);
//            }
//        }).start();

        //通过verifyByReturnArr方法验证，验证后返回数组
//        System.out.println(Arrays.toString(MapVerify.verifyByReturnArr("User01", map)));
    }

}
