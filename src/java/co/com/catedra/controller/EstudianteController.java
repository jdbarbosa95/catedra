package co.com.catedra.controller;

import co.com.catedra.controller.dto.AsistenciaDto;
import co.com.catedra.controller.dto.NotaDto;
import co.com.catedra.eo.Estudiante;
import co.com.catedra.controller.util.JsfUtil;
import co.com.catedra.controller.util.PaginationHelper;
import co.com.catedra.eo.Acudiente;
import co.com.catedra.eo.Asignatura;
import co.com.catedra.eo.Clase;
import co.com.catedra.eo.Curso;
import co.com.catedra.eo.Nota;
import co.com.catedra.eo.Observador;
import co.com.catedra.eo.Regitroasistencia;
import co.com.catedra.eo.Rol;
import co.com.catedra.eo.Usuario;
import co.com.catedra.operaciones.EnvioCorreosFacade;
import co.com.catedra.operaciones.EstudianteFacade;
import co.com.catedra.operaciones.ObservadorFacade;
import co.com.catedra.operaciones.UsuarioFacade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

@ManagedBean(name = "estudianteController")
@SessionScoped
public class EstudianteController implements Serializable {

    @ManagedProperty(value = "#{loginUsuarioController}")
    private LoginUsuarioController luc;

    @ManagedProperty(value = "#{usuarioController}")
    private UsuarioController uc;

    @ManagedProperty(value = "#{asignaturaController}")
    private AsignaturaController ac;
    

    //---------------------------------------
    private Estudiante estudianteSelecionado;
    //--------------------
    private Asignatura asignaturaEs;
    //------------------------------
    private Usuario NewUsuario = new Usuario();
    private Estudiante NewEstudiante = new Estudiante();
    private  Observador NewObservador = new Observador();
    //-------------------------------
    private List<Estudiante> listausuest;
    private List<Regitroasistencia> listasistencia;
    private List<Estudiante> lista;
    private List<NotaDto> listaEstNota;
    private Curso curso;
    private List<AsistenciaDto> listaAsis;
    private Clase clase;
    //---------------
    private int UsuDocumento;
    private String UsuNombres;
    private String UsuApellidos;
    private int UsuTelefono;
    private String UsuCorreo;
    private String UsuNombreUsuario;
    private String usupassword;
    private Rol usurol;
    
    //-----
   
    private Acudiente EstAcudiente;
    private Curso EstCurso;
    ///
    private int documentoajax;

//------------------------------------------
    private Estudiante current;
    private DataModel items = null;
    @EJB
    private co.com.catedra.operaciones.EstudianteFacade ejbFacade;
    @EJB
    private UsuarioFacade uf;
    @EJB
    private ObservadorFacade of;
    @EJB
    private EnvioCorreosFacade emailFacade;
    
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public EstudianteController() {
    }

    public Estudiante getSelected() {
        if (current == null) {
            current = new Estudiante();
            selectedItemIndex = -1;
        }
        return current;
    }

    private EstudianteFacade getFacade() {
        return ejbFacade;
    }
    

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}));
                }
            };
        }
        return pagination;
    }

    public String prepareList() {
        recreateModel();
        return "List";
    }

    public String prepareView() {
        current = (Estudiante) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Estudiante();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("EstudianteCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Estudiante) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("EstudianteUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Estudiante) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }

    public String destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (selectedItemIndex >= 0) {
            return "View";
        } else {
            // all items were removed - go back to list
            recreateModel();
            return "List";
        }
    }

    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("EstudianteDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    private void updateCurrentItem() {
        int count = getFacade().count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getFacade().findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0);
        }
    }

    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }

    private void recreateModel() {
        items = null;
    }

    private void recreatePagination() {
        pagination = null;
    }

    public String next() {
        getPagination().nextPage();
        recreateModel();
        return "List";
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "List";
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemIsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }
    
  //***/
    public List<Estudiante> getItemIsAvailableSelectOneEstPorCurso(){
        
   return ejbFacade.ListaEstudiatePorCorso(uc.getCursoSelecUsuario());
    
    }
    //***/
//    public void consultarAsistencia(){
//        lista = ejbFacade.ListaEstudiatePorCorso(curso);
//        listaAsis = new ArrayList();
//        for (Estudiante e : lista) {
//             AsistenciaDto a = new AsistenciaDto();
//             a.getEstudiante();
//             a.getRegitroAsistensia();   
//            
//        }
//        
//    }
   //***/
    public void obtenerEstudiantesPorCurso(){
        
        lista = ejbFacade.ListaEstudiatePorCorso(curso);
        listaAsis = new ArrayList();
        for (Estudiante e : lista) {
            AsistenciaDto a = new AsistenciaDto();
            a.setEstudiante(e);
            a.setClase(clase);
            a.setRegitroAsistensia(new Regitroasistencia());
            listaAsis.add(a);
            
            
            
            
        }
    }
    
    ///****/
    public void mostrarAsistencia(){
     listasistencia =  ejbFacade.MostrarAsistencia(curso);
    }
    
    
    //***/
   public List<Estudiante> getObtenerUsuario(){
       
    return ejbFacade.usuariosEsstudiante();
      
   }
   //***/
   public List<Estudiante> EstPorDocumento(){
       
     return  ejbFacade.buscardocumento(documentoajax);
       
   }
    

    //***/
    public void obtenerEstudiantes() {

        /*if (ac.getAsignaturaSelecDocente() == null) {
            System.out.println("no se  captura id asignatura");
        } else if (uc.getCursoSelecUsuario() == null) {
            System.out.println("no se  captura id curso");
        }*/
        lista = ejbFacade.listarEstudiantesP(luc.getUsuario(),uc.getCursoSelecUsuario(), ac.getAsignaturaSelecDocente());
        listaEstNota= new ArrayList();
        for (Estudiante e : lista) {
            NotaDto n = new NotaDto();
            n.setEstudiante(e);
            n.setNota(new Nota());
            listaEstNota.add(n);
            
        }
    }
    
    
    //***/
    public void crearEstudiante(){
        try {
            NewUsuario.setDocumento(UsuDocumento);
           NewUsuario.setNombres(UsuNombres);
           NewUsuario.setApellidos(UsuApellidos);
           NewUsuario.setTelefono(UsuTelefono);
           NewUsuario.setCorreo(UsuCorreo);
           NewUsuario.setNombreUsuario(UsuNombreUsuario);
          NewUsuario.setPassword2(usupassword=uf.getCadenaAlfanumAleatoria(4));
          NewUsuario.setRolidRol(usurol=uf.ObtenerRol(1));           
           
           getUf().create(NewUsuario);
           NewEstudiante.setUsuarioidUsuario(NewUsuario);
           NewEstudiante.setAcudienteidAcudiente(EstAcudiente);
           NewEstudiante.setCursoidCurso(EstCurso);
            getFacade().create(NewEstudiante);
            NewObservador.setEstudianteidEstudiante(getNewEstudiante());
            getOf().create(NewObservador);
            
            emailFacade.SEND(UsuCorreo, UsuNombreUsuario, usupassword, usurol.toString());
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Estudiante ha sido creado"));
          
        } catch (Exception e) {
        e.printStackTrace();
        }
        
        
       
    }
    
    

    public void setLuc(LoginUsuarioController luc) {
        this.luc = luc;
    }

    public List<Estudiante> getLista() {
        return lista;
    }

    public void setLista(List<Estudiante> lista) {
        this.lista = lista;
    }

    public Asignatura getAsignaturaEs() {
        return asignaturaEs;
    }

    public void setAsignaturaEs(Asignatura asignaturaEs) {
        this.asignaturaEs = asignaturaEs;
    }

    public Estudiante getEstudianteSelecionado() {
        return estudianteSelecionado;
    }

    public void setEstudianteSelecionado(Estudiante estudianteSelecionado) {
        this.estudianteSelecionado = estudianteSelecionado;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public void setUc(UsuarioController uc) {
        this.uc = uc;
    }

    public void setAc(AsignaturaController ac) {
        this.ac = ac;
    }
    

    public List<NotaDto> getListaEstNota() {
        return listaEstNota;
    }

    public void setListaEstNota(List<NotaDto> listaEstNota) {
        this.listaEstNota = listaEstNota;
    }

    public Usuario getNewUsuario() {
        return NewUsuario;
    }

    public void setNewUsuario(Usuario NewUsuario) {
        this.NewUsuario = NewUsuario;
    }
    

    public Estudiante getNewEstudiante() {
        return NewEstudiante;
    }

    public void setNewEstudiante(Estudiante NewEstudiante) {
        this.NewEstudiante = NewEstudiante;
    }

    public int getUsuDocumento() {
        return UsuDocumento;
    }

    public void setUsuDocumento(int UsuDocumento) {
        this.UsuDocumento = UsuDocumento;
    }

    public String getUsuNombres() {
        return UsuNombres;
    }

    public void setUsuNombres(String UsuNombres) {
        this.UsuNombres = UsuNombres;
    }

    public String getUsuApellidos() {
        return UsuApellidos;
    }

    public void setUsuApellidos(String UsuApellidos) {
        this.UsuApellidos = UsuApellidos;
    }

    public int getUsuTelefono() {
        return UsuTelefono;
    }

    public void setUsuTelefono(int UsuTelefono) {
        this.UsuTelefono = UsuTelefono;
    }

    public String getUsuCorreo() {
        return UsuCorreo;
    }

    public void setUsuCorreo(String UsuCorreo) {
        this.UsuCorreo = UsuCorreo;
    }

    public String getUsuNombreUsuario() {
        return UsuNombreUsuario;
    }

    public void setUsuNombreUsuario(String UsuNombreUsuario) {
        this.UsuNombreUsuario = UsuNombreUsuario;
    }

    
    public Acudiente getEstAcudiente() {
        return EstAcudiente;
    }

    public void setEstAcudiente(Acudiente EstAcudiente) {
        this.EstAcudiente = EstAcudiente;
    }

    public Curso getEstCurso() {
        return EstCurso;
    }

    public void setEstCurso(Curso EstCurso) {
        this.EstCurso = EstCurso;
    }
    

    public UsuarioFacade getUf() {
        return uf;
    }

    

    public ObservadorFacade getOf() {
        return of;
    }

   

    public Observador getNewObservador() {
        return NewObservador;
    }

    public void setNewObservador(Observador NewObservador) {
        this.NewObservador = NewObservador;
    }

    public List<Estudiante> getListausuest() {
        return listausuest;
    }

    public void setListausuest(List<Estudiante> listausuest) {
        this.listausuest = listausuest;
    }

    public int getDocumentoajax() {
        return documentoajax;
    }

    public void setDocumentoajax(int documentoajax) {
        this.documentoajax = documentoajax;
    }

    public String getUsupassword() {
        return usupassword;
    }

    public void setUsupassword(String usupassword) {
        this.usupassword = usupassword;
    }

    public Rol getUsurol() {
        return usurol;
    }

    public void setUsurol(Rol usurol) {
        this.usurol = usurol;
    }

    public EnvioCorreosFacade getEmailFacade() {
        return emailFacade;
    }

    public List<AsistenciaDto> getListaAsis() {
        return listaAsis;
    }

    public void setListaAsis(List<AsistenciaDto> listaAsis) {
        this.listaAsis = listaAsis;
    }

    public Clase getClase() {
        return clase;
    }

    public void setClase(Clase clase) {
        this.clase = clase;
    }

    public List<Regitroasistencia> getListasistencia() {
        return listasistencia;
    }

    public void setListasistencia(List<Regitroasistencia> listasistencia) {
        this.listasistencia = listasistencia;
    }

    


    @FacesConverter(forClass = Estudiante.class)
    public static class EstudianteControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            EstudianteController controller = (EstudianteController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "estudianteController");
            return controller.ejbFacade.find(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Estudiante) {
                Estudiante o = (Estudiante) object;
                return getStringKey(o.getIdEstudiante());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Estudiante.class.getName());
            }
        }

    }

}
