/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.catedra.operaciones;

import co.com.catedra.eo.Estudiante;
import co.com.catedra.eo.Observacion;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Sneider
 */
@Stateless
public class ObservacionFacade extends AbstractFacade<Observacion> {
    @PersistenceContext(unitName = "ProyectoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ObservacionFacade() {
        super(Observacion.class);
    }
    
    public List<Observacion> busacarObservaciones(Estudiante estudiante){
        try {
            
          return  em.createQuery("SELECT o FROM Observacion o,Observador ob, Estudiante e WHERE o.observadoridObservador.idObservador=ob.idObservador AND o.observadoridObservador.estudianteidEstudiante.idEstudiante=e.idEstudiante AND ob.estudianteidEstudiante.idEstudiante=e.idEstudiante AND OB.estudianteidEstudiante.idEstudiante=:idestudiante ",Observacion.class)
                    .setParameter("estudiante", estudiante.getIdEstudiante())
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
        
    }
    
}
