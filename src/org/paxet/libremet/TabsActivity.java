package org.paxet.libremet;

import java.util.ArrayList;
import java.util.List;

import org.paxet.libremet.consultas.PeticionesServicio;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.os.Bundle;
import android.view.Menu;

public class TabsActivity extends Activity implements TabListener {
	
	public static final int POSICION_TAB_ACTUAL = 0;
	public static final int POSICION_TAB_5DIAS = 1;
	public static final int POSICION_TAB_14DIAS = 2;
	
	private List<Fragment> fragList = new ArrayList<Fragment>();
	private long poblacion = -1L;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		poblacion = getIntent().getExtras().getLong("poblacion");
		String nombre = getIntent().getExtras().getString("nombre");;
		String pais = getIntent().getExtras().getString("pais");;
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
	    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	    
	    creaTab(actionBar, getString(R.string.prediccion_actual), POSICION_TAB_ACTUAL);
	    creaTab(actionBar, getString(R.string.prediccion_3horas), POSICION_TAB_5DIAS);
	    creaTab(actionBar, getString(R.string.prediccion_14dias), POSICION_TAB_14DIAS);
	    
	    setTitle(getString(R.string.app_name) + " - " + nombre + ", " + pais);
	    
		if (savedInstanceState != null) {
			int savedIndex = savedInstanceState.getInt("SAVED_INDEX");
			getActionBar().setSelectedNavigationItem(savedIndex);
		}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("SAVED_INDEX", getActionBar()
				.getSelectedNavigationIndex());
	}

	private void creaTab(ActionBar actionBar, String titulo, int posicion) {
		Tab tab = actionBar.newTab();
        tab.setText(titulo);
        tab.setTabListener(this);
        actionBar.addTab(tab, posicion);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		Fragment tabFrag = null;
		int tabIndex = tab.getPosition();

		if (fragList.size() > tabIndex) {
			tabFrag = fragList.get(tabIndex);
		}
		
		if (tabFrag == null) {			
			Bundle data = new Bundle();
			switch (tabIndex) {
			case POSICION_TAB_ACTUAL:
				tabFrag = Fragment.instantiate(this, TabFragmentListView.class.getName());
				data.putInt("prediccion_dias",  PeticionesServicio.PREDICCION_ACTUAL);
				break;
			case POSICION_TAB_5DIAS:
				tabFrag = Fragment.instantiate(this, TabFragmentListView.class.getName());
				data.putInt("prediccion_dias",  PeticionesServicio.PREDICCION_CINCO_DIAS);
				break;
			case POSICION_TAB_14DIAS:
				tabFrag = Fragment.instantiate(this, TabFragmentListView.class.getName());
				data.putInt("prediccion_dias",  PeticionesServicio.PREDICCION_CATORCE_DIAS);
				break;
			default:
				tabFrag = Fragment.instantiate(this, TabFragmentListView.class.getName());
				break;
					
			}
			data.putLong("poblacion", poblacion);
            tabFrag.setArguments(data);
			
            ft.add(android.R.id.content, tabFrag, String.valueOf(tabIndex));
            fragList.add(tabFrag);
        } else {
            //Si ya existia lo volvemos a posicionar
            ft.attach(tabFrag);
        }

	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		if (fragList.size() > tab.getPosition()) {
			ft.detach(fragList.get(tab.getPosition()));
        }
	}
}
