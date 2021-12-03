package es.uvigo.esei.dai.hybridserver;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
//import java.util.HashMap;
//import java.util.Map;
import java.util.Properties;

public class Launcher {

	public static void main(String[] args) throws IOException {
		Properties props = new Properties();
		InputStream inputStream = new FileInputStream("config.conf");
		props.load(inputStream);
		inputStream.close();

//		Map<String, String> pages = new HashMap<String, String>();
//
//		pages.put("6df1047e-cf19-4a83-8cf3-38f5e53f7725", "This is the page 6df1047e-cf19-4a83-8cf3-38f5e53f7725.");
//
//		HybridServer server = new HybridServer(pages); // Arrancar para SEGUNDA
														// Semana
		HybridServer server = new HybridServer(props); // Arrancar para TERCERA
														// Semana

		server.start();
		System.out.println("Server Connected...");

	}
}
