package org.paxet.libremet.widgets;

import org.paxet.libremet.R;
import org.paxet.libremet.tasks.ConsultaAsincronaWidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;
import android.widget.Toast;

public class WidgetMeteorologia extends AppWidgetProvider {
	
	private static final long DIEZ_MINUTOS = 600000L;
	public static final long CIUDAD_EJEMPLO = 1283240L;

	@Override 
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		//Obtenemos la lista de controles del widget actual 
		RemoteViews controles = new RemoteViews(context.getPackageName(), R.layout.widget);
		
		//Iteramos la lista de widgets en ejecucion 
		for (int i = 0; i < appWidgetIds.length; i++) { 
			//ID del widget actual 
			int widgetId = appWidgetIds[i];   
			
			//Asociamos los 'eventos' al widget 
			Intent intent = new Intent("org.paxet.libremet.widgets.ACTUALIZAR_WIDGET_METEOROLOGIA");
			intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId); 
			PendingIntent pendingIntent = PendingIntent.getBroadcast(context, widgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT);   
			controles.setOnClickPendingIntent(R.id.relativeLayoutWidget, pendingIntent);
			
			//Actualizamos el widget actual 
			actualizarWidget(context, appWidgetManager, widgetId);
		}
	}
	
	@Override 
	public void onReceive(Context context, Intent intent) {   
		if (intent.getAction().equals("org.paxet.libremet.widgets.ACTUALIZAR_WIDGET_METEOROLOGIA")) { 
			//Obtenemos el ID del widget a actualizar 
			int widgetId = intent.getIntExtra( AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);   
			//Obtenemos el widget manager de nuestro contexto 
			AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);   
			//Actualizamos el widget 
			if (widgetId != AppWidgetManager.INVALID_APPWIDGET_ID) { 
				actualizarWidget(context, widgetManager, widgetId);
			} 
		} 
		super.onReceive(context, intent);  
	}
	
	@Override 
	public void onDeleted(Context context, int[] appWidgetIds) {
		SharedPreferences prefs = context.getSharedPreferences("WidgetPreferencias", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		
		for (int i = 0; i < appWidgetIds.length; i++) { 
			//ID del widget actual 
			int widgetId = appWidgetIds[i];   
			
			//Quitamos su configuracion de las preferencias
			editor.remove("poblacion_" + widgetId);
			editor.remove("lastupdatemilis_" + widgetId);
		}
		
		editor.commit();
	}
	
	public static void actualizarWidget(Context context, AppWidgetManager appWidgetManager, int widgetId) { 
		//Recuperamos la poblacion para el widget actual 
		SharedPreferences prefs = context.getSharedPreferences("WidgetPreferencias", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		
		long poblacion = prefs.getLong("poblacion_" + widgetId, CIUDAD_EJEMPLO);
		long poblacionLastUpdated = prefs.getLong("poblacionlastupdated_" + widgetId, -1L);
		long lastUpdateMilis = prefs.getLong("lastupdatemilis_" + widgetId, 1L);
		long horaActualMilis = System.currentTimeMillis();
		
		//El widget se actualiza automaticamente cada hora, pero se puede hacer tambien manualmente
		//Se comprueba que al menos hayan pasado diez minutos desde la ultima sincronizacion
		//para no sobrecargar el servidor.
		if (isPoblacionDistinta(poblacion, poblacionLastUpdated) || is10MinSinceLastUpdate(lastUpdateMilis, horaActualMilis)) {
			Toast.makeText(context, context.getString(R.string.descargando_prediccion), Toast.LENGTH_LONG).show();
			recuperaMeteo(new RemoteViews(context.getPackageName(), R.layout.widget), appWidgetManager, widgetId, poblacion);
			editor.putLong("lastupdatemilis_" + widgetId, horaActualMilis);
			editor.putLong("poblacionlastupdated_" + widgetId, poblacion);
			editor.commit();
		}
	}

	private static boolean is10MinSinceLastUpdate(long lastUpdateMilis,
			long horaActualMilis) {
		return horaActualMilis - lastUpdateMilis > DIEZ_MINUTOS;
	}

	private static boolean isPoblacionDistinta(long poblacion,
			long poblacionLastUpdated) {
		return poblacion != poblacionLastUpdated;
	}

	private static void recuperaMeteo(RemoteViews controles,
			AppWidgetManager appWidgetManager, int widgetId, long poblacion) {
		

		//Actualizamos los datos en el control del widget 
		ConsultaAsincronaWidget tarea = new ConsultaAsincronaWidget();
	    tarea.execute(controles, appWidgetManager, widgetId, poblacion);
	}
}