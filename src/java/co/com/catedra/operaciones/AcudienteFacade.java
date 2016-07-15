/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.catedra.operaciones;

import co.com.catedra.eo.Acudiente;
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
public class AcudienteFacade extends AbstractFacade<Acudiente> {
    @PersistenceContext(unitName = "ProyectoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AcudienteFacade() {
        super(Acudiente.class);
    }
    
public List<Acudiente> listaAcudiente(){
    try {
       return em.createQuery("SELECT a FROM Acudiente a ")
                .getResultList();
    } catch (Exception e) {
    e.printStackTrace();
    }
    return null;
}
public List<Acudiente> usuariosAcu(){
        try {
    return em.createQuery("SELECT a FROM Usuario u,Acudiente a WHERE a.usuarioidUsuario.idUsuario=u.idUsuario AND u.rolidRol.idRol=3")
                    .getResultList();
        } catch (Exception e) {
        e.printStackTrace();
        }
        
        return null;
    }
public Acudiente buscarProfesor(Usuario usuario) {
        try {
            return (Acudiente) em.createNamedQuery("Acudiente.buscarProfesor")
                .setParameter("idUs", usuario.getIdUsuario()).getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  null;
    }

}
