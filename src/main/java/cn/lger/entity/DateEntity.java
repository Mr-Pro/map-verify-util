package cn.lger.entity;

import cn.lger.exception.ConvertException;
import cn.lger.parser.EntityFactory;
import cn.lger.utils.Util;
import org.dom4j.Element;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Code that Changed the World
 *
 * @author Pro
 * @date 2019-03-30.
 */
public class DateEntity implements VerifyEntity {

    private AbstractEquation<Date> dateEquation;

    private boolean notNull = false;
    private String notNullErrMsg;

    private String format;
    private String formatErrMsg;


    @Override
    public void init(Element currentEle, EntityFactory factory) {
        String name = currentEle.attributeValue("name");
        this.format = currentEle.attributeValue("format");
        this.formatErrMsg = currentEle.attributeValue("formatErrMsg");

        Element element = currentEle.element("notNull");
        if (element != null) {
            this.notNull = true;
            String errMsg = element.attributeValue("errMsg");
            this.notNullErrMsg = name + errMsg;
            if (Util.isEmpty(errMsg)) {
                this.notNullErrMsg = name + " : this is not null";
            }
        }

        if (Util.isEmpty(this.formatErrMsg)) {
            this.formatErrMsg = name + " : format error.";
        } else {
            this.formatErrMsg = name + ": " + this.formatErrMsg;
        }

        dateEquation = new AbstractEquation<Date>(currentEle, name) {
            @Override
            Date valueOf(String value) {
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat(format);
                    return dateFormat.parse(value);
                } catch (ParseException e) {
                    throw new ConvertException(formatErrMsg);
                }
            }

            @Override
            boolean lessThan(Date value, Date lt) {
                return value.before(lt);
            }

            @Override
            boolean greaterThan(Date value, Date gt) {
                return value.after(gt);
            }

            @Override
            boolean lessThanOrEquals(Date value, Date lte) {
                return value.before(lte) || value.equals(lte);
            }

            @Override
            boolean greaterThanOrEquals(Date value, Date gte) {
                return value.after(gte) || value.equals(gte);
            }

            @Override
            boolean equals(Date value, Date eq) {
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
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            Date date =  dateFormat.parse(value.toString());
            return dateEquation.verify(date);
        } catch (ParseException e) {
            return new String[]{this.formatErrMsg};
        }
    }

    @Override
    public void finished(Map<String, VerifyEntity> entityMap) {

    }
}
