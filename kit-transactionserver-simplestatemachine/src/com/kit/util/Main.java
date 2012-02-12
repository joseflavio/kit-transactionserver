package com.kit.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;


public class Main {

	public static void main(final String[] args) {
	    final List<String> argsList = Arrays.asList(args);

		Main main = new Main(argsList);
		try {
			main.run();
		} catch (Exception e) {
			System.out.flush();
			System.err.println(e);
			//if (e.getCause() != null) {
			// System.err.println(e.getCause());
			//}
		}
	}

	private final List<String> args;
	private final Properties properties = new Properties();

	public Main(final List<String> args) {
		this.args = args;
	}

	public void run() throws Exception {
		if (args.size() != 1) {
			throw new Exception("Deve exatamente um parametro: caminho para o arquivo jad");
		}

		InputStream is = null;
		try {
			String path = args.get(0);
			is = new FileInputStream(path);
			properties.load(is);
		} catch (IOException e) {
		  e.printStackTrace();
			throw new Exception("Erro ao abrir arquivo jad");
		}
		finally {
		    if( is != null ) {
		        is.close();
		    }
		}

		boolean temAtualizador = false;
		System.out.println("Vers�o: " + getStringProperty("MIDlet-Version"));
		System.out.println("Jar: " + getStringProperty("MIDlet-Jar-URL"));
		System.out.println("Nome: " + getStringProperty("MIDlet-Name"));
		System.out.println("Navegador: " + getStringProperty("MIDlet-1"));

		if (properties.containsKey("MIDlet-2")) {
			System.out.println("Atualizador: " + getStringProperty("MIDlet-2"));
			temAtualizador = true;
		} else {
			System.out.println("Atualizador: n�o h�!");
		}
		System.out.println("Empresa: " + getStringProperty("MIDlet-Vendor"));

		String profile = getStringProperty("MicroEdition-Profile");
		String configuration = getStringProperty("MicroEdition-Configuration");
		if (! profile.equalsIgnoreCase("MIDP-2.0")) {
			throw new Exception("MicroEdition-Profile inv�lido, deveria ser MIDP-2.0");
		}
		if (! configuration.equalsIgnoreCase("CLDC-1.1")) {
			throw new Exception("MicroEdition-Configuration inv�lido, deveria ser CLDC-1.1");
		}

		String networkServer = getStringProperty("Network-Server");
		String networkPort = getStringPropertyLongVerified("Network-Port");
		String networkPath = getStringProperty("Network-Path");
		String networkHash = getStringPropertyUnverified("Network-Hash");
		final String properNetworkHash = SHA1.encode(networkServer+":"+networkPort+networkPath+"RRDJ");
		boolean temNetworkHash = false;
		if ((networkHash != null) && ! properNetworkHash.equalsIgnoreCase(networkHash)) {
			throw new Exception("Network-Hash inv�lido, deveria ser: "+properNetworkHash);
		} else if (networkHash != null){
			temNetworkHash = true;
		} else {
			networkHash = properNetworkHash;
		}

		getListProperty("Widget-CategoryList-Style", new String[] { "option", "link", "button", "text-edit", "text-nonedit" });
		getListProperty("Widget-CategoryList-Command", new String[] { "both", "additional", "default" });
		getBooleanProperty("Widget-DateField-Debug");

		boolean controlScheduleUpdate = getBooleanProperty("Control-Schedule-Update");
		if (controlScheduleUpdate) {
			if (! temAtualizador) {
				throw new Exception("Se Control-Schedule-Update � True, ent�o deveria existir MIDlet-2");
			}
		} else {
			if (temAtualizador) {
				System.err.println("Se Control-Schedule-Update � False, ent�o n�o deveria existir MIDlet-2");
			}
		}
		getListProperty("Screen-Login-Style", new String[] { "button", "link", "command" });
		getListProperty("Screen-Main-Style", new String[] { "button", "link", "command" });
		getListProperty("Screen-Main-List-Style", new String[] { "option", "link", "button", "text-edit", "text-nonedit" });
		getListProperty("Screen-Main-List-Command", new String[] { "both", "additional", "default" });


		String statusMaxUpdateInterval = getStringPropertyLongVerified("Status-MaxUpdateInterval");
		String statusMinUpdateInterval = getStringPropertyLongVerified("Status-MinUpdateInterval");
		String statusMaxOfflineInterval = getStringPropertyLongVerified("Status-MaxOfflineInterval");
		String statusMinAutoStartInterval = getStringPropertyLongVerified("Status-MinAutoStartInterval");
		String statusMinIdleInterval = getStringPropertyLongVerified("Status-MinIdleInterval");
		String statusTimerInterval = getStringPropertyLongVerified("Status-TimerInterval");
		String statusPath = getStringProperty("Status-Path");
		String statusHash = getStringProperty("Status-Hash");

		String properStatusHash = SHA1.encode(statusMaxUpdateInterval+
				statusMinUpdateInterval+
				statusMaxOfflineInterval+
				statusMinAutoStartInterval+
				statusMinIdleInterval+
				statusTimerInterval+
				statusPath+"RRDJ");

		boolean temStatusHash = false;
		if ((statusHash != null) && ! properStatusHash.equalsIgnoreCase(statusHash)) {
			throw new Exception("Status-Hash inv�lido, deveria ser: "+properStatusHash);
		} else if (statusHash != null) {
			temStatusHash = true;
		} else {
			statusHash = properStatusHash;
		}

		if (! temStatusHash || ! temNetworkHash) {
			System.err.println("Adicione estas linhas!");
			if (!temNetworkHash) {
        System.out.println("Network-Hash: "+networkHash);
      }
			if (!temStatusHash) {
        System.out.println("Status-Hash: "+statusHash);
      }
		} else {
			System.out.println();
			System.out.println("Nenhum problema encontrado.");
		}
		//System.exit(0);
	}

	private String getStringPropertyUnverified(final String key) throws Exception {
		if (! properties.containsKey(key)) {
			return null;
		}
		return properties.getProperty(key);
	}

	private String getListProperty(final String key, final String list[]) throws Exception {
		String valor = getStringProperty(key);
		String l = null;
		for (int i = 0; i < list.length; i++) {
			String string = list[i];
			if (string.equalsIgnoreCase(valor)) {
				return valor;
			}
			if (l == null) {
				l = string;
			} else {
				l += ", " + string;
			}
		}
		throw new Exception("Propridade n�o est� em "  + l +": " + key);
	}

	private boolean getBooleanProperty(final String key) throws Exception {
		String valor = getStringProperty(key);
		if ( ! (valor.equalsIgnoreCase("true") || valor.equalsIgnoreCase("false"))) {
			throw new Exception("Propridade n�o � True/False: " + key);
		}
		return Boolean.getBoolean(valor);

	}

	private String getStringProperty(final String key) throws Exception {
		if (! properties.containsKey(key)) {
			throw new Exception("Propridade est� faltando: " + key);
		}
		return properties.getProperty(key);
	}

	private String getStringPropertyLongVerified(final String key) throws Exception {
		String valor = getStringProperty(key);
		try {
			Long.parseLong(valor);
		} catch (NumberFormatException e) {
			throw new Exception("Propridade n�o � um n�mero v�lido: " + key);
		}
		return valor;
	}

}
