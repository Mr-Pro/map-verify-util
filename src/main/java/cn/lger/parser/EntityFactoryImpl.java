package cn.lger.parser;

import cn.lger.entity.*;
import cn.lger.exception.VerifyUtilInitException;

/**
 * Code that Changed the World
 *
 * @author Pro
 * @date 2019-04-02.
 */
public class EntityFactoryImpl implements EntityFactory{

    @Override
    public VerifyEntity getEntity(String name) {
        switch (name) {
            case "Date":
                return new DateEntity();
            case "String":
                return new StringEntity();
            case "List":
                return new ListEntity();
            case "Double":
                return new DoubleEntity();
            case "Integer":
                return new IntegerEntity();
            case "Entity":
                return new RootEntity();
            default:
                throw new VerifyUtilInitException("Current Doc Name is not exist.");
        }

    }
}
