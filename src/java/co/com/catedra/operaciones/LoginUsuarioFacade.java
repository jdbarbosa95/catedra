/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.catedra.operaciones;

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
public class LoginUsuarioFacade {

   @PersistenceContext(unitName = "ProyectoPU")
   EntityManager em;
   
   public Usuario buscarUsuario(String usuario,String pass){
       try {
           return  em.createNamedQuery("Usuario.login",Usuario.class).setParameter("usuario", usuario).setParameter("pass", pass).getSingleResult();
       } catch (Exception e) {
           e.printStackTrace();
       }
   return  null;
   }
  
   public List<Usuario> mostrarDatosLogeoUsuario(String usuario){
     try {
           return  em.createNamedQuery("Usuario.datosUsuarioLogeo",Usuario.class).setParameter("usuario", usuario).getResultList();
       } catch (Exception e) {
           e.printStackTrace();
       }
   return  null;
   
   
   }
   
   
   
}
