package es.uvigo.esei.dai.hybridserver.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Map;
import java.net.URLDecoder;
import java.util.LinkedHashMap;

public class HTTPRequest {
	private HTTPRequestMethod metodo;
	private String version;
	private String resourceName;
	private String resourceChain;
	private String[] resourcePath;
	private Map<String, String> headerParameters;
	private Map<String, String> resourceParameters;
	private String content;
	private int contentLength;

	public HTTPRequest(Reader reader) throws IOException, HTTPParseException {

		resourceParameters = new LinkedHashMap<String, String>();
		headerParameters = new LinkedHashMap<String, String>();
		contentLength = 0;

		BufferedReader br = new BufferedReader(reader);
		try {
			String linea;

			linea = br.readLine();
			String[] space = linea.split("\\s"); // Inicializo y divido la
													// petición por espacios.
			if (space.length < 3)
				throw new HTTPParseException();

			if (space[0].equals("GET")) {
				metodo = HTTPRequestMethod.GET;
			} else if (space[0].equals("POST")) {
				metodo = HTTPRequestMethod.POST;
			} else if (space[0].equals("PUT")) {
				metodo = HTTPRequestMethod.PUT;
			} else if (space[0].equals("DELETE")) {
				metodo = HTTPRequestMethod.DELETE;
			} else if (space[0].equals("TRACE")) {
				metodo = HTTPRequestMethod.TRACE;
			} else if (space[0].equals("HEAD")) {
				metodo = HTTPRequestMethod.HEAD;
			} else if (space[0].equals("CONNECT")) {
				metodo = HTTPRequestMethod.CONNECT;
			} else if (space[0].equals("OPTIONS")) {
				metodo = HTTPRequestMethod.OPTIONS;
			}

			version = space[2];

			if (space[1].contains("?")) {
				String[] name = space[1].split("\\?"); // dividimos por
														// interrogante
				resourceChain = space[1]; // /hello/hola.txt?province=ourense
				resourceName = name[0].substring(1); // hello/hola.txt

				resourcePath = resourceName.split("/");
				String[] aux;
				String[] ampers;
				if (name[1].contains("&")) {
					ampers = name[1].split("&");

					for (int i = 0; i < ampers.length; i++) {
						aux = ampers[i].split("[=]+");
						resourceParameters.put(aux[0], aux[1]);
					}
				} else {
					aux = name[1].split("[=]+");
					resourceParameters.put(aux[0], aux[1]);

				}

			} else {

				resourceChain = space[1];
				String name;
				if (space[1].equals("/")) {

					name = space[1];
					resourceName = name.substring(1);
					resourcePath = name.split("/");
				} else {
					name = space[1].substring(1);
					resourceName = name;
					resourcePath = name.split("/");
				}

			}
			int cont = 0;
			while (!(linea = br.readLine()).isEmpty()) {
				space = linea.split(": ");

				if (space.length < 2 && cont == 0) {
					cont++;
					throw new HTTPParseException();
				}

				if (space[0].contains("Content-Length")) {
					contentLength = Integer.parseInt(space[1]);
					headerParameters.put(space[0], space[1]);

				} else {
					cont++;

					headerParameters.put(space[0], space[1]);
				}

			}

			if (this.contentLength != 0) {
				char[] buff = new char[contentLength];
				br.read(buff);
				String c = new String(buff);
				content = c;

				String type = headerParameters.get("Content-Type");

				if (type != null && type.startsWith("application/x-www-form-urlencoded")) {
					content = URLDecoder.decode(content, "UTF-8");
				}

				String aux3 = content.replaceAll("&", ", ");

				String[] auxCont = aux3.split(", ");
				for (int i = 0; i < auxCont.length; i++) {
					String[] auxBucl = auxCont[i].split("[=]+");
					resourceParameters.put(auxBucl[0], auxBucl[1]);
				}
			}

		} catch (Exception e) {
			throw new HTTPParseException();
		}

	}

	public HTTPRequestMethod getMethod() {
		return metodo;
	}

	public String getResourceChain() {
		return resourceChain;
	}

	public String[] getResourcePath() {
		return resourcePath;
	}

	public String getResourceName() {
		return resourceName;
	}

	public Map<String, String> getResourceParameters() {
		return resourceParameters;
	}

	public String getHttpVersion() {
		return version;
	}

	public Map<String, String> getHeaderParameters() {
		return headerParameters;
	}

	public String getContent() {
		return content;
	}

	public int getContentLength() {
		return contentLength;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(this.getMethod().name()).append(' ').append(this.getResourceChain())
				.append(' ').append(this.getHttpVersion()).append("\r\n");

		for (Map.Entry<String, String> param : this.getHeaderParameters().entrySet()) {
			sb.append(param.getKey()).append(": ").append(param.getValue()).append("\r\n");
		}

		if (this.getContentLength() > 0) {
			sb.append("\r\n").append(this.getContent());
		}

		return sb.toString();
	}

	public static void main(String[] args) throws IOException {
	}
}
