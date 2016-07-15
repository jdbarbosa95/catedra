/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.catedra.operaciones;

import co.com.catedra.eo.Asignatura;
import co.com.catedra.eo.Curso;
import co.com.catedra.eo.Estudiante;
import co.com.catedra.eo.Profesor;
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
public class AsignaturaFacade extends AbstractFacade<Asignatura> {

    @PersistenceContext(unitName = "ProyectoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AsignaturaFacade() {
        super(Asignatura.class);
    }

    public List<Asignatura> buscarMisAsignaturas(Profesor profesor) {
        try {
            return em.createQuery("SELECT a FROM Asignatura a,Curso c,Clase cc,Profesor p WHERE cc.profesoridProfesor.idProfesor=p.idProfesor AND a.idAsignatura = cc.asignaturaidAsignatura.idAsignatura AND c.idCurso = cc.cursoidCurso.idCurso AND p.usuarioidUsuario.idUsuario=:idUsuario", Asignatura.class)
                    .setParameter("idUsuario", profesor.getIdProfesor())
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Asignatura> buscarMisAsignaturasProfesorCurso(Usuario profesor, Curso curso) {
        try {
            List<Asignatura> lista = new ArrayList();
            List<Asignatura> listaQ = new ArrayList();

            if (curso != null && curso.getIdCurso().equals(-1)) {
                return new ArrayList<Asignatura>();
            }
            listaQ = em.createQuery("SELECT a FROM Asignatura a,Usuario u,Profesor p,Clase cc,Curso c WHERE p.usuarioidUsuario.idUsuario=u.idUsuario AND  cc.profesoridProfesor.idProfesor=p.idProfesor AND a.idAsignatura=cc.asignaturaidAsignatura.idAsignatura AND c.idCurso=cc.cursoidCurso.idCurso AND p.usuarioidUsuario.idUsuario=:idUsuario AND c.idCurso=:idCurso", Asignatura.class)
                    .setParameter("idUsuario", profesor.getIdUsuario())
                    .setParameter("idCurso", curso.getIdCurso())
                    .getResultList();
            lista.add(new Asignatura(-1, "--"));
            lista.addAll(listaQ);
            return lista;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    

 public List<Asignatura> buscarMisAsignaturasEstudiante(Estudiante estudiante) {
        try {

            List<Asignatura> lista = new ArrayList();
            List<Asignatura> lisatQ = new ArrayList();
          
            lisatQ = em.createQuery("SELECT DISTINCT a FROM Asignatura a,Usuario u,Estudiante e,Nota n WHERE u.idUsuario=e.usuarioidUsuario.idUsuario AND n.estudianteidEstudiante.idEstudiante=e.idEstudiante AND a.idAsignatura=n.asignaturaidAsignatura.idAsignatura AND e.idEstudiante=:idUsuario", Asignatura.class)
                    .setParameter("idUsuario", estudiante.getIdEstudiante())
                    .getResultList();
            lista.add(new Asignatura(-1, "--"));
            lista.addAll(lisatQ);
            return lista;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public List<Asignatura> buscarAsignaturasEstudianteAcudiente(Usuario estudiante) {
        try {

            List<Asignatura> lista = new ArrayList();
            List<Asignatura> lisatQ = new ArrayList();
           // SELECT DISTINCT asignatura.NomAsignatura from //usuario inner join estudiante on usuario.idUsuario = estudiante.Usuario_idUsuario INNER JOIN nota on estudiante.idEstudiante = nota.Estudiante_idEstudiante inner join asignatura on nota.Asignatura_idAsignatura = asignatura.idAsignatura WHERE usuario.idUsuario=9
            lisatQ = em.createQuery("SELECT DISTINCT a FROM Asignatura a,Usuario u,Estudiante e,Nota n WHERE u.idUsuario=e.usuarioidUsuario.idUsuario AND n.estudianteidEstudiante.idEstudiante=e.idEstudiante AND a.idAsignatura=n.asignaturaidAsignatura.idAsignatura AND e.usuarioidUsuario.idUsuario=:idUsuario", Asignatura.class)
                    .setParameter("idUsuario", estudiante.getIdUsuario())
                    .getResultList();
            lista.add(new Asignatura(-1, "---"));
            lista.addAll(lisatQ);
            return lista;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
