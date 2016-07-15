/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.catedra.operaciones;

import co.com.catedra.eo.Asignatura;
import co.com.catedra.eo.Curso;
import co.com.catedra.eo.Estudiante;
import co.com.catedra.eo.Regitroasistencia;
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
public class EstudianteFacade extends AbstractFacade<Estudiante> {
    @PersistenceContext(unitName = "ProyectoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EstudianteFacade() {
        super(Estudiante.class);
    }
    
       
    public List<Estudiante> listarEstudiantesP(Usuario usuario,Curso curso,Asignatura asignatura ){
    //SELECT asignatura.NomAsignatura,usuario.Nombres FROM clase INNER JOIN profesor ON clase.Profesor_idProfesor= profesor.idProfesor INNER JOIN asignatura ON clase.Asignatura_idAsignatura= asignatura.idAsignatura INNER JOIN curso ON clase.Curso_idCurso=curso.idCurso INNER join estudiante on curso.idCurso = estudiante.Curso_idCurso INNER JOIN usuario on usuario.idUsuario = estudiante.Usuario_idUsuario WHERE curso.idCurso=2 AND profesor.idProfesor=1 AND asignatura.idAsignatura=4
        try {
              return em.createQuery("SELECT e FROM Estudiante e ,Asignatura a ,Usuario u ,Profesor p ,Clase cc, Curso c WHERE cc.profesoridProfesor.idProfesor=p.idProfesor AND a.idAsignatura=cc.asignaturaidAsignatura.idAsignatura AND c.idCurso= cc.cursoidCurso.idCurso AND e.cursoidCurso.idCurso= c.idCurso AND u.idUsuario=e.usuarioidUsuario.idUsuario AND p.usuarioidUsuario.idUsuario=:idUsuario AND a.idAsignatura=:nomAsignatura  AND c.idCurso=:curso  ORDER BY e ",Estudiante.class)
                    .setParameter("idUsuario", usuario.getIdUsuario())
                    .setParameter("curso", curso.getIdCurso())
                    .setParameter("nomAsignatura", asignatura.getIdAsignatura())
                    .getResultList();
          
        } catch (Exception e) {
            e.printStackTrace();
        }
    return null;
    }
    public List<Estudiante> ListaEstudiatePorCorso(Curso curso){
        try {
         return em.createQuery("SELECT e FROM Estudiante e  WHERE   e.cursoidCurso.idCurso=:curso ORDER BY e ",Estudiante.class)
                     .setParameter("curso", curso.getIdCurso())
                    .getResultList();         
            
            
        } catch (Exception e) {
        e.printStackTrace();
        }
        
        return null;
    }
    public List<Regitroasistencia> MostrarAsistencia(Curso curso){
        try {
         return em.createQuery("SELECT a FROM Estudiante e, Regitroasistencia a  WHERE  A.estudianteidEstudiante.idEstudiante=e.idEstudiante AND e.cursoidCurso.idCurso=:curso ORDER BY e ",Regitroasistencia.class)
                     .setParameter("curso", curso.getIdCurso())
                    .getResultList();         
            
            
        } catch (Exception e) {
        e.printStackTrace();
        }
        
        return null;
    }
    
   
    
      public Estudiante buscarEstudiante(Usuario usuario) {
        try {
            return (Estudiante) em.createQuery("select e FROM Estudiante e WHERE e.usuarioidUsuario.idUsuario=:idUs")
                .setParameter("idUs", usuario.getIdUsuario()).getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  null;
    }
    
    public List<Estudiante> usuariosEsstudiante(){
        try {
    return em.createQuery("SELECT e FROM Usuario u,Estudiante e WHERE e.usuarioidUsuario.idUsuario=u.idUsuario AND u.rolidRol.idRol=1")
                    .getResultList();
        } catch (Exception e) {
        e.printStackTrace();
        }
        
        return null;
    }
    
public List<Estudiante> buscardocumento(int documento){
    
    try {
 return em.createQuery("SELECT e FROM Usuario u,Estudiante e WHERE e.usuarioidUsuario.idUsuario=u.idUsuario AND u.rolidRol.idRol=1 AND u.documento=:documento")
                .setParameter("documento", documento)
                .getResultList();
    } catch (Exception e) {
    e.printStackTrace();
    }
    return null;
}
    
    
}
