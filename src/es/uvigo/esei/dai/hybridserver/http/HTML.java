package es.uvigo.esei.dai.hybridserver.http;

import java.util.UUID;

public class HTML {
	String uuid;
	String content;

	public HTML(String content) {
		UUID uuid = UUID.randomUUID();
		String uuidst = uuid.toString();
		this.uuid = uuidst;
		this.content = content;
	}

	public HTML(String uuid, String content) {
		this.uuid = uuid;
		this.content = content;
	}

	public String getContent() {
		return this.content;
	}

	public String getUUID() {

		return this.uuid;
	}

	public void setUUID() {
		UUID aux = UUID.randomUUID();
		String uuidst = aux.toString();
		this.uuid = uuidst;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
