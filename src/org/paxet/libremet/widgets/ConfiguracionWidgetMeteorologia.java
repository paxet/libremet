package org.paxet.libremet.widgets;

import org.paxet.libremet.R;
import org.paxet.libremet.db.PoblacionDB;
import org.paxet.libremet.db.PoblacionesProvider;

import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class ConfiguracionWidgetMeteorologia extends Activity implements LoaderCallbacks<Cursor> {
	
	private int idWidget = 0; 
	private SimpleCursorAdapter mAdapter;
    private ListView mListView;
	
	/** Se invoca la primera vez que se crea la activity. */ 
	@Override 
	public void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState); 
		setContentView(R.layout.widgetconfiguration);
		
		mListView = (ListView) findViewById(R.id.listview);
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		    public void onItemClick(AdapterView<?> parent, View v, int position, long id){
		    	establecePoblacion(id);
		    }
		});
		
		mAdapter = new SimpleCursorAdapter(getBaseContext(),
                R.layout.listitem_poblacion,
                null,
                new String[] { PoblacionDB.KEY_ROW_ID, PoblacionDB.KEY_NOMBRE, PoblacionDB.KEY_PAIS},
                new int[] { R.id.id , R.id.nombre, R.id.pais }, 0);
        mListView.setAdapter(mAdapter);
        
        /** Con esta llamada se inicializa el sistema invocando onCreateLoader */
        getLoaderManager().initLoader(0, null, this);

		/* Ejecutamos este cdigo para recuperar el objeto Intent lanzado y *
		 * recuperamos tambien sus parametros para trabajar con ellos posteriormente */ 
		Intent intentLanzado = getIntent(); 
		Bundle parametros = intentLanzado.getExtras();   

		/* En la variable private que declaramos arriba vamos a guardar el ID del * 
		 * Intent que acabamos de recuperar */ 
		idWidget = parametros.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);   
		
		/* Codificamos la accion que se disparara cuando el usuario cancele la operacion * 
		 * de forma explicita o por medio del boton "Atras" del dispositivo movil */ 
		setResult(RESULT_CANCELED);
	}
	 
	 public void establecePoblacion(long id){
		 //Guardamos el mensaje personalizado en las preferencias 
		 SharedPreferences prefs = getSharedPreferences("WidgetPreferencias", Context.MODE_PRIVATE); 
		 SharedPreferences.Editor editor = prefs.edit();
		 
		 editor.putLong("poblacion_" + idWidget, id);
		 editor.commit();
		 
		 //Actualizamos el widget tras la configuracion
		 AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(ConfiguracionWidgetMeteorologia.this);   
		 WidgetMeteorologia.actualizarWidget(ConfiguracionWidgetMeteorologia.this, appWidgetManager, idWidget); 
		 
		 //Devolvemos como resultado: ACEPTAR (RESULT_OK) 
		 Intent resultado = new Intent(); 
		 resultado.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, idWidget); 
		 setResult(RESULT_OK, resultado); 
		 finish(); 
	 }

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		return getCursorLoader();
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
		mAdapter.swapCursor(arg1);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		mAdapter.swapCursor(null);
	}
	
	private Loader<Cursor> getCursorLoader() {
		Uri uri = PoblacionesProvider.CONTENT_URI;
	    return new CursorLoader(this, uri, null, null, null, null);
	}

}