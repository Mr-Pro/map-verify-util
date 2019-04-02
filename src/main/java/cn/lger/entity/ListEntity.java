package cn.lger.entity;

import cn.lger.exception.VerifyUtilInitException;
import cn.lger.parser.EntityFactory;
import cn.lger.utils.Util;
import org.dom4j.Element;

import java.util.List;
import java.util.Map;

/**
 * Code that Changed the World
 *
 * @author Pro
 * @date 2019-03-30.
 */
public class ListEntity implements VerifyEntity {

    private Map<String, VerifyEntity> entityMap;

    private AbstractEquation<Integer> listSizeEquation;

    private boolean notNull = false;
    private String notNullErrMsg;
    private boolean notEmpty = false;
    private String notEmptyErrMsg;

    private String castErrMsg;

    private String detailEntityName;

    @Override
    public void init(Element currentEle, EntityFactory factory) {
        String name = currentEle.attributeValue("name");
        Element element = currentEle.element("notNull");
        if (element != null) {
            this.notNullErrMsg = element.attributeValue("errMsg");
            this.notNull = true;
            if (Util.isEmpty(this.notNullErrMsg)) {
                this.notNullErrMsg = name + ": this is not null";
            } else {
                this.notNullErrMsg = name + ": " + this.notNullErrMsg;
            }
        }
        element = currentEle.element("notEmpty");
        if (element != null) {
            this.notEmptyErrMsg = element.attributeValue("errMsg");
            this.notEmpty = true;
            if (Util.isEmpty(this.notEmptyErrMsg)) {
                this.notEmptyErrMsg = name + ": this is not empty";
            } else {
                this.notEmptyErrMsg = name + ": " + this.notEmptyErrMsg;
            }
        }
        this.castErrMsg = currentEle.attributeValue("castErrMsg");
        if (Util.isEmpty(this.castErrMsg)) {
            this.castErrMsg = name + ": this is not list type.";
        } else {
            this.castErrMsg = name + ": " + this.castErrMsg;
        }
        element = currentEle.element("forEach");
        if (element != null) {
            this.detailEntityName = element.attributeValue("entity");
            listSizeEquation = new AbstractEquation<Integer>(element, name) {
                @Override
                Integer valueOf(String value) {
                    return Integer.valueOf(value);
                }

                @Override
                boolean lessThan(Integer value, Integer lt) {
                    return value < lt;
                }

                @Override
                boolean greaterThan(Integer value, Integer gt) {
                    return value > gt;
                }

                @Override
                boolean lessThanOrEquals(Integer value, Integer lte) {
                    return value <= lte;
                }

                @Override
                boolean greaterThanOrEquals(Integer value, Integer gte) {
                    return value <= gte;
                }

                @Override
                boolean equals(Integer value, Integer eq) {
                    return value.equals(eq);
                }
            };
        }

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
            List list = (List) value;
            if (this.notEmpty && list.size() == 0) {
                return new String[]{this.notEmptyErrMsg};
            }
            String[] results = listSizeEquation.verify(list.size());
            if (results != null && results.length > 0) {
                return results;
            }
            if (!Util.isEmpty(this.detailEntityName)) {
                VerifyEntity entity = entityMap.get(this.detailEntityName);
                for (Object o : list) {
                    results = entity.verify(o);
                    if (results != null && results.length > 0) {
                        return results;
                    }
                }
            }
            return null;
        }catch (Exception e) {
            return new String[]{this.castErrMsg};
        }
    }

    @Override
    public void finished(Map<String, VerifyEntity> entityMap) {
        this.entityMap = entityMap;
        if (!Util.isEmpty(this.detailEntityName) && !this.entityMap.containsKey(this.detailEntityName)) {
            throw new VerifyUtilInitException("Current entity ["+this.detailEntityName+"] is not exist.");
        }
    }
}
