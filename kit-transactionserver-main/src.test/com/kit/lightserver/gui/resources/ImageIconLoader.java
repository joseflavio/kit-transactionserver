package com.kit.lightserver.gui.resources;

import java.io.File;
import java.net.URL;

import javax.swing.ImageIcon;

import org.dajo.framework.filepath.DajoFilepath;

import com.kit.lightserver.KitTransactionServerGui;

public final class ImageIconLoader {

    private ImageIconLoader() {}

    static public ImageIcon load(final DajoFilepath file) {
        if( file.getType() == DajoFilepath.Type.CLASS_PATH ) {
            URL imageIconJarUrl = KitTransactionServerGui.class.getResource(file.getFilepath());
            if( imageIconJarUrl == null ) {
                throw new RuntimeException("File not found. file="+file);
            }
            return new ImageIcon(imageIconJarUrl);
        }
        else {
            File testTest = new File(file.getFilepath());
            if( testTest.exists() == false ) {
                throw new RuntimeException("File not found. file="+file);
            }
            return new ImageIcon(file.getFilepath());
        }
    }

}// class
