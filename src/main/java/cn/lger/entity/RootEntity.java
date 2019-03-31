package cn.lger.entity;

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

    private Map<String, VerifyEntity> childEles;

    @Override
    public void init(Element currentEle) {
        childEles = new HashMap<>(currentEle.elements().size());
        for (Object ele: currentEle.elements()){
            Element childEle = (Element) ele;
            VerifyEntity entity = EntityFactory.getEntity(childEle.getName());
            entity.init(childEle);
            childEles.put(childEle.attributeValue("name"), entity);
        }
    }

    @Override
    public String[] verify(Object value) {
        if (!(value instanceof Map)) {
            return new String[]{"Current Object is null or other type"};
        }
        String[] result = {};
        Map map = (Map) value;
        for (Map.Entry<String, VerifyEntity> entry : childEles.entrySet()) {
            String[] temp = entry.getValue().verify(map.get(entry.getKey()));
            if (!Util.isEmpty(temp)) {
                result = Util.stringArrConcat(result, temp);
            }
        }
        return result;
    }

    @Override
    public void finished(Map<String, VerifyEntity> entityMap) {
        for (Map.Entry<String, VerifyEntity> entry : childEles.entrySet()) {
            entry.getValue().finished(entityMap);
        }
    }


}
