/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.catedra.operaciones;

import co.com.catedra.eo.Curso;
import co.com.catedra.eo.Profesor;
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
public class CursoFacade extends AbstractFacade<Curso> {
    @PersistenceContext(unitName = "ProyectoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CursoFacade() {
        super(Curso.class);
    }

    public List<Curso> buscarMisCursos(Profesor profesor){
    
        try {
            List<Curso> lista = new ArrayList();
            List<Curso> listaQ = new ArrayList();

            listaQ= em.createQuery("SELECT DISTINCT c  FROM Curso c,Clase cc,Profesor p,Usuario u  WHERE u.idUsuario=p.usuarioidUsuario.idUsuario AND cc.cursoidCurso.idCurso = c.idCurso AND cc.profesoridProfesor.idProfesor = p.idProfesor AND p.idProfesor=:idProfesor ORDER BY c.nombre  DESC",Curso.class)
                    .setParameter("idProfesor", profesor.getIdProfesor())
                    .getResultList();
            lista.add(new Curso(-1, "--"));
            lista.addAll(listaQ);
            return lista;
        } catch (Exception e) {
            e.printStackTrace();
        }
    return null;
    }
    
    
}
