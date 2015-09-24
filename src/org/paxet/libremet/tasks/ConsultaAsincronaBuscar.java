package org.paxet.libremet.tasks;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.paxet.libremet.R;
import org.paxet.libremet.consultas.PeticionesBuscar;
import org.paxet.libremet.consultas.openweathermap.ParserBuscarOWMJSON;
import org.paxet.libremet.consultas.openweathermap.PeticionesBuscarOWM;
import org.paxet.libremet.db.Poblacion;
import org.paxet.libremet.exceptions.MethodNotImplemented;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ConsultaAsincronaBuscar extends AsyncTask<Object, Void, ArrayList<Poblacion>> {

	private Context context;
	private ListView lvPoblaciones;
	
	@Override
	protected ArrayList<Poblacion> doInBackground(Object... params) {
		context = (Context) params[0];
		lvPoblaciones = (ListView) params[1];
		String filtro = (String) params[2];
		
		ArrayList <Poblacion> pobls = null;
		
		try {
			PeticionesBuscar consulta = (new PeticionesBuscarOWM());
			pobls = ParserBuscarOWMJSON.getPoblaciones((consulta.getPoblacionesJSON(filtro)));
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (MethodNotImplemented e) {
			// TODO Aquí se deberá llamar alternativamente a getPoblacionesXML
			//De momento no hay nada implpmentado ya que se pospone para la versión AEMET
			Log.d("ConsultaAsincronaBuscar", e.getLocalizedMessage());
		}
		
		return pobls;
	}
	
	@Override
	protected void onPostExecute(ArrayList<Poblacion> pobls) {
		super.onPostExecute(pobls);

		// Actualizar el ListView con los datos
		/*String strings[] = new String[pobls.size()];
		Poblacion pobl;
		for (int i = 0; i < strings.length; i++) {
			pobl = pobls.get(i);
			strings[i] = new String(pobl.getID() + ":" + pobl.getNombre() + "," + pobl.getPais());
		}*/

		LvAdapter mAdapter = new LvAdapter(context);
		mAdapter.setList(pobls);
		lvPoblaciones.setAdapter(mAdapter);
	}

	 private static class LvAdapter extends BaseAdapter {
		 private LayoutInflater mInflater;
		 List<Poblacion> pobls;

		 public LvAdapter(Context context) {
			 mInflater = LayoutInflater.from(context);
		 }

		 public void setList(ArrayList<Poblacion> pobls) {
			this.pobls = pobls;
		}

		public int getCount() {
			 return pobls.size();
		 }

		 public Poblacion getItem(int position) {
			 return pobls.get(position);
		 }

		 public long getItemId(int position) {
			 return pobls.get(position).getID();
		 }

		 public View getView(int position, View convertView, ViewGroup parent) {
			 ViewHolder holder;
			 if (convertView == null) {
				 convertView = mInflater.inflate(R.layout.listitem_poblacion, null);
				 holder = new ViewHolder();
				 holder.id = (TextView) convertView
						 .findViewById(R.id.id);
				 holder.nombre = (TextView) convertView
						 .findViewById(R.id.nombre);
				 holder.pais = (TextView) convertView
						 .findViewById(R.id.pais);

				 convertView.setTag(holder);
			 } else {
				 holder = (ViewHolder) convertView.getTag();
			 }

			 Poblacion pobl = pobls.get(position);
			 holder.id.setText(String.valueOf(pobl.getID()));
			 holder.nombre.setText(pobl.getNombre());
			 holder.pais.setText(pobl.getPais());

			 return convertView;
		 }

		 static class ViewHolder {
			 TextView id;
			 TextView nombre;
			 TextView pais;
		 }
	 }
}
