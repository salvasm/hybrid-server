package es.uvigo.esei.dai.hybridserver.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.mysql.jdbc.Connection;

public class ServiceThread implements Runnable {
	private final Socket socket;
	private DAO MemoryDAO;
	
	public ServiceThread(Socket socket, Properties pag) {
		this.socket = socket;
		Connection con = null;
		try {
			con = (Connection) DriverManager.getConnection(pag.getProperty("db.url"),
					pag.getProperty("db.user"), pag.getProperty("db.password"));
			this.MemoryDAO = new HTMLDaoDB(con);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		//Map<String, String> pages = new HashMap<String, String>();
		this.MemoryDAO = new HTMLDaoDB(con);
		//this.MemoryDAO = new HTMLDao(pages);
	}

	public ServiceThread(Socket socket, Map<String, String> pages) {
		this.socket = socket;
		this.MemoryDAO = new HTMLDao(pages);
	}

	@Override
	public void run() {

		try (Socket socket = this.socket) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			try {
				HTTPRequest request = new HTTPRequest(reader); // Recibo Request del HybridServer y almaceno.
				Controller control = new Controller(MemoryDAO); // Creo una instancia que le paso un MemoryDAO (objeto que puede acceder a la base de datos) 
				HTTPResponse response = control.getDatos(request);
				response.print(new PrintWriter(socket.getOutputStream()));
				

			} catch (HTTPParseException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
}
