package com.zhang.http;

import com.zhang.http.annotation.Controller;
import com.zhang.http.annotation.RequestMapping;
import com.zhang.http.model.Router;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;

public class ClassPathXMLApplicationContext {

    private final String resources;

    public ClassPathXMLApplicationContext(String resources) {
        this.resources = resources;
    }

    public Map<String, Router> getRouters() {
        Map<String, Router> routers = new HashMap<>();
        Document document = null;
        SAXReader saxReader = new SAXReader();
        try {
            ClassLoader classLoader = getClassLoader();
            document = saxReader.read(classLoader.getResourceAsStream(resources));
            Element beans = document.getRootElement();
            String packageName = "";
            Iterator<Element> beansList = beans.elementIterator();
            beansList.hasNext();
            Element element = beansList.next();
            packageName = element.attributeValue("class");

            Set<Class<?>> classes = classSet(packageName);
            System.out.println(classes);

            processAnnotation(classes,routers);

            System.out.println(routers);

        } catch (DocumentException e) {
            e.printStackTrace();
            System.out.println("error in reading resources file....");
        }
        return routers;
    }

    private Set<Class<?>> classSet(String packageName) {
        Set<Class<?>> classes = new HashSet<>();
        try {
            Enumeration<URL> urls = getClassLoader().getResources(packageName.replace(".", "/"));
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                if (url != null) {
                    String protocol = url.getProtocol();

                    String packagePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    addClass(classes, packagePath, packageName);
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return classes;
    }

    private ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }


    private  void addClass(Set<Class<?>> classSet, String packagePath, String packageName) {

        File[] files = new File(packagePath).listFiles(
                f -> (f.isFile() && f.getName().endsWith(".class") || f.isDirectory())
        );
        for (File file : files) {
            String fileName = file.getName();
            if (file.isFile()) {
                String className = fileName.substring(0, fileName.lastIndexOf("."));
                if (StringUtils.isNotEmpty(packageName)) {
                    className = packageName + "." + className;
                }
                doAddClass(classSet, className);
            } else {
                String subPackagePath = fileName;
                if (StringUtils.isNotEmpty(packagePath)) {
                    subPackagePath = packagePath + "/" + subPackagePath;
                }
                String subPackageName = fileName;
                if (StringUtils.isNotEmpty(packageName)) {
                    subPackageName = packageName + "." + subPackageName;
                }
                addClass(classSet, subPackagePath, subPackageName);
            }
        }
    }

    private  void doAddClass(Set<Class<?>> classSet, String className) {
        try {
            Class<?> cls = Class.forName(className);
            classSet.add(cls);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void processAnnotation(Set<Class<?>> classes,Map<String, Router> routers){

        classes.forEach( clazz ->{
            boolean isController = clazz.isAnnotationPresent(Controller.class);
            if (isController){
                if (clazz.isAnnotationPresent(RequestMapping.class)){
                    StrBuilder sb = new StrBuilder();
                    RequestMapping root=clazz.getAnnotation(RequestMapping.class);
                    sb.append(root.value());
                    Method[] methods=clazz.getDeclaredMethods();
                    for (Method e:methods){
                        RequestMapping leaf=e.getAnnotation(RequestMapping.class);
                        if (null != leaf){
                            sb.append(leaf.value());
                            routers.put(sb.toString(),new Router(clazz,e.getName()));
                        }
                    }
                }else {
                    System.err.println("not mapped controller "+clazz);
                }
            }
        });

    }

    public static void main(String[] args) {
        ClassPathXMLApplicationContext context = new ClassPathXMLApplicationContext("Package.xml");
       context.getRouters();
    }
}
