package es.uvigo.esei.dai.hybridserver.http;

import java.util.HashMap;
import java.util.Map;

public class Controller {
	private DAO MemoryDAO;
	private HTTPResponse response;

	public Controller(DAO MemoryDAO) {
		this.MemoryDAO = MemoryDAO;
		this.response = new HTTPResponse();
	}

	public HTTPResponse getDatos(HTTPRequest request) throws Exception {
		Map<String, String> map = new HashMap<>();

		try {

			response.setVersion(request.getHttpVersion()); // Devuelve Versión

			switch (request.getMethod()) {

			// GET
			case GET:

				map = request.getResourceParameters();
				String key = map.get("uuid"); // Se ve la UUID
				String content = MemoryDAO.getPage(key); // Devuelve Contenido

				if (request.getResourceName().contains("html") || request.getResourceName().length() == 0) { // Con_HTML_en_ResourceName____O____Sin_nada
					if (key != null) {
						if (content != null) {
							response.setStatus(HTTPResponseStatus.S200);
							response.setContent(content);
						} else {
							response.setStatus(HTTPResponseStatus.S404);
							response.setContent(
									"<html><h1>Hybrid Server</h1><h2>Error 404 - Page not found</h2></html>");
						}
					} else {
						if (request.getResourceName().contains("uuid")) {
							response.setStatus(HTTPResponseStatus.S404);
							response.setContent(
									"<html><h1>Hybrid Server</h1><h2>Error 404 - Page not found</h2></html>");
						} else {
							response.setStatus(HTTPResponseStatus.S200);
							response.setContent(MemoryDAO.toStringUUID());
						}
					}
				} else {
					response.setStatus(HTTPResponseStatus.S400);
					response.setContent("<html><h1>Hybrid Server</h1><h2>Error 400 - Bad request</h2></html>");
				}
				break;

			// DELETE
			case DELETE:
				if (request.getResourceParameters().containsKey("uuid")) {

					map = request.getResourceParameters();
					key = map.get("uuid"); // Almacena UUID

					if (MemoryDAO.containsUUID(key)) {
						MemoryDAO.deletePage(key);
						String Message;

						if (!MemoryDAO.containsUUID(key)) {
							response.setStatus(HTTPResponseStatus.S200);
							Message = "<html><h1>Hybrid Server</h1><h2>Page deleted</h2></html>";
						} else {
							response.setStatus(HTTPResponseStatus.S404);
							Message = "<html><h1>Hybrid Server</h1><h2>Error 404 - Page not found. Page have not been deleted.</h2></html>";
						}
						response.setContent(Message);

					} else {
						response.setStatus(HTTPResponseStatus.S404);
						response.setContent(
								"<html><h1>Hybrid Server</h1><h2>Error 404 - Page not found. Page have not been deleted.</h2></html>");
					}

				} else {
					response.setStatus(HTTPResponseStatus.S400);
					response.setContent("<html><h1>Hybrid Server</h1><h2>Error 400 - Bad request</h2></html>");
				}

				break;

			// POST
			case POST:
				HTML html = new HTML(request.getContent());
				
				if (!request.getResourceParameters().containsKey("html")) {
					response.setStatus(HTTPResponseStatus.S400);
					response.setContent("<html><h1>Hybrid Server</h1><h2>Error 400 - Bad request</h2></html>");

				} else if (html.getContent() == null) {
					response.setStatus(HTTPResponseStatus.S404);
					response.setContent("<html><h1>Hybrid Server</h1><h2>Error 404 - Not found</h2></html>");
				} else {
					if (MemoryDAO.containsUUID(html.getUUID())) {

						boolean unica = false;
						while (!unica) {

							html.setUUID();
							if (!MemoryDAO.containsUUID(html.getUUID())) {
								unica = true;
							}
						}

					}
					html.setContent(html.getContent().substring(5, html.getContent().length()));
					MemoryDAO.postPage(html);
					response.setStatus(HTTPResponseStatus.S200);
					response.setContent("<a href=\"html?uuid=" + html.getUUID() + "\">" + html.getUUID() + "</a>");
				}

				break;

			default:
				response.setContent("<html><h1>Hybrid Server</h1><h1>500 Error - Internal server error</h1></html>");
				response.setStatus(HTTPResponseStatus.S500);
				response.setVersion(request.getHttpVersion());
				break;
			}
		} catch (Exception e) {
			response.setContent("<html><h1> Hybrid Server </h1><h1>500 Error</h1></html>");
			response.setStatus(HTTPResponseStatus.S500);
			response.setVersion(request.getHttpVersion());
			// e.printStackTrace();
			return response;
		}

		return response;
	}
}
