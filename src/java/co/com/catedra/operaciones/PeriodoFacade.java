/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.catedra.operaciones;

import co.com.catedra.eo.Asignatura;
import co.com.catedra.eo.Curso;
import co.com.catedra.eo.Periodo;
import co.com.catedra.eo.Usuario;
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
public class PeriodoFacade extends AbstractFacade<Periodo> {
    @PersistenceContext(unitName = "ProyectoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PeriodoFacade() {
        super(Periodo.class);
    }
public List<Periodo> buscarPeriodosMateriaE(Usuario usuario, Asignatura asignatura) {

        try {
            List<Periodo> lista = new ArrayList();
            List<Periodo> listaQ = new ArrayList();
            listaQ = em.createQuery("SELECT DISTINCT p FROM  Periodo p,Nota n,Usuario u,Estudiante e,Asignatura a WHERE u.idUsuario=e.usuarioidUsuario.idUsuario AND n.estudianteidEstudiante.idEstudiante=e.idEstudiante AND a.idAsignatura=n.asignaturaidAsignatura.idAsignatura AND p.idPeriodo=n.periodoidPeriodo.idPeriodo AND e.usuarioidUsuario.idUsuario=:idUsuario AND a.idAsignatura=:idAsignatura", Periodo.class)
                    .setParameter("idUsuario", usuario.getIdUsuario())
                    .setParameter("idAsignatura", asignatura.getIdAsignatura()).
                    getResultList();
            lista.add(new Periodo(-1));
            lista.addAll(listaQ);
            return lista;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

public List<Periodo> listarPeriodoNotasEstuDocenteUpda(Usuario usuario, Curso curso, Asignatura asignatura) {

        try {
            List<Periodo> lista = new ArrayList();
            List<Periodo> listaQ = new ArrayList();
            listaQ = em.createQuery("SELECT DISTINCT pe FROM Periodo pe,Nota n,Usuario u,Profesor p,Estudiante e,Asignatura a,Curso c,Clase cc WHERE cc.profesoridProfesor.idProfesor=p.idProfesor AND a.idAsignatura=cc.asignaturaidAsignatura.idAsignatura AND c.idCurso=cc.cursoidCurso.idCurso AND e.cursoidCurso.idCurso= c.idCurso AND u.idUsuario = e.usuarioidUsuario.idUsuario AND n.estudianteidEstudiante.idEstudiante=e.idEstudiante AND pe.idPeriodo=n.periodoidPeriodo.idPeriodo AND p.idProfesor=:idUsuario AND c.idCurso=:idCurso AND a.idAsignatura=:idAsignatura", Periodo.class)
                    .setParameter("idUsuario", usuario.getIdUsuario())
                    .setParameter("idCurso", curso.getIdCurso())
                    .setParameter("idAsignatura", asignatura.getIdAsignatura())
                    .getResultList();
            lista.add(new Periodo(-1));
            lista.addAll(listaQ);
            return lista;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
