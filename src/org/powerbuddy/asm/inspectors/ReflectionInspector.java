package org.powerbuddy.asm.inspectors;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

/**
 * Created with IntelliJ IDEA.
 * User: Jasper
 * Date: 8-5-13
 * Time: 18:25
 * To change this template use File | Settings | File Templates.
 */
public class ReflectionInspector implements Inspectable {

    @Override
    public boolean inspect(ClassNode node) {
        for(MethodNode mn : node.methods) {
            for(AbstractInsnNode ain : mn.instructions.toArray()) {
                if(ain instanceof MethodInsnNode) {
                    if(((MethodInsnNode) ain).desc.contains("java/lang/Reflect")) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
