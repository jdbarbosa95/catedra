package co.com.catedra.controller;

import co.com.catedra.controller.dto.ObservacionDto;
import co.com.catedra.eo.Observador;
import co.com.catedra.controller.util.JsfUtil;
import co.com.catedra.controller.util.PaginationHelper;
import co.com.catedra.eo.Estudiante;
import co.com.catedra.eo.Observacion;
import co.com.catedra.eo.Usuario;
import co.com.catedra.operaciones.ObservacionFacade;
import co.com.catedra.operaciones.ObservadorFacade;

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

@ManagedBean(name = "observadorController")
@SessionScoped
public class ObservadorController implements Serializable {

    @ManagedProperty(value = "#{EstudianteController}")
    private EstudianteController estudianteController;

    @ManagedProperty(value = "#{LoginController}")
    private LoginUsuarioController luc;

    private Estudiante estudiante;
    private Usuario selecionarStudent;
    private List<Observador> lista;
    private List<Observacion> listaObservacion;
    ///----------
private  Observador observador;
    private List<Observador> listaObservador;
    private List<ObservacionDto> listaEstObs;
    //---------------------

    private Observador current;
    private DataModel items = null;
    @EJB
    private co.com.catedra.operaciones.ObservadorFacade ejbFacade;
    @EJB
    private ObservacionFacade of;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public ObservadorController() {
    }

    public Observador getSelected() {
        if (current == null) {
            current = new Observador();
            selectedItemIndex = -1;
        }
        return current;
    }

    private ObservadorFacade getFacade() {
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

    public List<Observacion> obtenerObservador() {
      

        observador= ejbFacade.ObservadorEstudiante(luc.getEstudiante());
 return    ejbFacade.buscarObservadorEstudiante(observador);
      
//    listaObservador=ejbFacade.buscarObservadorEstudiante(estudianteController.getEstudianteSelecionado());
//    listaObservacion=of.busacarObservaciones(estudianteController.getEstudianteSelecionado());
//
//        listaEstObs = new ArrayList();
//        for (Observacion ob : listaObservacion) {
//            ObservacionDto o = new ObservacionDto();
//            o.setEstudiante(estudianteController.getEstudianteSelecionado());
//            o.setObservador();
//            o.setObservacion(ob);
//            listaEstObs.add(o);
//        }
    }

    //
    public String prepareList() {
        recreateModel();
        return "List";
    }

    public String prepareView() {
        current = (Observador) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Observador();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ObservadorCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Observador) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ObservadorUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Observador) getItems().getRowData();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ObservadorDeleted"));
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

    public Usuario getSelecionarStudent() {
        return selecionarStudent;
    }

    public void setSelecionarStudent(Usuario selecionarStudent) {
        this.selecionarStudent = selecionarStudent;
    }

    public List<Observador> getLista() {
        return lista;
    }

    public void setLista(List<Observador> lista) {
        this.lista = lista;
    }

    public List<ObservacionDto> getListaEstObs() {
        return listaEstObs;
    }

    public void setListaEstObs(List<ObservacionDto> listaEstObs) {
        this.listaEstObs = listaEstObs;
    }

    public void setEstudianteController(EstudianteController estudianteController) {
        this.estudianteController = estudianteController;
    }

    public List<Observador> getListaObservador() {
        return listaObservador;
    }

    public void setListaObservador(List<Observador> listaObservador) {
        this.listaObservador = listaObservador;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    public void setLuc(LoginUsuarioController luc) {
        this.luc = luc;
    }

    @FacesConverter(forClass = Observador.class)
    public static class ObservadorControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ObservadorController controller = (ObservadorController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "observadorController");
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
            if (object instanceof Observador) {
                Observador o = (Observador) object;
                return getStringKey(o.getIdObservador());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Observador.class.getName());
            }
        }

    }

}
