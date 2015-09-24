package org.paxet.libremet.tasks;

import org.json.JSONException;
import org.paxet.libremet.R;
import org.paxet.libremet.consultas.PeticionesServicio;
import org.paxet.libremet.consultas.openweathermap.ParserOWMJSON;
import org.paxet.libremet.consultas.openweathermap.PeticionesOWM;
import org.paxet.libremet.exceptions.MethodNotImplemented;
import org.paxet.libremet.meteo.Prediccion;

import android.appwidget.AppWidgetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.RemoteViews;

public class ConsultaAsincronaWidget extends AsyncTask<Object, Void, Prediccion> {
	//TODO Seria interesante separar en esta clase el codigo segun el servidor a utilizar,
	//si es OpenWeatherMap o AEMET. De momento esta OpenWeatherMap a pinyon porque no
	//hay nada para AEMET
	
	private RemoteViews controles= null;
	private AppWidgetManager appWidgetManager = null;
	private int widgetId = -1;

	@Override
	protected Prediccion doInBackground(Object... params) {
		//params[0] = RemoteViews
		//params[1] = AppWidgetManager
		//params[2] = widgetId
		//params[3] = localidad
		
		Prediccion pred = new Prediccion();
		controles = (RemoteViews) params[0];
		appWidgetManager = (AppWidgetManager) params[1];
		widgetId = (Integer) params[2];
		long localidad = (Long)params[3];
		
		try {
			PeticionesServicio consulta = (new PeticionesOWM());
			pred = ParserOWMJSON.getPrediccionActual((consulta.getPrevisionJSON(localidad, PeticionesServicio.PREDICCION_ACTUAL)));


		} catch (JSONException e) {
			e.printStackTrace();
		} catch (MethodNotImplemented e) {
			// TODO Aqui se debera llamar alternativamente a getPrevisionXML
			//De momento no hay nada implpmentado ya que se pospone para la version AEMET
			Log.d("ConsultaAsincronaWidget", e.getLocalizedMessage());
		}
		try {
			Bitmap img = null;
			int intentos = 3;
			byte[] bytesIcono;
			
			while (img == null || intentos > 0) {
				bytesIcono = (new PeticionesOWM()).getImagen(pred.getCondicionesActuales().getIcono());
				if (bytesIcono != null && bytesIcono.length > 0) {
					img = BitmapFactory.decodeByteArray(bytesIcono, 0, bytesIcono.length);
				}
				intentos--;
			}
			
			if (img != null) {
				pred.setIcono(img);
			}
		} catch (MethodNotImplemented e) {
			// TODO Aqui debera poner un icono por defecto si no esta implementado el metodo
			Log.d("ConsultaAsincronaWidget", e.getLocalizedMessage());
		}
		return pred;

	}

	@Override
	protected void onPostExecute(Prediccion weather) {
		super.onPostExecute(weather);

		String mensaje;
		controles.setImageViewBitmap(R.id.condIcon, weather.getIcono());
		
		mensaje = weather.getLocalizacion().getCiudad() + "," + weather.getLocalizacion().getPais();
		controles.setTextViewText(R.id.cityText, mensaje);
		
		mensaje = weather.getCondicionesActuales().getCondicion()
				+ "(" + weather.getCondicionesActuales().getDesc() + ")";
		controles.setTextViewText(R.id.condDescr, mensaje);
		
		mensaje = Math.round((weather.getTemperatura().getTemp())) + "°C";
		controles.setTextViewText(R.id.temp, mensaje);
		
		mensaje = Math.round((weather.getTemperatura().getMinTemp())) + "°C";
		controles.setTextViewText(R.id.tempMin, mensaje);
		
		mensaje = Math.round((weather.getTemperatura().getMaxTemp())) + "°C";
		controles.setTextViewText(R.id.tempMax, mensaje);
		
		mensaje = "" + weather.getCondicionesActuales().getHumedad() + "%";
		controles.setTextViewText(R.id.hum, mensaje);
		
		mensaje = weather.getCondicionesActuales().getPresion() + " hPa";
		controles.setTextViewText(R.id.press, mensaje);
		
		mensaje = weather.getViento().getVelocidad() + " m/s";
		controles.setTextViewText(R.id.windSpeed, mensaje);
		
		mensaje = weather.getViento().getDirGrados() + "°";
		controles.setTextViewText(R.id.windDeg, mensaje);
		
		//Notificamos al manager de la actualizacion del widget actual 
		appWidgetManager.updateAppWidget(widgetId, controles);

	}

}
