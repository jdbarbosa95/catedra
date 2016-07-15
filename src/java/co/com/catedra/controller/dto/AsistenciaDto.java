/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.catedra.controller.dto;

import co.com.catedra.eo.Clase;
import co.com.catedra.eo.Estudiante;
import co.com.catedra.eo.Regitroasistencia;
import java.io.Serializable;

/**
 *
 * @author sebastianPc
 */
public class AsistenciaDto implements Serializable{
    private Estudiante estudiante;
    private Regitroasistencia regitroAsistensia;
    private Clase clase;

    
    
    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    public Regitroasistencia getRegitroAsistensia() {
        return regitroAsistensia;
    }

    public void setRegitroAsistensia(Regitroasistencia regitroAsistensia) {
        this.regitroAsistensia = regitroAsistensia;
    }

    public Clase getClase() {
        return clase;
    }

    public void setClase(Clase clase) {
        this.clase = clase;
    }
    
    

    
}
