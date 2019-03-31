package cn.lger.parser;

import cn.lger.entity.EntityFactory;
import cn.lger.entity.VerifyEntity;
import cn.lger.exception.VerifyException;
import org.dom4j.Document;
import org.dom4j.Element;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Code that Changed the World
 *
 * @author Pro
 * @date 2019-03-30.
 */
public class EntityParser {

    /**
     * 解析、初始化并返回存储VerifyEntity的Map
     * @param documents 多个XML对应的doc
     * @return EntityMap
     */
    public static Map<String, VerifyEntity> parse(List<Document> documents) {
        //entityMap用于存储解析后的VerifyEntity
        Map<String, VerifyEntity> entityMap = new HashMap<>(1 << 10);
        //遍历解析XML的doc
        for (Document document: documents) {
            // 根元素map-verify
            Element rootEle = document.getRootElement();

            //遍历根元素中的Entity元素
            Iterator iterator = rootEle.elementIterator();
            while (iterator.hasNext()) {
                Element rootEntityEle = (Element) iterator.next();
                String entityName = rootEntityEle.attributeValue("name");
                //判断当前是否存在相同的entity
                if (entityMap.containsKey(entityName)) {
                    new VerifyException("Current Entity Name :[" + entityName + "] is exist. It will be filled by new.").printStackTrace();
                }
                //通过工厂获取VerifyEntity
                VerifyEntity rootEntity = EntityFactory.getEntity(rootEntityEle.getName());
                //调用初始化方法
                rootEntity.init(rootEntityEle);
                //存储解析后的Entity
                entityMap.put(entityName, rootEntity);
            }
        }
        //解析存储完毕后遍历调用finished方法
        for (Map.Entry<String, VerifyEntity> entityEntry : entityMap.entrySet()) {
            entityEntry.getValue().finished(entityMap);
        }

        return entityMap;
    }

}
