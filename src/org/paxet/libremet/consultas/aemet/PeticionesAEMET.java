package org.paxet.libremet.consultas.aemet;

import org.paxet.libremet.consultas.PeticionesServicio;
import org.paxet.libremet.exceptions.MethodNotImplemented;

public class PeticionesAEMET implements PeticionesServicio {
	
	private String DEFAULT_TEXT = "No se han implementado todavía las peticiones al servicio AEMET en esta versión de la app";

	@Override
	public String getPrevisionJSON(long idpoblacion, int tipoPrediccion) throws MethodNotImplemented {
		throw new MethodNotImplemented(DEFAULT_TEXT);
	}

	@Override
	public String getPrevisionXML(long idpoblacion, int tipoPrediccion) throws MethodNotImplemented {
		throw new MethodNotImplemented(DEFAULT_TEXT);
	}

	@Override
	public byte[] getImagen(String codigo) throws MethodNotImplemented {
		throw new MethodNotImplemented(DEFAULT_TEXT);
	}

}
