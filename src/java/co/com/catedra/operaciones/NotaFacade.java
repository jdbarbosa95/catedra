/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.catedra.operaciones;

import co.com.catedra.eo.Asignatura;
import co.com.catedra.eo.Curso;
import co.com.catedra.eo.Estudiante;
import co.com.catedra.eo.Nota;
import co.com.catedra.eo.Periodo;
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
public class NotaFacade extends AbstractFacade<Nota> {
    @PersistenceContext(unitName = "ProyectoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public NotaFacade() {
        super(Nota.class);
    }
    
      public List<Nota> buscarMisNotasEstudiante(Estudiante estudiante,Asignatura asignatura) {
       try {
            return em.createQuery("SELECT  n FROM Nota n,Usuario u,Asignatura a,Estudiante e WHERE u.idUsuario=e.usuarioidUsuario.idUsuario AND n.estudianteidEstudiante.idEstudiante=e.idEstudiante AND a.idAsignatura= n.asignaturaidAsignatura.idAsignatura AND e.usuarioidUsuario.idUsuario=:idUsuario  AND a.nomAsignatura=:nombreA",Nota.class)
                    .setParameter("idUsuario", estudiante.getIdEstudiante())
                  .setParameter("nombreA", asignatura.getIdAsignatura())
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
    return null; 
    }
      
    public List<Nota> BuscarNotasPorCurso(Usuario usuario ,Curso curso,Asignatura asignatura,Periodo periodo){
        try {
            return em.createQuery("SELECT n FROM Clase cl; Nota n,Estudiante e,Usuario u,Curso c,Asignatura a,Profesor p WHERE u.idUsuario=e.usuarioidUsuario.idUsuario AND n.estudianteidEstudiante.idEstudiante=e.idEstudiante AND n.asignaturaidAsignatura.idAsignatura=a.idAsignatura AND e.cursoidCurso.idCurso=c.idCurso AND p.usuarioidUsuario.idUsuario=:idusu AND C.idCurso=:idcurso AND a.idAsignatura=:idasignatura AND n.periodoidPeriodo.idPeriodo=:peridodo ORDER BY e ",Nota.class)
                    .setParameter("idusu", usuario.getIdUsuario())
                    .setParameter("idcurso", curso.getIdCurso())
                    .setParameter("idasignatura",asignatura.getIdAsignatura())
                    .setParameter("periodo", periodo.getIdPeriodo())
                    .getResultList();
       
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public List<Nota> buscarMisNotasEstudiante(Usuario estudiante, Asignatura asignatura, Periodo periodo) {
        try {
            return em.createQuery("SELECT n FROM  Nota n,Periodo p,Usuario u,Estudiante e,Asignatura a WHERE u.idUsuario=e.usuarioidUsuario.idUsuario AND n.estudianteidEstudiante.idEstudiante=e.idEstudiante AND a.idAsignatura=n.asignaturaidAsignatura.idAsignatura AND p.idPeriodo=n.periodoidPeriodo.idPeriodo AND e.usuarioidUsuario.idUsuario=:idUsuario AND a.idAsignatura=:idAsignatura AND p.idPeriodo=:idPeriodo", Nota.class)
                    .setParameter("idUsuario", estudiante.getIdUsuario())
                    .setParameter("idAsignatura", asignatura.getIdAsignatura())
                    .setParameter("idPeriodo", periodo.getIdPeriodo())
                    .getResultList();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
 public List<Nota> listarNotasEstuDocenteUpda(Usuario usuario, Curso curso, Asignatura asignatura,Periodo periodo) {
        //SELECT asignatura.NomAsignatura,usuario.Nombres FROM clase INNER JOIN profesor ON clase.Profesor_idProfesor= profesor.idProfesor INNER JOIN asignatura ON clase.Asignatura_idAsignatura= asignatura.idAsignatura INNER JOIN curso ON clase.Curso_idCurso=curso.idCurso INNER join estudiante on curso.idCurso = estudiante.Curso_idCurso INNER JOIN usuario on usuario.idUsuario = estudiante.Usuario_idUsuario WHERE curso.idCurso=2 AND profesor.idProfesor=1 AND asignatura.idAsignatura=4
        try {
            //SELECT asignatura.NomAsignatura,usuario.Nombres,nota.Valoracion FROM clase INNER JOIN profesor 
                                                                                                                     //ON clase.Profesor_idProfesor= profesor.idProfesor 
                                                                                                                                                           //INNER JOIN asignatura ON clase.Asignatura_idAsignatura= asignatura.idAsignatura 
                                                                                                                                                                                                                                  //INNER JOIN curso ON clase.Curso_idCurso=curso.idCurso 
                                                                                                                                                                                                                                                                                    //INNER join estudiante on curso.idCurso = estudiante.Curso_idCurso
                                                                                                                                                                                                                                                                                                                            // INNER JOIN usuario on usuario.idUsuario = estudiante.Usuario_idUsuario
                                                                                                                                                                                                                                                                                                                                                                        // inner join nota on nota.Estudiante_idEstudiante=estudiante.idEstudiante 
                                                                                                                                                                                                                                                                                                                                                                            //inner JOIN periodo on nota.Periodo_idPeriodo=periodo.idPeriodo 
                                                                                                                                                                                                                                                                                                                                                                                                                                                                          //WHERE profesor.idProfesor=1 AND asignatura.idAsignatura=4 And curso.idCurso=2 AND periodo.idPeriodo=1
            return em.createQuery("SELECT n FROM Nota n,Usuario u,Profesor p,Estudiante e,Asignatura a,Curso c,Clase cc,Periodo pe WHERE cc.profesoridProfesor.idProfesor=p.idProfesor AND a.idAsignatura=cc.asignaturaidAsignatura.idAsignatura AND c.idCurso=cc.cursoidCurso.idCurso AND e.cursoidCurso.idCurso= c.idCurso AND u.idUsuario = e.usuarioidUsuario.idUsuario AND n.estudianteidEstudiante.idEstudiante=e.idEstudiante AND pe.idPeriodo=n.periodoidPeriodo.idPeriodo AND p.idProfesor=:idUsuario AND c.idCurso=:idCurso AND a.idAsignatura=:idAsignatura AND pe.idPeriodo=:idPeriodo", Nota.class)
                    .setParameter("idUsuario", usuario.getIdUsuario())
                    .setParameter("idCurso", curso.getIdCurso())
                    .setParameter("idAsignatura", asignatura.getIdAsignatura())
                    .setParameter("idPeriodo", periodo.getIdPeriodo())
                    .getResultList();
// p.idProfesor=:idUsuario AND c.idCurso=:idCurso AND a.idAsignatura=:idAsignatura AND pe.idPeriodo=:idPeriodo
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
