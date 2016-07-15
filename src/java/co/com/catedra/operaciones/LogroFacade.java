/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.catedra.operaciones;

import co.com.catedra.eo.Asignatura;
import co.com.catedra.eo.Logro;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Sneider
 */
@Stateless
public class LogroFacade extends AbstractFacade<Logro> {
    @PersistenceContext(unitName = "ProyectoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public LogroFacade() {
        super(Logro.class);
    }
    
    public  List<Logro> listaLogrosPorAsignatura(Asignatura asignatura){
    
        try {
            List<Logro> lista= new ArrayList();
            List<Logro> listaQ= new ArrayList();
            listaQ = em.createQuery("SELECT l FROM Logro l,Asignatura a WHERE a.idAsignatura=l.asignaturaidAsignatura.idAsignatura AND a.idAsignatura=:idAsignatura",Logro.class)
                    .setParameter("idAsignatura", asignatura.getIdAsignatura())
                    .getResultList();
            lista.add(new Logro(-1, "---"));
            lista.addAll(listaQ);
            return lista;
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    
}
