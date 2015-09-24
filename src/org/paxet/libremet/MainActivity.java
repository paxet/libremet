package org.paxet.libremet;


import org.paxet.libremet.db.Poblacion;
import org.paxet.libremet.db.PoblacionDB;
import org.paxet.libremet.db.PoblacionesProvider;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class MainActivity extends Activity implements LoaderCallbacks<Cursor> {
	
	private SimpleCursorAdapter mAdapter;
    private ListView mListView;
    private ActionMode.Callback mCallback;
    private ActionMode mMode;
    
    private Poblacion pobl_sel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mListView = (ListView) findViewById(R.id.listview);
		mCallback = new Contextual();
		
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		    public void onItemClick(AdapterView<?> parent, View v, int position, long id){
		    	pobl_sel = extraePoblDeView(v, id);
		    	abrirPrediccion(id);
		    }
		});
		
		mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
		    public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id){
		    	pobl_sel = extraePoblDeView(v, id);
		    	
		    	if(mMode!=null) {
                    return false;
		    	} else {
                    mMode = startActionMode(mCallback);
		    	}
		    	
		    	parent.requestFocusFromTouch();
		        parent.setSelection(position);
		        
                return true;
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
        
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean consumido = false;
		
		switch (item.getItemId()) {
		case R.id.action_new:
			abrirBuscar();
			consumido = true;
			break;
		case R.id.action_settings:
			lanzarPreferencias(null);
			consumido = true;
			break;
		case R.id.action_about:
			lanzarAcercaDe(null);
			consumido = true;
			break;
		}

		return consumido;
	}
	
	public void lanzarPreferencias(View view){
		Intent i = new Intent(this, SettingsActivity.class);
		startActivity(i);
	}

	public void lanzarAcercaDe(View view){
		Intent i = new Intent(this, AcercaDeActivity.class);
		startActivity(i);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
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
	
	private void abrirBuscar() {
		Intent i = new Intent(MainActivity.this, SearchActivity.class);
		startActivityForResult(i, 1);
	}
	
	private void abrirPrediccion(long id) {
		Intent i = new Intent(MainActivity.this, TabsActivity.class);
		i.putExtra("poblacion", id);
		i.putExtra("nombre", pobl_sel.getNombre());
		i.putExtra("pais", pobl_sel.getPais());
		startActivity(i);
	}
	
	@Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
    	if (requestCode == 1 && resultCode == RESULT_OK) {
    		refrescaCursor();
    	}
    }
	
	private Poblacion extraePoblDeView(View v, long id) {
		String nombre;
		String pais;
		RelativeLayout rLay = (RelativeLayout)v;
    	nombre = ((TextView)rLay.findViewById(R.id.nombre)).getText().toString();
    	pais = ((TextView)rLay.findViewById(R.id.pais)).getText().toString();
    	Poblacion pobl = new Poblacion();
    	pobl.setID(id);
    	pobl.setNombre(nombre);
    	pobl.setPais(pais);
    	
    	return pobl;
	}
	
	private Loader<Cursor> getCursorLoader() {
		Uri uri = PoblacionesProvider.CONTENT_URI;
	    return new CursorLoader(this, uri, null, null, null, null);
	}
	
	private void refrescaCursor() {
		mAdapter.changeCursor(((CursorLoader)getCursorLoader()).loadInBackground());
	}
	
	private void eliminaBasico() {
		PoblacionDB dbhelper = new PoblacionDB(getApplicationContext());
		dbhelper.deletePoblacion(pobl_sel.getID());
    	dbhelper.close();
    	refrescaCursor();
	}
	
	//Este código sólo funciona de versión de API 16 en adelante :(
	/*private void eliminaAvanzado(MenuItem item) {
		final View view = findViewById(item.getItemId());
		final PoblacionDB dbhelper = new PoblacionDB(getApplicationContext());
    	view.animate().setDuration(2000).alpha(0)
        .withEndAction(new Runnable() {
          @Override
          public void run() {
            dbhelper.deletePoblacion(item_sel_id);
            dbhelper.close();
            refrescaCursor();
            view.setAlpha(1);
          }
        });
	}*/
	
	public void showConfirmarEliminacion(final MenuItem item) {
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        switch (which){
		        case DialogInterface.BUTTON_POSITIVE:
		        	//Este código sólo compila con versión 16 en adelante del API
		        	//Lo dejo comentado porque prefiero mantener la 13 como minima
	            	/*if (Build.VERSION.SDK_INT >= 16) {
	            		eliminaAvanzado(item);
	            	} else {
	            		eliminaBasico();
	            	}*/
	            	eliminaBasico();
		            break;

		        case DialogInterface.BUTTON_NEGATIVE:
		        	dialog.dismiss();
		            break;
		        }
		    }
		};

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(getString(R.string.dialog_confirmacion_titulo))
			.setPositiveButton(getString(R.string.si), dialogClickListener)
		    .setNegativeButton(getString(R.string.no), dialogClickListener).show();
	}
	
	class Contextual implements ActionMode.Callback {
		 
		@Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.setTitle(getString(R.string.contextual_titulo));
            getMenuInflater().inflate(R.menu.context_menu, menu);
            return true;
        }

        /** Cuando se le da un toque a alguna de las opciones mostradas */
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch(item.getItemId()){
            case R.id.ver:
                abrirPrediccion(pobl_sel.getID());
                break;
            case R.id.eliminar:
            	showConfirmarEliminacion(item);
                break;
            }
            //Quitamos el modo de contextual
            mode.finish();
            return false;
        }
        
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mMode = null;
        }
	}
}
