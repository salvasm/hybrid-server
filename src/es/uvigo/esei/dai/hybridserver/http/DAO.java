package es.uvigo.esei.dai.hybridserver.http;

public interface DAO {
	public abstract String getPage(String UUID) throws Exception;

	public abstract String ExisteContenido(String contenido) throws Exception;

	public abstract boolean containsUUID(String UUID) throws Exception;

	public abstract void postPage(HTML content) throws Exception;

	public abstract void deletePage(String UUID) throws Exception;

	public abstract String toStringUUID() throws Exception;
}
