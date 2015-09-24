package org.paxet.libremet.consultas.openweathermap;

import java.net.MalformedURLException;
import java.net.URL;

import org.paxet.libremet.consultas.PeticionesBuscar;
import org.paxet.libremet.exceptions.MethodNotImplemented;

import android.util.Log;

public class PeticionesBuscarOWM implements PeticionesBuscar {
	
	private static String SEARCH_URL = "http://api.openweathermap.org/data/2.5/find?q=";
	

	@Override
	public String getPoblacionesJSON(String filtro) throws MethodNotImplemented {
		String jsonData = null;
		
		URL urlServicio;
		
		try {
			urlServicio = new URL(SEARCH_URL + filtro);
			jsonData = PeticionesOWM.getJSONData(urlServicio);
		} catch (MalformedURLException malEx) {
			Log.d("PeticionesBuscarOWM", malEx.getLocalizedMessage());
		}
		return jsonData;
	}

	@Override
	public String getPoblacionesXML(String filtro) throws MethodNotImplemented {
		throw new MethodNotImplemented("No implementado, utiliza el de JSON.");
	}

}
