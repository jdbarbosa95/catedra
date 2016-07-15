package co.com.catedra.controller;

import co.com.catedra.eo.Profesor;
import co.com.catedra.controller.util.JsfUtil;
import co.com.catedra.controller.util.PaginationHelper;
import co.com.catedra.eo.Asignatura;
import co.com.catedra.eo.Curso;
import co.com.catedra.eo.Rol;
import co.com.catedra.eo.Usuario;
import co.com.catedra.operaciones.EnvioCorreosFacade;
import co.com.catedra.operaciones.ProfesorFacade;
import co.com.catedra.operaciones.UsuarioFacade;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

@ManagedBean(name = "profesorController")
@SessionScoped
public class ProfesorController implements Serializable {
      
    
    private Asignatura asignaturaSele;
    private Curso CursoSele;
    //------
     private Usuario NewUsuario = new Usuario();
    private int UsuDocumento;
    private String UsuNombres;
    private String UsuApellidos;
    private int UsuTelefono;
    private String UsuCorreo;
    private String UsuNombreUsuario;
    private String UsuPassword;
    private Rol UsuRol;
    
    //------
    private Profesor NewProfesor = new Profesor();
    
            
    //------
    private Profesor profesor;
    /**/
    private Profesor current;
    private DataModel items = null;
    @EJB
    private co.com.catedra.operaciones.ProfesorFacade ejbFacade;
    @EJB
    private UsuarioFacade uf;
    @EJB
    private EnvioCorreosFacade emailFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public ProfesorController() {
    }

    public Profesor getSelected() {
        if (current == null) {
            current = new Profesor();
            selectedItemIndex = -1;
        }
        return current;
    }

    private ProfesorFacade getFacade() {
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
        current = (Profesor) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Profesor();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ProfesorCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Profesor) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ProfesorUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Profesor) getItems().getRowData();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ProfesorDeleted"));
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

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }
    //******
    public List<Profesor> getObtenerProfesor(){
        
     return ejbFacade.usuariosProfesor();
        
    }
    
    ////
    public void crearprofesor(){
        try {
            NewUsuario.setDocumento(UsuDocumento);
           NewUsuario.setNombres(UsuNombres);
           NewUsuario.setApellidos(UsuApellidos);
           NewUsuario.setTelefono(UsuTelefono);
           NewUsuario.setCorreo(UsuCorreo);
           NewUsuario.setNombreUsuario(UsuNombreUsuario);
           NewUsuario.setPassword2(UsuPassword=uf.getCadenaAlfanumAleatoria(4));
           NewUsuario.setRolidRol(UsuRol=uf.ObtenerRol(2));           
           
           getUf().create(NewUsuario);
           NewProfesor.setUsuarioidUsuario(NewUsuario);
           getFacade().create(NewProfesor);
          
            emailFacade.SEND(UsuCorreo, UsuNombreUsuario, UsuPassword, UsuRol.toString());
          
         FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Profesor ha sido creado"));
            
        } catch (Exception e) {
        e.printStackTrace();
        }
        
        
       
    }

    
    
    public Asignatura getAsignaturaSele() {
        return asignaturaSele;
    }

    public void setAsignaturaSele(Asignatura asignaturaSele) {
        this.asignaturaSele = asignaturaSele;
        
    }

    public Curso getCursoSele() {
        return CursoSele;
    }

    public void setCursoSele(Curso CursoSele) {
        this.CursoSele = CursoSele;
    }

    public Usuario getNewUsuario() {
        return NewUsuario;
    }

    public void setNewUsuario(Usuario NewUsuario) {
        this.NewUsuario = NewUsuario;
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


    public Profesor getNewProfesor() {
        return NewProfesor;
    }

    public void setNewProfesor(Profesor NewProfesor) {
        this.NewProfesor = NewProfesor;
    }

    public UsuarioFacade getUf() {
        return uf;
    }

    public String getUsuPassword() {
        return UsuPassword;
    }

    public void setUsuPassword(String UsuPassword) {
        this.UsuPassword = UsuPassword;
    }

    public Rol getUsuRol() {
        return UsuRol;
    }

    public void setUsuRol(Rol UsuRol) {
        this.UsuRol = UsuRol;
    }

    public EnvioCorreosFacade getEmailFacade() {
        return emailFacade;
    }

    public Profesor getProfesor() {
        return profesor;
    }

    public void setProfesor(Profesor profesor) {
        this.profesor = profesor;
    }
    
    

    @FacesConverter(forClass = Profesor.class)
    public static class ProfesorControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ProfesorController controller = (ProfesorController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "profesorController");
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
            if (object instanceof Profesor) {
                Profesor o = (Profesor) object;
                return getStringKey(o.getIdProfesor());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Profesor.class.getName());
            }
        }

    }

}
