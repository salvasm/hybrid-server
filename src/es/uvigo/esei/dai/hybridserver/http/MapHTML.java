package es.uvigo.esei.dai.hybridserver.http;


import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;



public class MapHTML {
	private Map<String,String> bd;
	
	
	public String getUUIDrandom() {
		UUID uuid = UUID.randomUUID();
		String uuidst = uuid.toString();
		return uuidst;

	}
	
	public  MapHTML() {
		bd = new LinkedHashMap<String,String>() ;
		
		
		bd.put(this.getUUIDrandom(), "Pagina HTML con uuid: ");
		bd.put(this.getUUIDrandom(), "Otra pagina HTML uuid");
		
	}

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}

}
