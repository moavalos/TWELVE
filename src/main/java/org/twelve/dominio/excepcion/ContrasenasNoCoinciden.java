package org.twelve.dominio.excepcion;

public class ContrasenasNoCoinciden extends RuntimeException {

    public ContrasenasNoCoinciden() {
        super("Las contraseñas no coinciden");
    }
}
