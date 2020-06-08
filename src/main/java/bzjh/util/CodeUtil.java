package bzjh.util;

import bzjh.pojo.message.WechatMessage;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;


public class CodeUtil {

    public static Document xmlToDocument(String xml) {
        Document document = null;
        try {
            document = DocumentHelper.parseText(xml);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return document;
    }

    public static String messagetoXml(WechatMessage message) {
        if (message != null) {
            Class clazz = message.getClass();
            if (clazz.isAnnotationPresent(XmlRootElement.class)) {
                Document document = DocumentHelper.createDocument();
                Element element = document.addElement("xml");
                Field[] fields = getPrivateField(message.getClass());
                for (Field field : fields) {
                    if (field.isAnnotationPresent(XmlElement.class)) {
                        XmlElement xmlElement = field.getAnnotation(XmlElement.class);
                        field.setAccessible(true);
                        try {
                            element.addElement(xmlElement.name()).addCDATA(field.get(message).toString());
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }

                return document.asXML();
            }
        }
        return null;
    }

    private static Field[] getPrivateField(Class clazz) {
        Field[] supers = new Field[0];
        if (clazz.getSuperclass() != null) {
            supers = getPrivateField(clazz.getSuperclass());
        }
        Field[] fields = clazz.getDeclaredFields();
        Field[] fields1 = new Field[supers.length + fields.length];
        System.arraycopy(supers, 0, fields1, 0, supers.length);
        System.arraycopy(fields, 0, fields1, supers.length, fields.length);
        return fields1;
    }

    public static <T extends WechatMessage> T xmlToMessage(Class<T> clazz, String xml) throws Exception {
        if (clazz.isAnnotationPresent(XmlRootElement.class)) {
            Document document = DocumentHelper.parseText(xml.replaceAll("\r|\n",""));
            Element root = document.getRootElement();
            Field[] fields = getPrivateField(clazz);
            T wechatMessage = null;
            wechatMessage = clazz.newInstance();
            for (Field field : fields) {
                if (field.isAnnotationPresent(XmlElement.class)) {
                    XmlElement xmlElement = field.getAnnotation(XmlElement.class);
                    Object object = convertValType(root.element(xmlElement.name()).getStringValue().trim(), field.getType());
                    clazz.getMethod("set" + xmlElement.name(), field.getType()).invoke(wechatMessage, field.getType().cast(object));
                }
            }
            return wechatMessage;
        }
        return null;
    }

    /**
     * 从输入流中读取到字符串中
     *
     * @param inputStream 输流
     * @return
     * @throws IOException
     */
    public static String ReadInputStreamToString(InputStream inputStream) throws IOException {
        InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
        char[] data = new char[1024];
        int dataSize = 0;
        StringBuilder stringBuilder = new StringBuilder();
        while ((dataSize = reader.read(data)) != -1) {
            stringBuilder.append(data, 0, dataSize);
        }
        return stringBuilder.toString();
    }

    public static Object convertValType(Object value, Class fieldTypeClass) {
        Object retVal = null;
        if (Long.class.getName().equals(fieldTypeClass.getName())
                || long.class.getName().equals(fieldTypeClass.getName())) {
            retVal = Long.parseLong(value.toString());
        } else if (Integer.class.getName().equals(fieldTypeClass.getName())
                || int.class.getName().equals(fieldTypeClass.getName())) {
            retVal = Integer.parseInt(value.toString());
        } else if (Float.class.getName().equals(fieldTypeClass.getName())
                || float.class.getName().equals(fieldTypeClass.getName())) {
            retVal = Float.parseFloat(value.toString());
        } else if (Double.class.getName().equals(fieldTypeClass.getName())
                || double.class.getName().equals(fieldTypeClass.getName())) {
            retVal = Double.parseDouble(value.toString());
        } else {
            retVal = value;
        }
        return retVal;
    }
}
