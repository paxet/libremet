package org.paxet.libremet.consultas;

import org.paxet.libremet.exceptions.MethodNotImplemented;

public interface PeticionesBuscar {

	public String getPoblacionesJSON(String filtro) throws MethodNotImplemented;
	public String getPoblacionesXML(String filtro) throws MethodNotImplemented;
}
