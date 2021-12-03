package es.uvigo.esei.dai.hybridserver.http;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class HTMLDaoDB implements DAO {
	private Connection connection;
	
	public HTMLDaoDB(Connection con) {
		this.connection = con;
	}

	@Override
	public String getPage(String UUID) throws Exception {
		String s = null;

		String sql = "SELECT content FROM HTML  WHERE uuid=?";
		
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setString(1, UUID);
			ResultSet rs = statement.executeQuery();

			// Si es verdadero es que devolvió una fila
			if (rs.next()) {
				s = rs.getString(1);
			}
		}

		return s;
	}

	@Override
	public String ExisteContenido(String contenido) throws Exception {
		String s = null;

		String sql = "SELECT uuid FROM HTML  WHERE content=?";

		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setString(1, contenido);
			ResultSet rs = statement.executeQuery();
			// Si es verdadero es que devolvió una fila
			if (rs.next()) {
				s = rs.getString(1);
			}
		}

		return s;
	}

	@Override
	public boolean containsUUID(String UUID) throws Exception {
		String s = null;

		String sql = "SELECT * FROM HTML  WHERE uuid=?";

		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setString(1, UUID);
			ResultSet rs = statement.executeQuery();

			// Si es verdadero es que devolvió una fila
			if (rs.next()) {
				s = rs.getString(1);
			}
		}

		return (s != null);
	}


	@Override
	public void deletePage(String UUID) throws Exception {
		String sql = "DELETE FROM HTML WHERE uuid=?";

		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setString(1, UUID);
			int result = statement.executeUpdate();

			if (result == 1) {
				System.out.println("Borrado");
			} else
				System.out.println("No borrado");
		}

	}
	
	@Override
	public String toStringUUID() throws Exception {
		String toRet = "<html><h1>Hybrid Server</h1><p>All the pages:</p><ul>";

		String sql = "SELECT uuid FROM HTML ";

		try (PreparedStatement statement = connection.prepareStatement(sql)) {

			ResultSet rs = statement.executeQuery();

			while (rs.next()) {
				toRet += "<li><a href=html?uuid=" + rs.getString(1) + ">"
						+ rs.getString(1) + "</a></li>";
			}

		}

		toRet += "</ul></body></html>";
		return toRet;

	}


	@Override
	public void postPage(HTML content) throws Exception {
		String sql = "INSERT INTO HTML (uuid, content) VALUES(?,?)";

		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setString(1, content.getUUID());
			statement.setString(2, content.getContent());
			statement.executeUpdate();

		}
	}

}
