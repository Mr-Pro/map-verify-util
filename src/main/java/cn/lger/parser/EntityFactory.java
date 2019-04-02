package cn.lger.parser;

import cn.lger.entity.*;

/**
 * Code that Changed the World
 *
 * @author Pro
 * @date 2019-03-30.
 */
public interface EntityFactory {

    /**
     * 获取实体
     * @param name 实体节点名
     * @return VerifyEntity
     */
    VerifyEntity getEntity(String name);

}
