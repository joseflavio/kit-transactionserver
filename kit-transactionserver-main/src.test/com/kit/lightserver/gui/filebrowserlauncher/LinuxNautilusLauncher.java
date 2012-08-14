package com.kit.lightserver.gui.filebrowserlauncher;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


final class LinuxNautilusLauncher implements FileBrowserLauncher {

	static private final Logger LOGGER = LoggerFactory.getLogger(LinuxNautilusLauncher.class);
	
	private final ProcessBuilder processBuilder;
	
	LinuxNautilusLauncher(String dir) {
		this.processBuilder =  new ProcessBuilder("nautilus", dir);
	}

	@Override
	public LaunchResult launchFileBrowser() {
      try {            
    	  processBuilder.start();
    	  return LaunchResult.SUCCESS;    	  
      }
      catch (IOException e) {            
          LOGGER.error("Error launching <nautilus>", e);          
      }
      return LaunchResult.getError("Erro inesperado ao iniciar 'nautilus' no Ubuntu.");
	}
	
}// class
