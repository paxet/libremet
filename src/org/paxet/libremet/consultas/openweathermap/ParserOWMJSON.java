package org.paxet.libremet.consultas.openweathermap;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.paxet.libremet.meteo.Localizacion;
import org.paxet.libremet.meteo.Prediccion;

public class ParserOWMJSON {
	
	public static Prediccion getPrediccionActual(String data) throws JSONException {
		Prediccion pred = new Prediccion();
		JSONObject jObj = new JSONObject(data);
		
		Localizacion loc = new Localizacion();
		
		loc.setCiudad(jObj.getString("name"));
		
		JSONObject coordObj = jObj.getJSONObject("coord");
		loc.setLatitud(coordObj.getDouble("lat"));
		loc.setLongitud(coordObj.getDouble("lon"));
		
		JSONObject sysObj = jObj.getJSONObject("sys");
		loc.setPais(sysObj.getString("country"));
		loc.setAmanecer(sysObj.getInt("sunrise"));
		loc.setOcaso(sysObj.getInt("sunset"));
		
		pred.setLocalizacion(loc);
		
		JSONArray jsona = jObj.getJSONArray("weather");
		 
		JSONObject JSONWeather = jsona.getJSONObject(0);
		pred.getCondicionesActuales().setWeatherId(JSONWeather.getInt("id"));
		pred.getCondicionesActuales().setDesc(JSONWeather.getString("description"));
		pred.getCondicionesActuales().setCondicion(JSONWeather.getString("main"));
		pred.getCondicionesActuales().setIcono(JSONWeather.getString("icon"));
		 
		JSONObject mainObj = jObj.getJSONObject("main");
		pred.getCondicionesActuales().setHumedad(mainObj.getInt("humidity"));
		pred.getCondicionesActuales().setPresion(mainObj.getInt("pressure"));
		pred.getTemperatura().setMaxTemp(mainObj.getDouble("temp_max"));
		pred.getTemperatura().setMinTemp(mainObj.getDouble("temp_min"));
		pred.getTemperatura().setTemp(mainObj.getDouble("temp"));
		 
		// Wind
		JSONObject wObj = jObj.getJSONObject("wind");
		pred.getViento().setVelocidad(wObj.getDouble("speed"));
		pred.getViento().setDirGrados(wObj.getDouble("deg"));
		 
		// Clouds
		JSONObject cObj = jObj.getJSONObject("clouds");
		pred.getNubes().setPerc(cObj.getInt("all"));
		
		//DT
		pred.setHora(jObj.getLong("dt"));
		
		return pred;
		
	}

	public static ArrayList<Prediccion> getPredicciones(String data) throws JSONException {
		ArrayList<Prediccion> preds;
		
		JSONObject jObj = new JSONObject(data);

		if (jObj.getInt("cnt") == 14) {
			preds = interpretaJSONDias(jObj);
		} else {
			preds = interpretaJSONHoras(jObj);
		}
		
		return preds;
	}

	private static ArrayList<Prediccion> interpretaJSONDias(
			JSONObject Obj) throws JSONException {
		ArrayList<Prediccion> preds = new ArrayList<Prediccion>();
		
		JSONObject cityObj = Obj.getJSONObject("city");
		JSONArray jsonArray = Obj.getJSONArray("list");
		
		Prediccion pred;
		Localizacion loc;
		JSONObject jObj;
		
		for (int i = 0; i < jsonArray.length(); i++) {
			jObj = jsonArray.getJSONObject(i);
			pred = new Prediccion();
			loc = new Localizacion();
			
			pred.setHora(jObj.getLong("dt"));
			
			JSONObject coordObj = cityObj.getJSONObject("coord");
			loc.setCiudad(cityObj.getString("name"));
			loc.setPais(cityObj.getString("country"));
			loc.setLatitud(coordObj.getDouble("lat"));
			loc.setLongitud(coordObj.getDouble("lon"));
			
			JSONArray jsona = jObj.getJSONArray("weather");
			JSONObject JSONWeather = jsona.getJSONObject(0);
			pred.getCondicionesActuales().setWeatherId(JSONWeather.getInt("id"));
			pred.getCondicionesActuales().setDesc(JSONWeather.getString("description"));
			pred.getCondicionesActuales().setCondicion(JSONWeather.getString("main"));
			pred.getCondicionesActuales().setIcono(JSONWeather.getString("icon"));
			
			JSONObject tempObj = jObj.getJSONObject("temp");
			pred.getTemperatura().setMaxTemp(tempObj.getDouble("max"));
			pred.getTemperatura().setMinTemp(tempObj.getDouble("min"));
			pred.getTemperatura().setTemp(tempObj.getDouble("day"));
			loc.setAmanecer(tempObj.getInt("morn"));
			loc.setOcaso(tempObj.getInt("eve"));
			
			pred.getCondicionesActuales().setHumedad(jObj.getInt("humidity"));
			pred.getCondicionesActuales().setPresion(jObj.getInt("pressure"));
			
			pred.getViento().setVelocidad(jObj.getDouble("speed"));
			pred.getViento().setDirGrados(jObj.getDouble("deg"));
			pred.getNubes().setPerc(jObj.getInt("clouds"));
			
			pred.setLocalizacion(loc);
			
			preds.add(pred);
		}
		
		return preds;
	}
	
	private static ArrayList<Prediccion> interpretaJSONHoras(
			JSONObject Obj) throws JSONException {
		ArrayList<Prediccion> preds = new ArrayList<Prediccion>();
		
		JSONObject cityObj = Obj.getJSONObject("city");
		JSONArray jsonArray = Obj.getJSONArray("list");
		
		Prediccion pred;
		Localizacion loc;
		JSONObject jObj;
		
		for (int i = 0; i < jsonArray.length(); i++) {
			jObj = jsonArray.getJSONObject(i);
			pred = new Prediccion();
			loc = new Localizacion();
			
			pred.setHora(jObj.getLong("dt"));
			
			JSONObject coordObj = cityObj.getJSONObject("coord");
			loc.setCiudad(cityObj.getString("name"));
			loc.setPais(cityObj.getString("country"));
			loc.setLatitud(coordObj.getDouble("lat"));
			loc.setLongitud(coordObj.getDouble("lon"));
			
			JSONArray jsona = jObj.getJSONArray("weather");
			JSONObject JSONWeather = jsona.getJSONObject(0);
			pred.getCondicionesActuales().setWeatherId(JSONWeather.getInt("id"));
			pred.getCondicionesActuales().setDesc(JSONWeather.getString("description"));
			pred.getCondicionesActuales().setCondicion(JSONWeather.getString("main"));
			pred.getCondicionesActuales().setIcono(JSONWeather.getString("icon"));
			 
			JSONObject mainObj = jObj.getJSONObject("main");
			pred.getCondicionesActuales().setHumedad(mainObj.getInt("humidity"));
			pred.getCondicionesActuales().setPresion(mainObj.getInt("pressure"));
			pred.getTemperatura().setMaxTemp(mainObj.getDouble("temp_max"));
			pred.getTemperatura().setMinTemp(mainObj.getDouble("temp_min"));
			pred.getTemperatura().setTemp(mainObj.getDouble("temp"));
			 
			// Wind
			JSONObject wObj = jObj.getJSONObject("wind");
			pred.getViento().setVelocidad(wObj.getDouble("speed"));
			pred.getViento().setDirGrados(wObj.getDouble("deg"));
			 
			// Clouds
			JSONObject cObj = jObj.getJSONObject("clouds");
			pred.getNubes().setPerc(cObj.getInt("all"));
			
			pred.setLocalizacion(loc);
			
			preds.add(pred);
		}
		
		return preds;
	}

}
