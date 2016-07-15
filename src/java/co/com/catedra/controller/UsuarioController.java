package co.com.catedra.controller;

import co.com.catedra.eo.Usuario;
import co.com.catedra.controller.util.JsfUtil;
import co.com.catedra.controller.util.PaginationHelper;
import co.com.catedra.eo.Asignatura;
import co.com.catedra.eo.Curso;
import co.com.catedra.eo.Periodo;
import co.com.catedra.eo.Rol;
import co.com.catedra.operaciones.UsuarioFacade;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
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

@ManagedBean(name = "usuarioController")
@SessionScoped
public class UsuarioController implements Serializable {

    @ManagedProperty(value = "#{loginUsuarioController}")
    private LoginUsuarioController luc;

    ////--------------------
    private Rol rolSelecionado;
    private List<Usuario> listaUsuarios;
    private List<Asignatura> listaAsignaturas;
    private Periodo periodoseleccionado;
    private Asignatura asignaturaSelecionada;

    private Curso cursoSelecUsuario;
    private Periodo periodoSelecionado;
    //------------------

    private Usuario usuarioSelec;
    private Usuario current;
    private DataModel items = null;

    @EJB
    private co.com.catedra.operaciones.AsignaturaFacade asignaturaFacade;

    @EJB
    private co.com.catedra.operaciones.UsuarioFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public UsuarioController() {
    }

    public Usuario getSelected() {
        if (current == null) {
            current = new Usuario();
            selectedItemIndex = -1;
        }
        return current;
    }

    private UsuarioFacade getFacade() {
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
//

    public void obtenerIdRol() {
        listaUsuarios = ejbFacade.listarUsuariosConIdRol(rolSelecionado);
    }

    ///
    public void obtenerIdCursoProfesor() {
        if (cursoSelecUsuario == null) {
            System.out.println(" no se captura id curso");
        }
        listaAsignaturas = asignaturaFacade.buscarMisAsignaturasProfesorCurso(luc.getUsuario(), cursoSelecUsuario);
    }

    // 
    ///
    public String prepareView() {
        current = (Usuario) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Usuario();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("UsuarioCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Usuario) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("UsuarioUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Usuario) getItems().getRowData();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("UsuarioDeleted"));
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

    public List<Usuario> getItemsAvailableSelectOnelistaEstuAcudiente() {
        return ejbFacade.listarEstudiantesAcudiente(luc.getAcudiente());
    }
    
    public Rol getRolSelecionado() {
        return rolSelecionado;
    }

    public void setRolSelecionado(Rol rolSelecionado) {
        this.rolSelecionado = rolSelecionado;
    }

    public List<Usuario> getListaUsuarios() {
        return listaUsuarios;
    }

    public void setListaUsuarios(List<Usuario> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
    }

    public Usuario getUsuarioSelec() {
        return usuarioSelec;
    }

    public void setUsuarioSelec(Usuario usuarioSelec) {
        this.usuarioSelec = usuarioSelec;
    }

    public List<Asignatura> getListaAsignaturas() {
        return listaAsignaturas;
    }
    

    public void setListaAsignaturas(List<Asignatura> listaAsignaturas) {
        this.listaAsignaturas = listaAsignaturas;
    }

    public Asignatura getAsignaturaSelecionada() {
        return asignaturaSelecionada;
    }

    public void setAsignaturaSelecionada(Asignatura asignaturaSelecionada) {
        this.asignaturaSelecionada = asignaturaSelecionada;
    }

    public void setLuc(LoginUsuarioController luc) {
        this.luc = luc;
    }

    public Curso getCursoSelecUsuario() {
        return cursoSelecUsuario;
    }

    public void setCursoSelecUsuario(Curso cursoSelecUsuario) {
        this.cursoSelecUsuario = cursoSelecUsuario;
    }

    public Periodo getPeriodoSelecionado() {
        return periodoSelecionado;
    }

    public void setPeriodoSelecionado(Periodo periodoSelecionado) {
        this.periodoSelecionado = periodoSelecionado;
    }

    public Periodo getPeriodoseleccionado() {
        return periodoseleccionado;
    }

    public void setPeriodoseleccionado(Periodo periodoseleccionado) {
        this.periodoseleccionado = periodoseleccionado;
    }

    @FacesConverter(forClass = Usuario.class)
    public static class UsuarioControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            UsuarioController controller = (UsuarioController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "usuarioController");
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
            if (object instanceof Usuario) {
                Usuario o = (Usuario) object;
                return getStringKey(o.getIdUsuario());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Usuario.class.getName());
            }
        }

    }

}
