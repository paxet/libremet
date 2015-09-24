package org.paxet.libremet.consultas.openweathermap;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.paxet.libremet.db.Poblacion;

public class ParserBuscarOWMJSON {

	public static ArrayList<Poblacion> getPoblaciones(String jsonData) throws JSONException {
		ArrayList<Poblacion> pobls = new ArrayList<Poblacion>();
		Poblacion pobl;
		JSONObject jObjCompleto = new JSONObject(jsonData);
		JSONArray jsonArray = jObjCompleto.getJSONArray("list");
		JSONObject jObj, sysObj;
		
		for (int i = 0; i < jsonArray.length(); i++) {
			pobl = new Poblacion();
			jObj = jsonArray.getJSONObject(i);
			pobl.setID(jObj.getLong("id"));
			pobl.setNombre(jObj.getString("name"));
			sysObj = jObj.getJSONObject("sys");
			pobl.setPais(sysObj.getString("country"));
			
			pobls.add(pobl);
		}
		
		return pobls;
	}

}
