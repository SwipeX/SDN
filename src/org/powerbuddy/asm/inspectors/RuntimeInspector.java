package org.powerbuddy.asm.inspectors;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

/**
 * Created with IntelliJ IDEA.
 * User: Jasper
 * Date: 8-5-13
 * Time: 18:09
 * To change this template use File | Settings | File Templates.
 */
public class RuntimeInspector implements Inspectable {

    @Override
    public boolean inspect(ClassNode node) {
        for(MethodNode mn : node.methods) {
            for(AbstractInsnNode insn : mn.instructions.toArray()) {
                if(insn instanceof MethodInsnNode) {
                    MethodInsnNode method = (MethodInsnNode) insn;
                    if(method.desc.contains("java/lang/Runtime")) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
