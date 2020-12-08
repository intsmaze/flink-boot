package com.intsmaze.flink.sql;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * github地址: https://github.com/intsmaze
 * 博客地址：https://www.cnblogs.com/intsmaze/
 * 出版书籍《深入理解Flink核心设计与实践原理》
 *
 * @auther: intsmaze(刘洋)
 * @date: 2020/10/15 18:33
 */
public class XmlUtils {

    /**
     * github地址: https://github.com/intsmaze
     * 博客地址：https://www.cnblogs.com/intsmaze/
     * 出版书籍《深入理解Flink核心设计与实践原理》
     *
     * @auther: intsmaze(刘洋)
     * @date: 2020/10/15 18:33
     */
    public static Map<String, String> readXMLForSql(String[] pathArr)
            throws Exception {
        Map<String, String> map = new HashMap<String, String>(10);
        if (pathArr != null) {
            for (String path : pathArr) {
                InputStream in = XmlUtils.class.getClassLoader().getResourceAsStream(path);
                SAXReader reader = new SAXReader();
                Document doc = reader.read(in);

                Element rootElement = doc.getRootElement();
                Element fooElement;

                for (Iterator i = rootElement.elementIterator("sql"); i
                        .hasNext(); ) {
                    fooElement = (Element) i.next();
                    Attribute attribute = fooElement.attribute("id");
                    String text = attribute.getText();
                    if (map.containsKey(text)) {
                        throw new Exception(text + "冲突");
                    }
                    map.put(text, fooElement.getText());
                }
            }
        }
        return map;
    }

    /**
     * github地址: https://github.com/intsmaze
     * 博客地址：https://www.cnblogs.com/intsmaze/
     * 出版书籍《深入理解Flink核心设计与实践原理》
     *
     * @auther: intsmaze(刘洋)
     * @date: 2020/10/15 18:33
     */
    public static Map<String, String> readXMLForSql(String pathArr)
            throws Exception {
        readFile(new File(pathArr));

        for (int i = 0; i < fileList.size(); i++) {
            File file1 = fileList.get(i);
            System.out.println(file1.getName() + "1111111");
        }
        Map<String, String> map = new HashMap<String, String>(10);
        return map;
    }

    private static List<File> fileList = new ArrayList<File>();

    /**
     * github地址: https://github.com/intsmaze
     * 博客地址：https://www.cnblogs.com/intsmaze/
     * 出版书籍《深入理解Flink核心设计与实践原理》
     *
     * @auther: intsmaze(刘洋)
     * @date: 2020/10/15 18:33
     */
    private static void readFile(File file) {
        File[] fs = file.listFiles();
        for (File f : fs) {
            if (f.isDirectory()) {
                readFile(f);
            }
            if (f.isFile()) {
                fileList.add(f);
            }
        }
    }

}
