package org.paxet.libremet;

import org.paxet.libremet.db.Poblacion;
import org.paxet.libremet.db.PoblacionDB;
import org.paxet.libremet.tasks.ConsultaAsincronaBuscar;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SearchActivity extends Activity {
	
	private AsyncTask<Object, ?, ?> currentFilterTask = null;
	private ListView lvPoblaciones;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		
		lvPoblaciones = (ListView)findViewById(R.id.lvPoblaciones);
		lvPoblaciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		    public void onItemClick(AdapterView<?> parent, View v, int position, long id){
		    	String nombre, pais;
		    	RelativeLayout rLay = (RelativeLayout)v;
		    	nombre = ((TextView)rLay.findViewById(R.id.nombre)).getText().toString();
		    	pais = ((TextView)rLay.findViewById(R.id.pais)).getText().toString();
		    	anyadirPoblacion(id, nombre, pais);
		    }
		});
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	//Callback para el boton de buscar
	public void buscaPoblacion(View v) {
	  if (currentFilterTask != null && !currentFilterTask.getStatus().equals(AsyncTask.Status.FINISHED)) {
	    currentFilterTask.cancel(true);
	  }
	  currentFilterTask = new ConsultaAsincronaBuscar();
	  EditText et = (EditText) findViewById(R.id.filtro);
	  currentFilterTask.execute(this, lvPoblaciones, et.getText().toString()); 
	}
	
	//Para anyadir a la base de datos la poblacion seleccionada
	protected void anyadirPoblacion(long id, String nombre, String pais) {
		Poblacion pobl = new Poblacion();
		pobl.setID(id);
		pobl.setNombre(nombre);
		pobl.setPais(pais);
		
		PoblacionDB dbhelper = new PoblacionDB(getApplicationContext());
		dbhelper.insertPoblacion(pobl);
    	dbhelper.close();
    	
    	Intent intent = new Intent();
        intent.putExtra("resultado", true);
        setResult(RESULT_OK, intent);
        finish();
	}

}
