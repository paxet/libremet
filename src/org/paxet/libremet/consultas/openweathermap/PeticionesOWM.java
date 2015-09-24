package org.paxet.libremet.consultas.openweathermap;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

import org.paxet.libremet.consultas.PeticionesServicio;
import org.paxet.libremet.exceptions.MethodNotImplemented;

import android.util.Log;

public class PeticionesOWM implements PeticionesServicio {
	
	private static final String WEATHER_CURRENT_URL = "http://api.openweathermap.org/data/2.5/weather?id=";
	private static final String WEATHER_5DAYS_URL = "http://api.openweathermap.org/data/2.5/forecast?id=";
	private static final String WEATHER_14DAYS_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?cnt=14&id=";
	private static final String PARAMS_URL = "&units=metric&lang=" + 
			//Existe un "bug" en la API, he abierto incidencia
			//http://bugs.openweathermap.org/issues/121
			//Realizo esta corrección para que muestre bien en espanyol
			(Locale.getDefault().getLanguage().equals("es")?"sp" : Locale.getDefault().getLanguage());
	private static final String IMG_URL = "http://openweathermap.org/img/w/";
	public static final String APPID = "02aaaa33fcb5f76b78184d92c38ca5a2";
	
	public PeticionesOWM() {
		super();
	}

	@Override
	public String getPrevisionJSON(long idpoblacion, int tipoPrediccion)
			throws MethodNotImplemented {
		String jsonData = null;
		
		URL urlServicio;
		
		try {
			switch (tipoPrediccion) {
			case PREDICCION_ACTUAL:
				urlServicio = new URL(WEATHER_CURRENT_URL + idpoblacion + PARAMS_URL);
				break;
			case PREDICCION_CINCO_DIAS:
				urlServicio = new URL(WEATHER_5DAYS_URL + idpoblacion + PARAMS_URL);
				break;
			case PREDICCION_CATORCE_DIAS:
				urlServicio = new URL(WEATHER_14DAYS_URL + idpoblacion + PARAMS_URL);
				break;
			default:
				urlServicio = new URL(WEATHER_CURRENT_URL + idpoblacion + PARAMS_URL);
			};
			
			jsonData = PeticionesOWM.getJSONData(urlServicio);
		} catch (MalformedURLException malEx) {
			Log.d("PeticionesOWM", malEx.getLocalizedMessage());
		}
		return jsonData;
	}
	
	public static String getJSONData(URL urlServicio) {
		HttpURLConnection httpConn = null ;
		InputStream flujoConn = null;
		
		StringBuilder previsionJSON = null;

		try {
			
			httpConn = (HttpURLConnection) (urlServicio).openConnection();
			httpConn.setRequestMethod("GET");
			httpConn.addRequestProperty("x-api-key", PeticionesOWM.APPID);
			httpConn.setDoInput(true);
			httpConn.setDoOutput(true);
			httpConn.connect();
			
			previsionJSON = new StringBuilder();
			flujoConn = httpConn.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(flujoConn));
			String linea;
			while (  (linea = reader.readLine()) != null ) {
				previsionJSON.append(linea + "\r\n");
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (flujoConn != null) {
					flujoConn.close();
				}
			} catch(Exception e) {
				Log.d("PeticionesOWM", e.getLocalizedMessage());
			}
			try {
				if (httpConn != null) {
					httpConn.disconnect();
				}
			} catch(Exception e) {
				Log.d("PeticionesOWM", e.getLocalizedMessage());
			}
		}
		return previsionJSON==null?null:previsionJSON.toString();
	}

	@Override
	public String getPrevisionXML(long idpoblacion, int tipoPrediccion)
			throws MethodNotImplemented {
		throw new MethodNotImplemented("No implementado, utiliza el de JSON.");
	}

	@Override
	public byte[] getImagen(String codigo) throws MethodNotImplemented {
		HttpURLConnection httpConn = null ;
		InputStream flujoConn = null;
		
		ByteArrayOutputStream bytea = null;
		
		try {
			URL urlServicio = new URL(IMG_URL + codigo);
			httpConn = (HttpURLConnection) (urlServicio).openConnection();
			httpConn.setRequestMethod("GET");
			httpConn.addRequestProperty("x-api-key", APPID);
			httpConn.setDoInput(true);
			httpConn.setDoOutput(true);
			httpConn.connect();
			
			flujoConn = httpConn.getInputStream();
			byte[] buffer = new byte[1024];
			bytea = new ByteArrayOutputStream();
			
			while ( flujoConn.read(buffer) != -1) {
				bytea.write(buffer);
			}
		}
		catch(Exception e) {
			Log.d("libremet", this.getClass().getName() + "\t" + e.getLocalizedMessage());
			/*System.err.println(e.getLocalizedMessage());
			e.printStackTrace(System.err);*/
		}
		finally {
			try {
				if (flujoConn != null) {
					flujoConn.close();
				}
			} catch(Exception e) {
				Log.d("libremet", this.getClass().getName() + "\t" + e.getLocalizedMessage());
			}
			try {
				if (httpConn != null) {
					httpConn.disconnect();
				}
			} catch(Exception e) {
				Log.d("libremet", this.getClass().getName() + "\t" + e.getLocalizedMessage());
			}
        }
		return bytea==null?null:bytea.toByteArray();
	}

}
