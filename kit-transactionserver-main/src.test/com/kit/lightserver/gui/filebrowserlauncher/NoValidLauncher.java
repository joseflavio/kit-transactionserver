package com.kit.lightserver.gui.filebrowserlauncher;


final class NoValidLauncher implements FileBrowserLauncher {

	private final String osIdentifier;
	private final String dir;

	public NoValidLauncher(String osIdentifier, String dir) {
		this.osIdentifier = osIdentifier;
		this.dir = dir;
	}

	@Override
	public LaunchResult launchFileBrowser() {		
		return LaunchResult.getError("Sistema desconhecido ("+osIdentifier+"). Logs disponiveis no diretorio: " + dir);
	}

}// class
