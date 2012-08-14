package com.kit.lightserver.gui.filebrowserlauncher;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


final class WindowsExplorerLauncher implements FileBrowserLauncher {

	static private final Logger LOGGER = LoggerFactory.getLogger(WindowsExplorerLauncher.class);

	private final ProcessBuilder processBuilder;

	WindowsExplorerLauncher(final String dir) {
		this.processBuilder =  new ProcessBuilder("explorer.exe", dir);
	}

	@Override
	public LaunchResult launchFileBrowser() {
      try {
    	  processBuilder.start();
    	  return LaunchResult.SUCCESS;
      }
      catch (IOException e) {
          LOGGER.error("Unexpected error launching: <explorer.exe>.", e);
      }
      return LaunchResult.getError("Erro inesperado ao iniciar 'nautilus' no Ubuntu.");
	}

}// class
