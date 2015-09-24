package org.paxet.libremet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.paxet.libremet.consultas.PeticionesServicio;
import org.paxet.libremet.consultas.openweathermap.ParserOWMJSON;
import org.paxet.libremet.consultas.openweathermap.PeticionesOWM;
import org.paxet.libremet.exceptions.MethodNotImplemented;
import org.paxet.libremet.meteo.Prediccion;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class TabFragmentListView extends Fragment {

	private AdaptadorPredicciones adpt;
	private int prediccion_dias;
	private long poblacion = -1L;
	private SimpleDateFormat sdf;
	private final long PASO_A_MILISEGUNDOS = 1000L;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Bundle data = getArguments();
		prediccion_dias = data.getInt("prediccion_dias");
		
		if (prediccion_dias == PeticionesServicio.PREDICCION_CATORCE_DIAS) {
			sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
		} else {
			sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault());
		}
		
		poblacion = data.getLong("poblacion");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		ConsultaAsincrona tarea = new ConsultaAsincrona();
	    tarea.execute(prediccion_dias, poblacion);
	    
		adpt = new AdaptadorPredicciones(new Prediccion[]{}, getActivity());

		View v = inflater.inflate(R.layout.fragment_listview, null);
		ListView lstPredicciones = (ListView) v.findViewById(R.id.LstPredicciones);

		lstPredicciones.setAdapter(adpt);

		return lstPredicciones;
	}
	
	class AdaptadorPredicciones extends ArrayAdapter<Prediccion> {

		private Prediccion[] datos;
		private Activity context;

		AdaptadorPredicciones(Prediccion[] datos, Activity context) {
			super(context, R.layout.listitem_prediccion, datos);
			this.datos = datos;
			this.context = context;
		}
		
		public int getCount() {
			int count = 0;
	        if (datos != null) {
	            count = datos.length;
	        }
	        return count;
	    }

	    public Prediccion getItem(int position) {
	    	Prediccion aDevolver = null;
	        if (datos != null) {
	            aDevolver = datos[position];
	        }
	        return aDevolver;
	    }

	    public long getItemId(int position) {
	    	int id = 0;
	        if (datos != null)
	            id = datos[position].hashCode();
	        return id;
	    }

		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = context.getLayoutInflater();
			View item = inflater.inflate(R.layout.listitem_prediccion, null);

			Prediccion pred = datos[position];
			
			ImageView imgView = (ImageView) item.findViewById(R.id.condIcon);
			
			/*byte[] bytesIcono = pred.getBytesIcono();
			if (bytesIcono != null && bytesIcono.length > 0) {
				Bitmap img = BitmapFactory.decodeByteArray(bytesIcono, 0, bytesIcono.length);
				if( img!= null) {
					imgView.setImageBitmap(img);
				} else {
					Log.d("AdaptadorPredicciones", "Los bytes de la imagen no se han podido decodificar");
				}
				
			}*/
			imgView.setImageBitmap(pred.getIcono());

			//Lo he quitado del Layout
			/*TextView tview = (TextView) item.findViewById(R.id.cityText);
			tview.setText(pred.getLocalizacion().getCiudad() + ","
					+ pred.getLocalizacion().getPais());*/
			
			TextView tview = (TextView) item.findViewById(R.id.datetimePredText);
			tview.setText(sdf.format(new Date(pred.getHora() * PASO_A_MILISEGUNDOS)));//Ese *1000 es porque OWM nos env√≠a la hora en forma UNIX TIME (sin milisegundos)
			
			//Lo he quitado del Layout
			/*tview = (TextView) item.findViewById(R.id.condDescr);
			tview.setText(pred.getCondicionesActuales().getCondicion()
					+ "(" + pred.getCondicionesActuales().getDesc() + ")");*/
			
			tview = (TextView) item.findViewById(R.id.temp);
			tview.setText("" + Math.round((pred.getTemperatura().getTemp())) + "∫C");
			
			tview = (TextView) item.findViewById(R.id.hum);
			tview.setText("" + pred.getCondicionesActuales().getHumedad() + "%");
			
			tview = (TextView) item.findViewById(R.id.press);
			tview.setText("" + pred.getCondicionesActuales().getPresion() + " hPa");
			
			tview = (TextView) item.findViewById(R.id.windSpeed);
			tview.setText("" + pred.getViento().getVelocidad() + " m/s");
			
			//Lo he quitado del Layout
			/*tview = (TextView) item.findViewById(R.id.windDeg);
			tview.setText("" + pred.getViento().getDirGrados() + "\");*/
			
			return (item);
		}
		
		public Prediccion[] getItemList() {
			return datos;
		}

		public void setItemList(Prediccion[] datos) {
			this.datos = datos;
		}
	}
	
	class ConsultaAsincrona extends AsyncTask<Object, Void, ArrayList<Prediccion>> {

		private ProgressDialog dialog;
		
		@Override
		protected ArrayList<Prediccion> doInBackground(Object... params) {
			int prediccion = (Integer)params[0]; //PeticionesServicio.PREDICCION_
			long poblacion = (Long) params[1];
			
			ArrayList<Prediccion> preds = null;
			PeticionesServicio consulta = new PeticionesOWM();
			try {
				if (prediccion_dias == PeticionesServicio.PREDICCION_ACTUAL) {
					preds = new ArrayList<Prediccion>();
					preds.add(ParserOWMJSON.getPrediccionActual(consulta.getPrevisionJSON(poblacion, PeticionesServicio.PREDICCION_ACTUAL)));
				} else {
					preds = ParserOWMJSON.getPredicciones(consulta.getPrevisionJSON(poblacion, prediccion));
				}


			} catch (JSONException e) {
				e.printStackTrace();
			} catch (MethodNotImplemented e) {
				// TODO Aqui se debera llamar alternativamente a getPrevisionXML
				//De momento no hay nada implpmentado ya que se pospone para la version AEMET
				Log.d("ConsultaAsincrona", e.getLocalizedMessage());
			}
			try {
				Bitmap img = null;
				int intentos = 3;
				byte[] bytesIcono;
				
				for (Prediccion pred : preds) {
					//Con este codigo consigo que se muestre siempre la imagen pero ralentiza la carga en pantalla
					while (img == null || intentos > 0) {
						bytesIcono = consulta.getImagen(pred.getCondicionesActuales().getIcono());
						if (bytesIcono != null && bytesIcono.length > 0) {
							img = BitmapFactory.decodeByteArray(bytesIcono, 0, bytesIcono.length);
						}
						intentos--;
					}
					
					//Lo ponemos en la prediccion si contiene algo
					if (img != null) {
						pred.setIcono(img);
					}
					
					//Establecemos de nuevo los intentos en 3 para la nueva iteracion
					intentos = 3;
				}

			} catch (MethodNotImplemented e) {
				// TODO Aqui debera poner un icono por defecto si no esta implementado el metodo
				//Sucede actualmente para version AEMET
				Log.d("ConsultaAsincrona", e.getLocalizedMessage());
			}
			return preds;

		}
		
		@Override
	    protected void onPreExecute() {        
	        super.onPreExecute();
	        dialog = ProgressDialog.show(getActivity(), getString(R.string.espere), getString(R.string.descargando_prediccion));
	        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
	        dialog.show();            
	    }


		@Override
		protected void onPostExecute(ArrayList<Prediccion> preds) {
			super.onPostExecute(preds);
			Prediccion[] vectorPreds = new Prediccion[preds.size()];
			for (int i = 0; i < preds.size(); i++) {
				vectorPreds[i] = preds.get(i);
			}
	        adpt.setItemList(vectorPreds);
	        adpt.notifyDataSetChanged();
	        dialog.dismiss();
		}

	}
}
