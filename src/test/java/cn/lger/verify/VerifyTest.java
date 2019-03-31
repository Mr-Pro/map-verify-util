package cn.lger.verify;

import java.util.*;

/**
 * Code that Changed the World
 *
 * @author Pro
 * @date 2019-03-30.
 */
public class VerifyTest {

    public static void main(String[] args) {
        //初始化MapVerify，将定义好xml以数组方式传入
        String[] xmls = {"demo.xml"};
        MapVerify.init(xmls);

        Map<String, Object> map = new HashMap<>(5);
        map.put("username", "12312371237123778");
        map.put("age", 4);
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

        //通过verifyByReturnArr方法验证，验证后返回数组
        System.out.println(Arrays.toString(MapVerify.verifyByReturnArr("User01", map)));
    }

}
