package com.kit.lightserver.gui.filebrowserlauncher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class FileBrowserLauncherFactory {

	static private final Logger LOGGER = LoggerFactory.getLogger(FileBrowserLauncherFactory.class);

	static public FileBrowserLauncher build(final String currentFullDir) {


		String osName = System.getProperty("os.name");
		String osVersion = System.getProperty("os.version");
		String osArch = System.getProperty("os.arch");

		String osIdentifier = osName + "/" + osVersion + "/" + osArch;
		LOGGER.info("os={}", osIdentifier);

		boolean isWindows7 = ( osName != null && osName.toUpperCase().indexOf("WINDOWS 7") != -1 ); // Windows 7/6.1/amd64

		boolean isLinux = ( osName != null && osName.toUpperCase().indexOf("LINUX") != -1 );

		final FileBrowserLauncher launcher;
		if( isWindows7 ) {
		    launcher = new WindowsExplorerLauncher(currentFullDir);
		}
		else if( isLinux ) {
			launcher = new LinuxNautilusLauncher(currentFullDir);
		}
		else {
			launcher = new NoValidLauncher(osIdentifier, currentFullDir);
		}

		return launcher;

	}

}// class
