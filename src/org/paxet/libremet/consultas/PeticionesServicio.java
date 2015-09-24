package org.paxet.libremet.consultas;

import org.paxet.libremet.exceptions.MethodNotImplemented;

public interface PeticionesServicio {
	
	public final int PREDICCION_ACTUAL = 1;
	public final int PREDICCION_CINCO_DIAS = 2;
	public final int PREDICCION_CATORCE_DIAS = 3;

	public String getPrevisionJSON(long idpoblacion, int tipoPrediccion) throws MethodNotImplemented;
	public String getPrevisionXML(long idpoblacion, int tipoPrediccion) throws MethodNotImplemented;
	public byte[] getImagen(String codigo) throws MethodNotImplemented;
}
