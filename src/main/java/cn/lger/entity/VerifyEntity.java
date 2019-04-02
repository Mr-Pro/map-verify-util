package cn.lger.entity;


import cn.lger.parser.EntityFactory;
import org.dom4j.Element;

import java.util.Map;

/**
 * Code that Changed the World
 *
 * @author Pro
 * @date 2019-03-30.
 */
public interface VerifyEntity {

    /**
     * 一个实体初始化，当实体被创建时将由创建方主动调用
     * @param currentEle 被创建的实体节点
     * @param factory 实体创建工厂，可以通过此工厂创建Entity
     */
    void init(Element currentEle, EntityFactory factory);

    /**
     * 验证当前节点是否匹配XML的配置
     * @param value 需要验证的值
     * @return 异常字符串集
     */
    String[] verify(Object value);

    /**
     * 初始化完毕后调用，传入包含所有Entity的Map
     * @param entityMap entityMap
     */
    void finished(Map<String, VerifyEntity> entityMap);


}
