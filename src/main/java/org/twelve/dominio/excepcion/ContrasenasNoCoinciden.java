package org.twelve.dominio.excepcion;

public class ContrasenasNoCoinciden extends RuntimeException {
    public ContrasenasNoCoinciden(String message) {

        super(message);
    }
}
