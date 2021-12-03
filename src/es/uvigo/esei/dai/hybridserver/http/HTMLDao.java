package es.uvigo.esei.dai.hybridserver.http;

import java.util.Map;

public class HTMLDao implements DAO {
	private Map<String, String> pages;
	
	public HTMLDao(Map<String,String> pages) {
		this.pages = pages;
		
	}
	
	public boolean existeUUID(String uuid){
		return pages.containsKey(uuid);
	}
	
	// Hacer constructor para manejar Mapa de datos que da el 
	@Override
	public String getPage(String UUID) throws Exception {
		return pages.get(UUID);
	}

	@Override
	public String ExisteContenido(String contenido) throws Exception {
		String content = null;
		for (Map.Entry<String, String> d : pages.entrySet()) {
			if (d.getValue().equals(contenido))
				content = d.getValue();
		}
		return content;
	}

	@Override
	public boolean containsUUID(String UUID) throws Exception {
		return pages.containsKey(UUID);
	}

	@Override
	public void deletePage(String UUID) throws Exception {
		pages.remove(UUID);	
	}

	@Override
	public String toStringUUID() throws Exception {
		String toRet = "<html><head><meta charset ='UTF-8'></head><body><h1>Hybrid Server</h1><p>Páginas web:</p><ul>";
		for (Map.Entry<String, String> dato : this.pages.entrySet()) {
			toRet += "<li><a href=\"html?uuid=" + dato.getKey() + "\">" +  dato.getKey() +"</a></li>";
		}
		toRet += "</ul></body></html>";
		
		System.out.println(toRet);
		return toRet;
	}

	@Override
	public void postPage(HTML content) throws Exception {
		pages.put(content.getUUID(), content.getContent());
	}
	
	public String toString(){
		String tr = ""; 
		for(Map.Entry<String, String> v : pages.entrySet()){
			tr += v.getKey() +"\n";
		}
		return tr;
	}
}
