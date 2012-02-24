package com.kit.lightserver.gui.resources;

import java.io.File;
import java.net.URL;

import javax.swing.ImageIcon;

import com.jfap.framework.types.FilepathVO;
import com.kit.lightserver.KITLightServerBootstrap;

public final class ImageIconLoader {

    private ImageIconLoader() {}

    static public ImageIcon load(final FilepathVO file) {
        if( file.getType() == FilepathVO.FilepathType.CLASS_PATH ) {
            URL imageIconJarUrl = KITLightServerBootstrap.class.getResource(file.getFilepath());
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