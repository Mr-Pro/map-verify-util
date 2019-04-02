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
public class DoubleEntity implements VerifyEntity {

    private AbstractEquation<Double> doubleEquation;

    private boolean notNull = false;
    private String notNullErrMsg;
    private String castErrMsg;

    @Override
    public void init(Element currentEle, EntityFactory factory) {
        String name = currentEle.attributeValue("name");
        this.castErrMsg = currentEle.attributeValue("castErrMsg");
        Element element = currentEle.element("notNull");
        if (element != null) {
            this.notNull = true;
            String errMsg = element.attributeValue("errMsg");
            this.notNullErrMsg = name + ": " + errMsg;
            if (Util.isEmpty(errMsg)) {
                this.notNullErrMsg = name + ": this is not null";
            }
        }
        if (Util.isEmpty(this.castErrMsg)) {
            this.castErrMsg = name + ": this is not double type.";
        } else {
            this.castErrMsg = name + ": " + this.castErrMsg;
        }

        doubleEquation = new AbstractEquation<Double>(currentEle, name) {
            @Override
            Double valueOf(String value) {
                try {
                    return Double.valueOf(value);
                }catch (NumberFormatException e) {
                    throw new ConvertException(castErrMsg);
                }
            }

            @Override
            boolean lessThan(Double value, Double lt) {
                return lt != null && value < lt;
            }

            @Override
            boolean greaterThan(Double value, Double gt) {
                return gt != null && value > gt;
            }

            @Override
            boolean lessThanOrEquals(Double value, Double lte) {
                return lte != null && value <= lte;
            }

            @Override
            boolean greaterThanOrEquals(Double value, Double gte) {
                return gte != null && value <= gte;
            }

            @Override
            boolean equals(Double value, Double eq) {
                return value.equals(eq);
            }
        };

    }

    @Override
    public String[] verify(Object value) {
        if (this.notNull && value == null) {
            return new String[]{this.notNullErrMsg};
        }
        try {
            return doubleEquation.verify(Double.valueOf(value.toString()));
        } catch (NumberFormatException e) {
            return new String[]{this.castErrMsg};
        }
    }

    @Override
    public void finished(Map<String, VerifyEntity> entityMap) {

    }

}
