package co.com.catedra.controller;

import co.com.catedra.eo.Periodo;
import co.com.catedra.controller.util.JsfUtil;
import co.com.catedra.controller.util.PaginationHelper;
import co.com.catedra.operaciones.PeriodoFacade;

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

@ManagedBean(name = "periodoController")
@SessionScoped
public class PeriodoController implements Serializable {
    
    @ManagedProperty(value = "#{loginUsuarioController}")
    private LoginUsuarioController luc;
    @ManagedProperty(value = "#{cursoController}")
    private CursoController cursoController;

    @ManagedProperty(value = "#{asignaturaController}")
    private AsignaturaController asignaturaController;

    @ManagedProperty(value = "#{usuarioController}")
    private UsuarioController usuarioController;

    private List<Periodo> listaP;
    private Periodo periodoSelec;
    
////_-----
    private Periodo current;
    private DataModel items = null;
    @EJB
    private co.com.catedra.operaciones.PeriodoFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public PeriodoController() {
    }

    public Periodo getSelected() {
        if (current == null) {
            current = new Periodo();
            selectedItemIndex = -1;
        }
        return current;
    }

    private PeriodoFacade getFacade() {
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
        current = (Periodo) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Periodo();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("PeriodoCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Periodo) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("PeriodoUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Periodo) getItems().getRowData();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("PeriodoDeleted"));
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
    public void periodoNotasEstDocentUpdate(){
     listaP = ejbFacade.listarPeriodoNotasEstuDocenteUpda(luc.getUsuario(),usuarioController.getCursoSelecUsuario(),asignaturaController.getAsignaturaSelecDocente());
     }
    public void periodoAsigEst() {

        listaP = ejbFacade.buscarPeriodosMateriaE(luc.getUsuario(), asignaturaController.getAsignaturaSelecDocente());
    }

    public List<Periodo> getListaP() {
        return listaP;
    }

    public void setListaP(List<Periodo> listaP) {
        this.listaP = listaP;
    }

    public Periodo getPeriodoSelec() {
        return periodoSelec;
    }

    public void setPeriodoSelec(Periodo periodoSelec) {
        this.periodoSelec = periodoSelec;
    }

    public void setLuc(LoginUsuarioController luc) {
        this.luc = luc;
    }

    public void setAsignaturaController(AsignaturaController asignaturaController) {
        this.asignaturaController = asignaturaController;
    }

    public void setUsuarioController(UsuarioController usuarioController) {
        this.usuarioController = usuarioController;
    }

    public void setCursoController(CursoController cursoController) {
        this.cursoController = cursoController;
    }
     
     

    @FacesConverter(forClass = Periodo.class)
    public static class PeriodoControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            PeriodoController controller = (PeriodoController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "periodoController");
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
            if (object instanceof Periodo) {
                Periodo o = (Periodo) object;
                return getStringKey(o.getIdPeriodo());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Periodo.class.getName());
            }
        }

    }

}
