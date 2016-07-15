/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.catedra.operaciones;

import co.com.catedra.eo.Clase;
import co.com.catedra.eo.Curso;
import co.com.catedra.eo.Profesor;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Sneider
 */
@Stateless
public class ClaseFacade extends AbstractFacade<Clase> {
    @PersistenceContext(unitName = "ProyectoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ClaseFacade() {
        super(Clase.class);
    }
    public List<Profesor> ProfesorDisponible(String dia,Date hora){
        try {
          
           return em.createQuery("SELECT p FROM Profesor p,Clase cl  WHERE  cl.profesoridProfesor.idProfesor=p.idProfesor AND  NOT cl.dia LIKE :dia AND NOT cl.hora=:hora")
                    .setParameter("dia", dia)
                    .setParameter("hora", hora)
                    .getResultList();
            
          
        } catch (Exception e) {
        }
        
        return null;
    }
    
    public List<Clase> clasePorCurso(Curso curso,Profesor profesor){
        try {
           return em.createQuery("SELECT cl FROM Clase cl,Curso c,Profesor p WHERE cl.cursoidCurso.idCurso=c.idCurso AND cl.profesoridProfesor.idProfesor=p.idProfesor AND cl.cursoidCurso.idCurso=:curso AND cl.profesoridProfesor.idProfesor=:profesor")
                    .setParameter("curso", curso.getIdCurso())
                    .setParameter("profesor",profesor.getIdProfesor())
                    .getResultList();
            
           
        } catch (Exception e) {
        e.printStackTrace();
        }
        return null;
    }
}
