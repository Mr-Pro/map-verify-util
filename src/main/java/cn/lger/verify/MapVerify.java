package cn.lger.verify;

import cn.lger.entity.VerifyEntity;
import cn.lger.exception.VerifyException;
import cn.lger.exception.VerifyUtilInitException;
import cn.lger.parser.EntityFactory;
import cn.lger.parser.EntityParser;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Code that Changed the World
 *
 * @author Pro
 * @date 2019-03-30.
 */
public class MapVerify {

    /**
     * 保存解析后的Entity,key为EntityName
     */
    private static Map<String, VerifyEntity> entityMap;

    /**
     * 是否已经初始化
     */
    private static volatile boolean isInit = false;

    /**
     * 初始化Map验证工具
     *
     * @param xmls xml文件数组
     */
    synchronized public static void init(String[] xmls) {
        List<Document> documents = getDocs(xmls);
        entityMap = EntityParser.parse(documents, null);
        MapVerify.isInit = true;
    }

    /**
     * 初始化Map验证工具
     *
     * @param xmls xml文件数组
     */
    synchronized public static void init(String[] xmls, EntityFactory factory) {
        List<Document> documents = getDocs(xmls);
        entityMap = EntityParser.parse(documents, factory);
        MapVerify.isInit = true;
    }

    private static List<Document> getDocs(String[] xmls) {
        if (MapVerify.isInit) {
            throw new VerifyUtilInitException("Map verify util is run.");
        }
        if (xmls == null || xmls.length == 0) {
            throw new VerifyUtilInitException("XML Path is not null.");
        }
        return xmlValidAndRead(xmls);
    }

    /**
     * 验证过程出错将出错结果使用数组方式返回
     *
     * @param entityName 需要验证的实体名
     * @return 错误结果集
     */
    public static String[] verifyByReturnArr(String entityName, Map map) {
        if (!MapVerify.isInit) {
            throw new VerifyUtilInitException("Map verify util is not init.");
        }
        if (entityMap.containsKey(entityName)) {
            return entityMap.get(entityName).verify(map);
        }
        throw new VerifyException(entityName + " entity is not found");
    }

    /**
     * 验证是否符合xsd定义要求并读取xml结果返回
     *
     * @param xmls xml文件全名数组
     * @return List<Document>
     */
    private static List<Document> xmlValidAndRead(String[] xmls) {
        SAXReader reader = new SAXReader(true);
        try {
            reader.setFeature("http://apache.org/xml/features/validation/schema", true);
            reader.setFeature("http://apache.org/xml/features/validation/schema-full-checking", true);
            reader.setProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema");
        } catch (SAXException e) {
            e.printStackTrace();
        }

        EntityResolver resolver = new EntityResolver() {
            @Override
            public InputSource resolveEntity(String publicId, String systemId) {
                InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("map-verify-util.xsd");
                return new InputSource(inputStream);
            }
        };
        reader.setEntityResolver(resolver);

        //读取符合xsd定义的xml document集
        List<Document> documents = new ArrayList<>(xmls.length);
        for (String xml : xmls) {
            try {
                InputStream inputStream = MapVerify.class.getClassLoader().getResourceAsStream(xml);
                documents.add(reader.read(inputStream));
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        }
        return documents;
    }

}
