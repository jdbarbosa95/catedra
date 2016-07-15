/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.catedra.controller.dto;


import co.com.catedra.eo.Estudiante;
import co.com.catedra.eo.Observacion;
import co.com.catedra.eo.Observador;

/**
 *
 * @author sebastianPc
 */
public class ObservacionDto {
    
   
    private Observador observador;
    private Observacion observacion;
    private Estudiante estudiante;

     public Observador getObservador() {
        return observador;
    }

    public void setObservador(Observador observador) {
        this.observador = observador;
    }

    public Observacion getObservacion() {
        return observacion;
    }

    public void setObservacion(Observacion observacion) {
        this.observacion = observacion;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }
    
    
    
    
}
