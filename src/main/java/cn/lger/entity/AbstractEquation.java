package cn.lger.entity;

import cn.lger.exception.ConvertException;
import cn.lger.utils.Util;
import org.dom4j.Element;

/**
 * Code that Changed the World
 *
 * @author Pro
 * @date 2019-03-31.
 */
public abstract class AbstractEquation<T> {

    protected T lt;
    protected String ltErrMsg;
    protected T lte;
    protected String lteErrMsg;
    protected T gt;
    protected String gtErrMsg;
    protected T gte;
    protected String gteErrMsg;
    protected T eq;
    protected String eqErrMsg;

    private String name;
    private Class<T> clazz;

    /**
     * 初始化等式
     *
     * @param parent 当前包含等式的父级节点Element
     * @param name   信息中包含的名字
     */
    public AbstractEquation(Element parent, String name) {
        this.name = name;
        Element current = parent.element("lt");
        String errMsg;
        if (current != null) {
            errMsg = current.attributeValue("errMsg");
            this.lt = this.valueOf(current.getStringValue());
            this.ltErrMsg = name + ": " + errMsg;
            if (Util.isEmpty(errMsg)) {
                this.ltErrMsg = name + " : this value is less than " + this.lt;
            }
        }
        current = parent.element("gt");
        if (current != null) {
            errMsg = current.attributeValue("errMsg");
            this.gt = this.valueOf(current.getStringValue());
            this.gtErrMsg = name + ": " + errMsg;
            if (Util.isEmpty(errMsg)) {
                this.gtErrMsg = name + " : this value is greater than " + this.gt;
            }
        }
        current = parent.element("lte");
        if (current != null) {
            errMsg = current.attributeValue("errMsg");
            this.lte = this.valueOf(current.getStringValue());
            this.lteErrMsg = name + ": " + errMsg;
            if (Util.isEmpty(errMsg)) {
                this.lteErrMsg = name + " : this value is less than or equal " + this.lte;
            }
        }
        current = parent.element("gte");
        if (current != null) {
            errMsg = current.attributeValue("errMsg");
            this.gte = this.valueOf(current.getStringValue());
            this.gteErrMsg = name + ": " + errMsg;
            if (Util.isEmpty(errMsg)) {
                this.gteErrMsg = name + " : this value is less than or equal " + this.gte;
            }
        }
        current = parent.element("eq");
        if (current != null) {
            errMsg = current.attributeValue("errMsg");
            this.eq = this.valueOf(current.getStringValue());
            this.eqErrMsg = name + ": " + errMsg;
            if (Util.isEmpty(errMsg)) {
                this.eqErrMsg = name + " : this value is equal " + this.eq;
            }
        }
    }

    /**
     * 通过该函数转化lt等参数的值
     *
     * @param value 需要被转化的字符串值
     * @return 转化后的结果
     */
    abstract T valueOf(String value);

    /**
     * 是否小于当前值
     *
     * @param value 当前值
     * @param lt    配置的期望值
     * @return 小于 true 反之 false
     */
    abstract boolean lessThan(T value, T lt);

    /**
     * 是否大于当前值
     *
     * @param value 当前值
     * @param gt    配置的期望值
     * @return 大于 true 反之 false
     */
    abstract boolean greaterThan(T value, T gt);

    /**
     * 是否小于等于当前值
     *
     * @param value 当前值
     * @param lte   配置的期望值
     * @return 小于等于 true 反之 false
     */
    abstract boolean lessThanOrEquals(T value, T lte);

    /**
     * 是否大于等于当前值
     *
     * @param value 当前值
     * @param gte   配置的期望值
     * @return 大于等于 true 反之 false
     */
    abstract boolean greaterThanOrEquals(T value, T gte);

    /**
     * 是否等于当前值
     *
     * @param value 当前值
     * @param eq    配置的期望值
     * @return 等于 true 反之 false
     */
    abstract boolean equals(T value, T eq);

    /**
     * 验证是否符合等式
     *
     * @param value 验证的值
     * @return 验证结果集
     */
    public String[] verify(T value) {
        if (this.lt != null && lessThan(value, lt)) {
            return new String[]{this.ltErrMsg};
        }
        if (this.lte != null && lessThanOrEquals(value, this.lte)) {
            return new String[]{this.lteErrMsg};
        }
        if (this.gt != null && greaterThan(value, this.gt)) {
            return new String[]{this.gtErrMsg};
        }
        if (this.gte != null && greaterThanOrEquals(value, this.gte)) {
            return new String[]{this.gteErrMsg};
        }
        if (this.eq != null && equals(value, this.eq)) {
            return new String[]{this.eqErrMsg};
        }
        return null;
    }

}
