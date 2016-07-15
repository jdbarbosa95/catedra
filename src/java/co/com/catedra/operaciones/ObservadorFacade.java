/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.catedra.operaciones;

import co.com.catedra.eo.Estudiante;
import co.com.catedra.eo.Observacion;
import co.com.catedra.eo.Observador;
import co.com.catedra.eo.Usuario;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Sneider
 */
@Stateless
public class ObservadorFacade extends AbstractFacade<Observador> {
    @PersistenceContext(unitName = "ProyectoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ObservadorFacade() {
        super(Observador.class);
    }
    
    public  Observador traerObservador (Estudiante estudiante){
        try {
         return   em.createQuery("SELECT o FROM Observador o, Estudiante e WHERE  O.estudianteidEstudiante.idEstudiante =e.idEstudiante AND e.idEstudiante=:est",Observador.class)
                    .setParameter("est", estudiante.getIdEstudiante())
                    .getSingleResult();
            
        } catch (Exception e) {
        e.printStackTrace();
        }
        return null;
    }
    
    public List<Observacion> buscarObservadorEstudiante(Observador observador){
        try {
       
             return em.createQuery("SELECT ob FROM Observacion ob, Observador o,Estudiante e WHERE  OB.observadoridObservador.idObservador=o.idObservador AND o.estudianteidEstudiante.idEstudiante=e.idEstudiante AND OB.observadoridObservador.idObservador=:ob",Observacion.class)
                     .setParameter("ob",observador )
                     .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
      return null;
    
    }
     public Observador ObservadorEstudiante(Estudiante estudiante){
        try {
       
             return em.createQuery("SELECT o FROM Observador o,Estudiante e WHERE o.estudianteidEstudiante.idEstudiante=e.idEstudiante AND o.estudianteidEstudiante.idEstudiante =:idEstdiante",Observador.class)
                     .setParameter("idEstdiante", estudiante.getIdEstudiante())
                     .getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
      return null;
    
    }
    
   
    
}
