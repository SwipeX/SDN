package org.powerbuddy.asm.inspectors;

import org.objectweb.asm.tree.*;

/**
 * Created with IntelliJ IDEA.
 * User: Jasper
 * Date: 8-5-13
 * Time: 18:15
 * To change this template use File | Settings | File Templates.
 */
public class ClassLoaderInspector implements Inspectable {

    @Override
    public boolean inspect(ClassNode node) {
        if(node.superName.contains("ClassLoader")) {
            return true;
        }

        for(FieldNode fn : node.fields) {
            if(fn.desc.contains("ClassLoader")) {
                return true;
            }
        }

        for(MethodNode mn : node.methods) {
            for(AbstractInsnNode ain : mn.instructions.toArray()) {
                if(ain instanceof MethodInsnNode) {
                    if(((MethodInsnNode) ain).desc.contains("ClassLoader")) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
