package org.powerbuddy.asm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.powerbuddy.asm.inspectors.ClassLoaderInspector;
import org.powerbuddy.asm.inspectors.Inspectable;
import org.powerbuddy.asm.inspectors.ReflectionInspector;
import org.powerbuddy.asm.inspectors.RuntimeInspector;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created with IntelliJ IDEA.
 * User: Jasper
 * Date: 8-5-13
 * Time: 16:21
 * To change this template use File | Settings | File Templates.
 */
public class Inspector {

    private final static ArrayList<Inspectable> inspectors = new ArrayList<>();

    public Inspector() {
        initiateInspectors();
    }

    public static String getScriptName(File jar) {
        ClassNode[] nodes = parse(jar);
        String auth = "";
        String name = "";
        for (ClassNode node : nodes) {
            if (node.superName.contains("Script")) {
                if(node == null || node.visibleAnnotations == null)
                    continue;
                for (AnnotationNode ann : node.visibleAnnotations) {
                    for (int i = 0; i < ann.values.size(); i++) {
                        if (ann.values.get(i).equals("author")) {
                            auth = ann.values.get(i + 1).toString();
                        }
                        if (ann.values.get(i).equals("name")) {
                            name = ann.values.get(i + 1).toString();
                        }
                    }
                }
            }
        }
        return auth + "." + name;
    }

    private static ClassNode[] parse(File file) {
        ArrayList<ClassNode> nodes = new ArrayList<ClassNode>();
        try {
            JarFile jar = new JarFile(file);
            Enumeration<?> en = jar.entries();
            while (en.hasMoreElements()) {
                JarEntry e = (JarEntry) en.nextElement();
                if (e.getName().endsWith(".class")) {
                    ClassReader reader = new ClassReader(jar.getInputStream(e));
                    ClassNode node = new ClassNode();
                    reader.accept(node, ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);

                    nodes.add(node);
                }
            }
            jar.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return nodes.toArray(new ClassNode[nodes.size()]);
    }

    public void initiateInspectors() {
        inspectors.add(new ClassLoaderInspector());
        inspectors.add(new RuntimeInspector());
        inspectors.add(new ReflectionInspector());
    }

    public boolean inspect(File jar) {
        ClassNode[] nodes = parse(jar);
        for (ClassNode node : nodes) {
            for (Inspectable ins : inspectors) {
                if (ins.inspect(node))
                    return true;
            }
        }
        return false;
    }
}
