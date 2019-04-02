package cn.lger.entity;

import cn.lger.parser.EntityFactory;
import cn.lger.utils.Util;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Code that Changed the World
 *
 * @author Pro
 * @date 2019-03-30.
 */
public class StringEntity implements VerifyEntity {

    /**
     * 当前实体的名字
     */
    private String name;

    /**
     * 长度的等式
     */
    private AbstractEquation<Integer> stringLengthEquation;

    private boolean notNull = false;
    private String notNullErrMsg;

    private boolean notBlank = false;
    private String notBlankErrMsg;

    private List<PatternEle> patterns;


    @Override
    public void init(Element currentEle, EntityFactory factory) {
        this.name = currentEle.attributeValue("name");
        Element element = currentEle.element("notNull");
        if (element != null) {
            this.notNull = true;
            this.notNullErrMsg = this.name + ": " + element.attributeValue("errMsg");
            if (Util.isEmpty(this.notNullErrMsg)) {
                this.notNullErrMsg = this.name + " : this is not null";
            }
        }
        element = currentEle.element("notBlank");
        if (element != null) {
            //不能为空白则包括不能为空的情况
            this.notNull = true;
            this.notBlank = true;
            this.notBlankErrMsg = this.name + ": " + element.attributeValue("errMsg");
            if (Util.isEmpty(this.notBlankErrMsg)) {
                this.notBlankErrMsg = this.name + " : this is not blank";
            }
        }
        processLength(currentEle.element("length"));
        processPattern(currentEle.element("pattern"));
    }

    private void processPattern(Element pattern) {
        if (pattern != null) {
            this.patterns = new ArrayList<>(pattern.elements().size());
            for (Object o: pattern.elements()) {
                Element valEle = (Element) o;
                PatternEle patternEle = new PatternEle(valEle.getStringValue(), this.name + ": " +valEle.attributeValue("errMsg"));
                this.patterns.add(patternEle);
            }
        }
    }

    private void processLength(Element length) {
        if (length != null) {
            //标记为有length节点
            stringLengthEquation = new AbstractEquation<Integer>(length, this.name) {
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
        String str = value.toString();
        if (this.notBlank && Util.isBlank(str)) {
            return new String[]{this.notBlankErrMsg};
        }
        if (this.patterns != null) {
            for (PatternEle ele : this.patterns) {
                if (!ele.verify(str)) {
                    return new String[] {ele.patternErrMsg};
                }
            }
        }
        return stringLengthEquation.verify(str.length());
    }

    @Override
    public void finished(Map<String, VerifyEntity> entityMap) {

    }

    private class PatternEle {
        String pattern;
        String patternErrMsg;

        PatternEle(String pattern, String patternErrMsg) {
            this.pattern = pattern;
            this.patternErrMsg = patternErrMsg;
            if (Util.isEmpty(patternErrMsg)) {
                this.patternErrMsg = name + ": this is no matches pattern.";
            }
        }

        boolean verify(String value) {
            return !value.matches(pattern);
        }
    }

}
