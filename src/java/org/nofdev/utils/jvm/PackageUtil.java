package org.nofdev.utils.jvm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.*;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class PackageUtil {
    private static final Logger log = LoggerFactory.getLogger(PackageUtil.class);

    private static final String CLASS_EXT = ".class";
    private static final String SPECIAL_NOT_CLASS_EXT = "$";
    private static final Character DOT = '.';
    private static final Character SLASH = '/';

    /**
     * Scans all classes accessible from the context class loader which belong to the given package and subpackages.
     *
     * @param packageName The base package
     * @return The classes
     * @throws ClassNotFoundException
     * @throws java.io.IOException
     */
    @SuppressWarnings("unchecked")
    public static Set<Class<?>> getClasses(String packageName)
            throws ClassNotFoundException, IOException {
        //获取类加载器
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        //是否循环
        boolean recursive = true;
        //第一个class类的集合
        Set<Class<?>> classes = new LinkedHashSet<Class<?>>();
        //获取包的名字，并进行替换
        String packagePathName = packageName.replace(DOT, SLASH);
        Enumeration<URL> resources = classLoader.getResources(packagePathName);
        //循环迭代resources
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            String protocol = resource.getProtocol();

            if ("file".equals(protocol)) {
                log.debug("file类型的扫描");
                //获取包的物理路径
                String filePath = URLDecoder.decode(resource.getFile(), "UTF-8");
                log.debug("The file path is: " + filePath);
                // 以文件的方式扫描整个包下的类并添加到集合中
                findAndAddClassesInPackageByFile(packageName, filePath, recursive, classes);
            } else if ("jar".equals(protocol)) {
                log.debug("jar类型的扫描");
                log.debug("The jar path is: " + resource.getFile());
                //获取jar文件
                JarFile jar = ((JarURLConnection) resource.openConnection()).getJarFile();
                // 以jar的方式扫描整个包下的类并添加到集合中
                findAndAddClassesInPackageByJar(jar, packagePathName, recursive, classes);
            }
        }
        return classes;
    }

    /**
     * 以jar的形式来获取包下的所有Class
     */
    public static void findAndAddClassesInPackageByJar(JarFile jar, String packagePathName, Boolean recursive, Set<Class<?>> classes) throws ClassNotFoundException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Enumeration<JarEntry> jarEntries = jar.entries();
        //循环迭代jarEntries
        while (jarEntries.hasMoreElements()) {
            // 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
            JarEntry jarEntry = jarEntries.nextElement();
            String name = jarEntry.getName();
            log.debug("The files in jar is: " + name);
            if (name.charAt(0) == SLASH) {
                name = name.substring(1);
            } else {
                //什么都不做
            }
            if (name.startsWith(packagePathName)) {//前半部分和定义的包名相同
                int idx = name.lastIndexOf(SLASH);
                if (idx != -1 && recursive) {
                    String jarPackageName = name.substring(0, idx).replace(SLASH, DOT);
                    if (isClass(name) && !jarEntry.isDirectory()) {
                        String className = name.substring(jarPackageName.length() + 1, name.length() - CLASS_EXT.length());
                        classes.add(classLoader.loadClass(jarPackageName + DOT + className));
                    } else {
                        //什么都不做
                    }
                } else if (idx != -1 && !recursive) {
                    if (idx == (packagePathName + SLASH).lastIndexOf(SLASH)) {//class就在当前包下
                        String jarPackageName = name.substring(0, idx).replace(SLASH, DOT);
                        if (isClass(name) && !jarEntry.isDirectory()) {
                            String className = name.substring(jarPackageName.length() + 1, name.length() - CLASS_EXT.length());
                            classes.add(classLoader.loadClass(jarPackageName + DOT + className));
                        } else {
                            //什么都不做
                        }
                    } else {
                        //什么都不做
                    }
                } else {
                    //什么都不做
                }

            } else {
                //什么都不做
            }
        }
    }

    /**
     * 以文件的形式来获取包下的所有Class
     *
     * @param packageName
     * @param packagePath
     * @param recursive
     * @param classes
     */
    public static void findAndAddClassesInPackageByFile(String packageName, String packagePath, final boolean recursive, Set<Class<?>> classes) throws ClassNotFoundException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        // 获取此包的目录 建立一个File
        File dir = new File(packagePath);
        // 如果不存在或者 也不是目录就直接返回
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        // 如果存在 就获取包下的所有文件 包括目录
        File[] dirfiles = dir.listFiles(new FileFilter() {
            // 自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)
            public boolean accept(File file) {
                return (recursive && file.isDirectory()) || (file.getName().endsWith(CLASS_EXT));
            }
        });
        // 循环所有文件
        for (File file : dirfiles) {
            // 如果是目录 则继续扫描
            if (file.isDirectory()) {
                findAndAddClassesInPackageByFile(packageName + DOT + file.getName(), file.getAbsolutePath(), recursive, classes);
            } else if (isClass(file.getName())) {
                // 如果是java类文件 去掉后面的.class 只留下类名
                String className = file.getName().substring(0, file.getName().length() - CLASS_EXT.length());
                //这里用forName有一些不好，会触发static方法，没有使用classLoader的load干净
                classes.add(classLoader.loadClass(packageName + DOT + className));
            }
        }
    }

    /**
     * 判断文件是否为一个class
     *
     * @param fileName
     * @return
     */
    private static boolean isClass(String fileName) {
        return fileName.endsWith(CLASS_EXT) && !fileName.contains(SPECIAL_NOT_CLASS_EXT);
    }
}
