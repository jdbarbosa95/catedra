package co.com.catedra.controller;

import co.com.catedra.controller.dto.ObservacionDto;
import co.com.catedra.eo.Observacion;
import co.com.catedra.controller.util.JsfUtil;
import co.com.catedra.controller.util.PaginationHelper;
import co.com.catedra.operaciones.ObservacionFacade;
import co.com.catedra.operaciones.ObservadorFacade;

import java.io.Serializable;
import java.util.ArrayList;
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

@ManagedBean(name = "observacionController")
@SessionScoped
public class ObservacionController implements Serializable {
    
    @ManagedProperty(value = "ObservadorController")
    private ObservadorController oc;
    @ManagedProperty(value = "EstudianteController")
    private EstudianteController ec;
    
    
//------
    private Observacion Newobservacion;
    private String observacion;
    private String descargos;
    
 
    //----
    private Observacion current;
    private DataModel items = null;
    @EJB
    private co.com.catedra.operaciones.ObservacionFacade ejbFacade;
    @EJB
    private ObservadorFacade of;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public ObservacionController() {
    }

    public Observacion getSelected() {
        if (current == null) {
            current = new Observacion();
            selectedItemIndex = -1;
        }
        return current;
    }

    private ObservacionFacade getFacade() {
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
        current = (Observacion) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Observacion();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ObservacionCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }
    
    public void crearObservacionEstudiante(){
        for (ObservacionDto observacionDto : oc.getListaEstObs()){
            observacionDto.getObservacion().setObservadoridObservador(observacionDto.getObservador());
            getFacade().create(observacionDto.getObservacion());
                       
        }
        oc.setListaEstObs(new ArrayList());
        
    }
    
    

    public String prepareEdit() {
        current = (Observacion) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ObservacionUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Observacion) getItems().getRowData();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ObservacionDeleted"));
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
    
    /////******////////
    public void crearObservacion(){
        Newobservacion.setObservadoridObservador(of.ObservadorEstudiante(oc.getEstudiante()));
        Newobservacion.setObservacion(observacion);
        Newobservacion.setDescargos(descargos);
        getFacade().create(Newobservacion);
        
        
        
        
        
    }
    
    

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getDescargos() {
        return descargos;
    }

    public void setDescargos(String descargos) {
        this.descargos = descargos;
    }

    public void setOc(ObservadorController oc) {
        this.oc = oc;
    }

    public void setEc(EstudianteController ec) {
        this.ec = ec;
    }

    @FacesConverter(forClass = Observacion.class)
    public static class ObservacionControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ObservacionController controller = (ObservacionController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "observacionController");
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
            if (object instanceof Observacion) {
                Observacion o = (Observacion) object;
                return getStringKey(o.getIdObservacion());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Observacion.class.getName());
            }
        }

    }

}
