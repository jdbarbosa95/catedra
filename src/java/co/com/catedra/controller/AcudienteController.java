package co.com.catedra.controller;

import co.com.catedra.eo.Acudiente;
import co.com.catedra.controller.util.JsfUtil;
import co.com.catedra.controller.util.PaginationHelper;
import co.com.catedra.eo.Rol;
import co.com.catedra.eo.Usuario;
import co.com.catedra.operaciones.AcudienteFacade;
import co.com.catedra.operaciones.EnvioCorreosFacade;
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

@ManagedBean(name = "acudienteController")
@SessionScoped
public class AcudienteController implements Serializable {
       
    
    //------
     private Usuario NewUsuario = new Usuario();
    private int UsuDocumento;
    private String UsuNombres;
    private String UsuApellidos;
    private int UsuTelefono;
    private String UsuCorreo;
    private String UsuNombreUsuario;
    private String usupassword;
    private Rol usurol;
    //------
    private Acudiente NewAcudiente = new Acudiente();
    
    //------
    private Acudiente current;
    private DataModel items = null;
    @EJB
    private co.com.catedra.operaciones.AcudienteFacade ejbFacade;
    @EJB
    private UsuarioFacade uf;
    @EJB
    private EnvioCorreosFacade emailFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public AcudienteController() {
    }

    public Acudiente getSelected() {
        if (current == null) {
            current = new Acudiente();
            selectedItemIndex = -1;
        }
        return current;
    }

    private AcudienteFacade getFacade() {
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
        current = (Acudiente) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Acudiente();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("AcudienteCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Acudiente) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("AcudienteUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Acudiente) getItems().getRowData();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("AcudienteDeleted"));
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
    
      public List<Acudiente> getObtenerUsuario(){
       
    return ejbFacade.usuariosAcu();
      
   }
     public void crearAcudiente(){
        try {
            NewUsuario.setDocumento(UsuDocumento);
           NewUsuario.setNombres(UsuNombres);
           NewUsuario.setApellidos(UsuApellidos);
           NewUsuario.setTelefono(UsuTelefono);
           NewUsuario.setCorreo(UsuCorreo);
           NewUsuario.setNombreUsuario(UsuNombreUsuario);
           NewUsuario.setPassword2(usupassword=uf.getCadenaAlfanumAleatoria(4));
           NewUsuario.setRolidRol(usurol=uf.ObtenerRol(2));           
           
           getUf().create(NewUsuario);
           NewAcudiente.setUsuarioidUsuario(NewUsuario);
           getFacade().create(NewAcudiente);
          
            emailFacade.SEND(UsuCorreo, UsuNombreUsuario, usupassword, usurol.toString());
          
         FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Acudiente ha sido creado"));
            
        } catch (Exception e) {
        e.printStackTrace();
        }
        
        
       
    }
     
    
    public List<Acudiente> obtenerAcudientes(){
        
      return ejbFacade.listaAcudiente();
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

    public Acudiente getNewAcudiente() {
        return NewAcudiente;
    }

    public void setNewAcudiente(Acudiente NewAcudiente) {
        this.NewAcudiente = NewAcudiente;
    }

    public UsuarioFacade getUf() {
        return uf;
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
    
    

    @FacesConverter(forClass = Acudiente.class)
    public static class AcudienteControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            AcudienteController controller = (AcudienteController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "acudienteController");
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
            if (object instanceof Acudiente) {
                Acudiente o = (Acudiente) object;
                return getStringKey(o.getIdAcudiente());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Acudiente.class.getName());
            }
        }

    }

}
