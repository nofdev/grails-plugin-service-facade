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
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        boolean recursive = true;
        //The first class set
        Set<Class<?>> classes = new LinkedHashSet<Class<?>>();
        //Get the package name, and replace it
        String packagePathName = packageName.replace(DOT, SLASH);
        Enumeration<URL> resources = classLoader.getResources(packagePathName);
        //Loop resources
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            String protocol = resource.getProtocol();

            if ("file".equals(protocol)) {
                log.debug("scan type with file");
                //Get the physical path of the package
                String filePath = URLDecoder.decode(resource.getFile(), "UTF-8");
                log.debug("The file path is: " + filePath);

                findAndAddClassesInPackageByFile(packageName, filePath, recursive, classes);
            } else if ("jar".equals(protocol)) {
                log.debug("scan type with jar");
                log.debug("The jar path is: " + resource.getFile());
                JarFile jar = ((JarURLConnection) resource.openConnection()).getJarFile();

                findAndAddClassesInPackageByJar(jar, packagePathName, recursive, classes);
            }
        }
        return classes;
    }

    /**
     * Scan all classes of the whole package with the type of jar and add to set
     *
     * @param jar
     * @param packagePathName
     * @param recursive
     * @param classes
     * @throws ClassNotFoundException
     */
    public static void findAndAddClassesInPackageByJar(JarFile jar, String packagePathName, Boolean recursive, Set<Class<?>> classes) throws ClassNotFoundException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Enumeration<JarEntry> jarEntries = jar.entries();
        //Loop jarEntries
        while (jarEntries.hasMoreElements()) {
            //Get a entry in the jar, which may be a directory or other files, such as META-INF
            JarEntry jarEntry = jarEntries.nextElement();
            String name = jarEntry.getName();
            log.debug("The files in jar is: " + name);
            if (name.charAt(0) == SLASH) {
                name = name.substring(1);
            } else {
                //Do not need sub name
            }
            if (name.startsWith(packagePathName)) {
                int idx = name.lastIndexOf(SLASH);
                if (idx != -1 && recursive) {
                    String jarPackageName = name.substring(0, idx).replace(SLASH, DOT);
                    if (isClass(name) && !jarEntry.isDirectory()) {
                        String className = name.substring(jarPackageName.length() + 1, name.length() - CLASS_EXT.length());
                        classes.add(classLoader.loadClass(jarPackageName + DOT + className));
                    } else {
                        //ignore this type
                    }
                } else if (idx != -1 && !recursive) {
                    if (idx == (packagePathName + SLASH).lastIndexOf(SLASH)) {//class is just in the current package
                        String jarPackageName = name.substring(0, idx).replace(SLASH, DOT);
                        if (isClass(name) && !jarEntry.isDirectory()) {
                            String className = name.substring(jarPackageName.length() + 1, name.length() - CLASS_EXT.length());
                            classes.add(classLoader.loadClass(jarPackageName + DOT + className));
                        } else {
                            //ignore this type
                        }
                    } else {
                        //Do not need deal the deeper class, because all classes has a flatten performance
                    }
                } else {
                    //The file end with slash is not a class
                }

            } else {
                //The file end with slash is not a class
            }
        }
    }

    /**
     * Scan all classes of the whole package with the type of file and add to set
     *
     * @param packageName
     * @param packagePath
     * @param recursive
     * @param classes
     */
    public static void findAndAddClassesInPackageByFile(String packageName, String packagePath, final boolean recursive, Set<Class<?>> classes) throws ClassNotFoundException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        File dir = new File(packagePath);

        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        // Get all files of the package, including the directory
        File[] dirfiles = dir.listFiles(new FileFilter() {
            // Custom filter rule
            public boolean accept(File file) {
                return (recursive && file.isDirectory()) || (file.getName().endsWith(CLASS_EXT));
            }
        });
        for (File file : dirfiles) {
            if (file.isDirectory()) {
                findAndAddClassesInPackageByFile(packageName + DOT + file.getName(), file.getAbsolutePath(), recursive, classes);
            } else if (isClass(file.getName())) {
                // Get the class name without .class suffix
                String className = file.getName().substring(0, file.getName().length() - CLASS_EXT.length());
                //Do not use forName method, because of static methods will be triggered
                classes.add(classLoader.loadClass(packageName + DOT + className));
            }
        }
    }

    /**
     * Assert the file is a class
     *
     * @param fileName
     * @return
     */
    private static boolean isClass(String fileName) {
        return fileName.endsWith(CLASS_EXT) && !fileName.contains(SPECIAL_NOT_CLASS_EXT);
    }
}
