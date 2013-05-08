package org.powerbuddy.asm.inspectors;

import org.objectweb.asm.tree.ClassNode;

/**
 * Created with IntelliJ IDEA.
 * User: Jasper
 * Date: 8-5-13
 * Time: 18:07
 * To change this template use File | Settings | File Templates.
 */
public interface Inspectable {

    public boolean inspect(ClassNode node);

}
