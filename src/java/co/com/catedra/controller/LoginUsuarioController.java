/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.catedra.controller;

import co.com.catedra.eo.Acudiente;
import co.com.catedra.eo.Curso;
import co.com.catedra.eo.Estudiante;
import co.com.catedra.eo.Profesor;
import co.com.catedra.eo.Usuario;
import co.com.catedra.operaciones.AcudienteFacade;
import co.com.catedra.operaciones.EstudianteFacade;
import co.com.catedra.operaciones.LoginUsuarioFacade;
import co.com.catedra.operaciones.ProfesorFacade;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Sneider
 */
@ManagedBean(name = "loginUsuarioController")
@SessionScoped
public class LoginUsuarioController implements Serializable{
//----------------
    private String nombreUsuario;
    private String pass;
    private Profesor profesor;
    private Estudiante estudiante;
    private Acudiente acudiente;
    private  Curso curso;
    
    ///-----------------

    private Usuario usuario;
    private List<Usuario> listaUsuario;
    //-------------------------------------
    
    
    @EJB
    private EstudianteFacade ef;
    @EJB
    private ProfesorFacade pf;
    @EJB
    private LoginUsuarioFacade luf;
    @EJB
    private AcudienteFacade af;

    public LoginUsuarioController() {
    }
    
    public  String cerrarSesion(){
        FacesContext context = FacesContext.getCurrentInstance();

ExternalContext externalContext = context.getExternalContext();

Object session = externalContext.getSession(false);

HttpSession httpSession = (HttpSession) session;

httpSession.invalidate();
     return "faces/index";
    }

    public String usaurioEncontrado() {

        usuario = luf.buscarUsuario(nombreUsuario, pass);

        if (usuario != null) {

            if (usuario.getRolidRol().getIdRol() == 1) {
                listaUsuario = luf.mostrarDatosLogeoUsuario(nombreUsuario);
                estudiante = ef.buscarEstudiante(usuario);
                return "/EstudianteViews/PerfilEstudiante";
                
            } else if (usuario.getRolidRol().getIdRol() == 2) {

                listaUsuario = luf.mostrarDatosLogeoUsuario(nombreUsuario);
                
                profesor = pf.buscarProfesor(usuario);
                return "/ProfesorViews/PerfilProfesor";
                
            } else if (usuario.getRolidRol().getIdRol() == 3) {
                listaUsuario = luf.mostrarDatosLogeoUsuario(nombreUsuario);
               
                return "/AcudienteViews/PerfilAcudiente";
            } else if (usuario.getRolidRol().getIdRol() == 4) {
                listaUsuario = luf.mostrarDatosLogeoUsuario(nombreUsuario);
                return "/AdministradorViews/PerfilAdministrador";
            }

        }
        //return "login";
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Usuario o contraseña incorrectos intente de nuevo"));
            
        return "login";

    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Usuario> getListaUsuario() {
        return listaUsuario;
    }

    public void setListaUsuario(List<Usuario> listaUsuario) {
        this.listaUsuario = listaUsuario;
    }

    public Profesor getProfesor() {
        return profesor;
    }

    public void setProfesor(Profesor profesor) {
        this.profesor = profesor;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public Acudiente getAcudiente() {
        return acudiente;
    }

    public void setAcudiente(Acudiente acudiente) {
        this.acudiente = acudiente;
    }

}
