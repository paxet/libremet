package org.paxet.libremet.meteo;

import org.paxet.libremet.meteo.parametros.Lluvia;
import org.paxet.libremet.meteo.parametros.Nieve;
import org.paxet.libremet.meteo.parametros.Nubes;
import org.paxet.libremet.meteo.parametros.Temperatura;
import org.paxet.libremet.meteo.parametros.Viento;

import android.graphics.Bitmap;

public class Prediccion {

	private Localizacion localizacion;
	private CondicionesActuales condicionesActuales = new CondicionesActuales();
	private Temperatura temperatura = new Temperatura();
	private Viento viento = new Viento();
	private Lluvia lluvia = new Lluvia();
	private Nieve nieve = new Nieve();
	private Nubes nubes = new Nubes();
	private long hora;
	
	private Bitmap icono;
	
	public Localizacion getLocalizacion() {
		return localizacion;
	}
	public void setLocalizacion(Localizacion localizacion) {
		this.localizacion = localizacion;
	}
	public CondicionesActuales getCondicionesActuales() {
		return condicionesActuales;
	}
	public void setCondicionesActuales(CondicionesActuales condicionesActuales) {
		this.condicionesActuales = condicionesActuales;
	}
	public Temperatura getTemperatura() {
		return temperatura;
	}
	public void setTemperatura(Temperatura temperatura) {
		this.temperatura = temperatura;
	}
	public Viento getViento() {
		return viento;
	}
	public void setViento(Viento viento) {
		this.viento = viento;
	}
	public Lluvia getLluvia() {
		return lluvia;
	}
	public void setLluvia(Lluvia lluvia) {
		this.lluvia = lluvia;
	}
	public Nieve getNieve() {
		return nieve;
	}
	public void setNieve(Nieve nieve) {
		this.nieve = nieve;
	}
	public Nubes getNubes() {
		return nubes;
	}
	public void setNubes(Nubes nubes) {
		this.nubes = nubes;
	}
	public long getHora() {
		return hora;
	}
	public void setHora(long hora) {
		this.hora = hora;
	}
	public Bitmap getIcono() {
		return icono;
	}
	public void setIcono(Bitmap icono) {
		this.icono = icono;
	}
}
