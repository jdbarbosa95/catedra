/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.catedra.operaciones;

import co.com.catedra.eo.Acudiente;
import co.com.catedra.eo.Rol;
import co.com.catedra.eo.Usuario;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Sneider
 */
@Stateless
public class UsuarioFacade extends AbstractFacade<Usuario> {
    @PersistenceContext(unitName = "ProyectoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UsuarioFacade() {
        super(Usuario.class);
    }
    /////
    
   public List<Usuario> listarUsuariosConIdRol(Rol rol){
    
        try {
            return em.createQuery("SELECT u FROM Usuario u,Rol r WHERE u.rolidRol.idRol=r.idRol AND r.idRol=:idRol",Usuario.class)
                    .setParameter("idRol", rol.getIdRol())
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
    return null;
    }
    
public String getCadenaAlfanumAleatoria (int longitud){
String cadenaAleatoria = "";
long milis = new java.util.GregorianCalendar().getTimeInMillis();
Random r = new Random(milis);
int i = 0;
while ( i < longitud){
char c = (char)r.nextInt(255);
if ( (c >= '0' && c <='9') || (c >='A' && c <='Z') ){
cadenaAleatoria += c;
i ++;
}
}
return cadenaAleatoria;
}
public Rol ObtenerRol(int idrol){
    
    try {
        return em.createQuery("SELECT r FROM Rol r WHERE r.idRol=:idrol",Rol.class)
                .setParameter("idrol",idrol)
                .getSingleResult();
    } catch (Exception e) {
    e.printStackTrace();
    }
    return null;
}
 public List<Usuario> listarEstudiantesAcudiente(Acudiente acudiente){
   
     //SELECT usuario.Nombres,usuario.Rol_idRol,usuario.Apellidos,estudiante.Acudiente_idAcudiente,estudiante.Curso_idCurso
           //FROM usuario INNER JOIN estudiante 
                                                                                                                                    //ON usuario.idUsuario = estudiante.Usuario_idUsuario 
                                                                                                                             //INNER JOIN acudiente ON estudiante.Acudiente_idAcudiente = acudiente.idAcudiente WHERE acudiente.Usuario_idUsuario=6                                                                                                                                 //INNER JOIN acudiente ON estudiante.Acudiente_idAcudiente = acudiente.idAcudiente
        try {
            List<Usuario> lista = new ArrayList();
            List<Usuario> listaQ = new ArrayList();
            
              listaQ= em.createQuery("SELECT u FROM Usuario u, Estudiante e,Acudiente a WHERE u.idUsuario=e.usuarioidUsuario.idUsuario AND a.idAcudiente=e.acudienteidAcudiente.idAcudiente AND a.idAcudiente=:idUsuario",Usuario.class)
                    .setParameter("idUsuario", acudiente.getIdAcudiente())
                    .getResultList();
              lista.add(new Usuario(-1));
              lista.addAll(listaQ);
              return lista;
          
        } catch (Exception e) {
            e.printStackTrace();
        }
    return null;
    }
}