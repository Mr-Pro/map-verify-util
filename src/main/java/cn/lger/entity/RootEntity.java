package cn.lger.entity;

import cn.lger.parser.EntityFactory;
import cn.lger.utils.Util;
import org.dom4j.Element;

import java.util.HashMap;
import java.util.Map;

/**
 * Code that Changed the World
 *
 * @author Pro
 * @date 2019-03-30.
 */
public class RootEntity implements VerifyEntity {

    /**
     * 保存一个Entity中所有的子节点信息
     */
    private Map<String, VerifyEntity> childEles;

    @Override
    public void init(Element currentEle, EntityFactory factory) {
        childEles = new HashMap<>(currentEle.elements().size());
        for (Object ele: currentEle.elements()){
            Element childEle = (Element) ele;
            VerifyEntity entity = factory.getEntity(childEle.getName());
            entity.init(childEle, factory);
            childEles.put(childEle.attributeValue("name"), entity);
        }
    }

    @Override
    public String[] verify(Object value) {
        if (!(value instanceof Map)) {
            return new String[]{"Current object is null or not map type."};
        }
        String[] result = {};
        Map map = (Map) value;
        // 由子节点判断当前的Map中各个参数是否合法
        for (Map.Entry<String, VerifyEntity> entry : childEles.entrySet()) {
            String[] temp = entry.getValue().verify(map.get(entry.getKey()));
            if (!Util.isEmpty(temp)) {
                // 如果子节点有返回错误则将错误信息数组连接
                result = Util.stringArrConcat(result, temp);
            }
        }
        return result;
    }

    @Override
    public void finished(Map<String, VerifyEntity> entityMap) {
        // 通知各个子节点解析完毕
        for (Map.Entry<String, VerifyEntity> entry : childEles.entrySet()) {
            entry.getValue().finished(entityMap);
        }
    }


}
