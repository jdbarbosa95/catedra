/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.catedra.operaciones;

import co.com.catedra.eo.Estudiante;
import co.com.catedra.eo.Regitroasistencia;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Sneider
 */
@Stateless
public class RegitroasistenciaFacade extends AbstractFacade<Regitroasistencia> {
    @PersistenceContext(unitName = "ProyectoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RegitroasistenciaFacade() {
        super(Regitroasistencia.class);
    }

    public List<Regitroasistencia> verAsistencia(Estudiante usuario){
        
        try {
           return em.createQuery("SELECT ra FROM Regitroasistencia ra,Estudiante e WHERE ra.estudianteidEstudiante.idEstudiante=e.idEstudiante AND RA.estudianteidEstudiante.idEstudiante=:usuario",Regitroasistencia.class)
                    .setParameter("usuario", usuario.getIdEstudiante())
                    .getResultList();
        } catch (Exception e) {
        e.printStackTrace();
        }
    return null;
    }
    
    
    
}
