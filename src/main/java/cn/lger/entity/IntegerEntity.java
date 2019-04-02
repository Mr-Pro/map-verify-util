package cn.lger.entity;

import cn.lger.exception.ConvertException;
import cn.lger.parser.EntityFactory;
import cn.lger.utils.Util;
import org.dom4j.Element;

import java.util.Map;

/**
 * Code that Changed the World
 *
 * @author Pro
 * @date 2019-03-30.
 */
public class IntegerEntity implements VerifyEntity {

    private AbstractEquation<Integer> integerEquation;

    private boolean notNull = false;
    private String notNullErrMsg;

    private String castErrMsg;

    @Override
    public void init(Element currentEle, EntityFactory factory) {
        String name = currentEle.attributeValue("name");
        this.castErrMsg = currentEle.attributeValue("castErrMsg");
        if (Util.isEmpty(this.castErrMsg)) {
            this.castErrMsg = name + ": this is not integer type.";
        } else {
            this.castErrMsg = name + ": " + this.castErrMsg;
        }

        Element element = currentEle.element("notNull");
        if (element != null) {
            this.notNull = true;
            this.notNullErrMsg = element.attributeValue("errMsg");
            if (Util.isEmpty(this.notNullErrMsg)) {
                this.notNullErrMsg = name + ": this is not null";
            } else {
                this.notNullErrMsg = name + ": " + this.notNullErrMsg;
            }
        }

        integerEquation = new AbstractEquation<Integer>(currentEle, name) {
            @Override
            Integer valueOf(String value) {
                try {
                    return Integer.valueOf(value);
                }catch (NumberFormatException e) {
                    throw new ConvertException(castErrMsg);
                }
            }
            @Override
            boolean lessThan(Integer value, Integer lt) {
                return lt != null && value < lt;
            }

            @Override
            boolean greaterThan(Integer value, Integer gt) {
                return gt != null && value > gt;
            }

            @Override
            boolean lessThanOrEquals(Integer value, Integer lte) {
                return lte != null && value <= lte;
            }

            @Override
            boolean greaterThanOrEquals(Integer value, Integer gte) {
                return gte != null && value <= gte;
            }

            @Override
            boolean equals(Integer value, Integer eq) {
                return value.equals(eq);
            }
        };

    }

    @Override
    public String[] verify(Object value) {
        if (value == null) {
            if (this.notNull) {
                return new String[]{this.notNullErrMsg};
            }
            return null;
        }
        try {
            return integerEquation.verify(Integer.valueOf(value.toString()));
        }catch (NumberFormatException e) {
            return new String[]{this.castErrMsg};
        }
    }

    @Override
    public void finished(Map<String, VerifyEntity> entityMap) {

    }

}
