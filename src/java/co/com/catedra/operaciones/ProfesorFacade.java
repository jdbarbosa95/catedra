/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.catedra.operaciones;

import co.com.catedra.eo.Estudiante;
import co.com.catedra.eo.Profesor;
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
public class ProfesorFacade extends AbstractFacade<Profesor> {
    @PersistenceContext(unitName = "ProyectoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProfesorFacade() {
        super(Profesor.class);
    }
    
    
    public Profesor buscarProfesor(Usuario usuario) {
        try {
            return (Profesor) em.createNamedQuery("Profesor.buscarProfesor")
                .setParameter("idUs", usuario.getIdUsuario()).getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  null;
    }
    
  public List<Profesor> usuariosProfesor(){
        try {
    return em.createQuery("SELECT p FROM Usuario u,Profesor p WHERE p.usuarioidUsuario.idUsuario=u.idUsuario AND u.rolidRol.idRol=2")
                    .getResultList();
        } catch (Exception e) {
        e.printStackTrace();
        }
        
        return null;
    }    
    
}
