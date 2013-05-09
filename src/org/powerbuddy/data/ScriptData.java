package org.powerbuddy.data;

import org.powerbuddy.script.Manifest;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: Tim
 * Date: 5/8/13
 * Time: 12:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class ScriptData {
    Manifest manifest;
    File jar;
    int id = -1;

    public ScriptData(Manifest manifest, File jar) {
        this.manifest = manifest;
        this.jar = jar;
    }
}
