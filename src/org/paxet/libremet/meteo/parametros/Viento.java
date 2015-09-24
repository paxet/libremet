package org.paxet.libremet.meteo.parametros;

public class Viento {

	private double velocidad;
	private String direccion;
	private double dirGrados;
	
	public double getVelocidad() {
		return velocidad;
	}
	public void setVelocidad(double velocidad) {
		this.velocidad = velocidad;
	}
	public String getDireccion() {
		return direccion;
	}
	private void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public double getDirGrados() {
		return dirGrados;
	}
	public void setDirGrados(double dirGrados) {
		this.dirGrados = dirGrados;
		setDireccion(calculaDirViento(dirGrados));
	}
	
	private String calculaDirViento(double dirGrados2) {
		String dirViento = "";
		//TODO Calcular direccion en base a los grados
		
		return dirViento;
	}

}
