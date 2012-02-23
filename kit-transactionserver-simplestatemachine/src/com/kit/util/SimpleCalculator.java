package com.kit.util;



public class SimpleCalculator {

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		String networkServer = "192.168.10.3";
		String networkPort = "3301"; // deve ser um int/long
		String networkPath = "nokia/mira"; // Network-Path: nokia/mira
		String baseNetworkHashString = networkServer+":"+networkPort+networkPath+"RRDJ";
		String networkHash = SHA1.encode(baseNetworkHashString);

		System.out.println("baseNetworkHashString="+baseNetworkHashString);
		System.out.println("Network-Hash: "+networkHash);

	}

}
