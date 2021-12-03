package es.uvigo.esei.dai.hybridserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import es.uvigo.esei.dai.hybridserver.http.ServiceThread;

public class HybridServer {
	private static int SERVICE_PORT = 8888;
	private Thread serverThread;
	private boolean stop;
	private int numClientes = 50;
	private Map<String, String> pages;
	private Properties prop;
//	DATOS DE LA BASE DE DATOS
//	db.url=jdbc:mysql://localhost:3306/hstestdb
//	db.user=hsdb
//	db.password=hsdbpass
//	Connection connection = new DriverManager.getConnection("jdbc:mysql://localhost:3306/hstestdb", "hsdb", "hsdbpass");
	
	public HybridServer() {
		
		this(Collections.<String, String> emptyMap()); //Primera Semana
	}
	
	public HybridServer(Map<String, String> pages) { //Segunda Semana
		this.pages = pages; 
		this.prop = null;
	}

	public HybridServer(Properties properties) { //Tercera Semana
		this.numClientes = Integer.parseInt(properties.getProperty("numClients"));
		this.prop = properties;
		this.pages = null;
		
		
	}

	public int getPort() {
		return SERVICE_PORT;
	}

	public Map<String, String> getMap() {
		return this.pages;
	}

	public void start() {
		this.serverThread = new Thread() {
			@Override
			public void run() {
				try (final ServerSocket serverSocket = new ServerSocket(SERVICE_PORT)) { //Crear socket del servidor
					ExecutorService threadPool = Executors.newFixedThreadPool(numClientes); //Crear un pool de hilos
					while (true) {
							Socket socket = serverSocket.accept();
							if (stop) break; // Si stop para hilo.
							if (prop != null) { // Si no para crea el servicio
								threadPool.execute(new ServiceThread(socket, prop)); //Primera forma de crear un servicio (externa: pasando socket y propiedades)
							} else {
								threadPool.execute(new ServiceThread(socket, pages)); //Segunda forma de crear un servicio (memoria: pasando socket y mapa de paginas)
							}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};

		this.stop = false;
		this.serverThread.start();
	}

	public void stop() {
		this.stop = true;
		
		try (Socket socket = new Socket("localhost", SERVICE_PORT)) {
			// Esta conexión se hace, simplemente, para "despertar" el hilo servidor
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		try {
			this.serverThread.join();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		
		this.serverThread = null;
	}

}
