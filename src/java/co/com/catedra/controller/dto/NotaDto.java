/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.catedra.controller.dto;

import co.com.catedra.eo.Estudiante;
import co.com.catedra.eo.Nota;

/**
 *
 * @author Sneider
 */
public class NotaDto {
    
    private Estudiante estudiante;
    private Nota nota;
    

    public Estudiante getEstudiante() {
        return estudiante;
    }
    

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    public Nota getNota() {
        return nota;
    }

    public void setNota(Nota nota) {
        this.nota = nota;
    }
    
    
}
