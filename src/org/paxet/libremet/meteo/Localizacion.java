package org.paxet.libremet.meteo;

public class Localizacion {
	
	private double latitud;
	private double longitud;
	private long ocaso;
	private long amanecer;
	private String pais;
	private String ciudad;
	
	public double getLatitud() {
		return latitud;
	}
	public void setLatitud(double latitud) {
		this.latitud = latitud;
	}
	public double getLongitud() {
		return longitud;
	}
	public void setLongitud(double longitud) {
		this.longitud = longitud;
	}
	public long getOcaso() {
		return ocaso;
	}
	public void setOcaso(long ocaso) {
		this.ocaso = ocaso;
	}
	public long getAmanecer() {
		return amanecer;
	}
	public void setAmanecer(long amanecer) {
		this.amanecer = amanecer;
	}
	public String getPais() {
		return pais;
	}
	public void setPais(String pais) {
		this.pais = pais;
	}
	public String getCiudad() {
		return ciudad;
	}
	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}
	

}
