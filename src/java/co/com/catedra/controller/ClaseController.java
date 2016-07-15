package co.com.catedra.controller;

import co.com.catedra.eo.Clase;
import co.com.catedra.controller.util.JsfUtil;
import co.com.catedra.controller.util.PaginationHelper;
import co.com.catedra.eo.Profesor;
import co.com.catedra.operaciones.ClaseFacade;
import co.com.catedra.operaciones.EnvioCorreosFacade;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;
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

@ManagedBean(name = "claseController")
@SessionScoped
public class ClaseController implements Serializable {

    @ManagedProperty(value = "#{loginUsuarioController}")
    private LoginUsuarioController luc;

    @ManagedProperty(value = "#{estudianteController}")
    private EstudianteController ec;

    @ManagedProperty(value = "#{profesorController}")
    private ProfesorController profesorController;

    @ManagedProperty(value = "#{asignaturaController}")
    private AsignaturaController asignaturaController;

    @ManagedProperty(value = "#{cursoController}")
    private CursoController cursoController;

    private List<Clase> listaclases;
    ///---
    private Clase NewClase;
    private String dia;
    private Time  hora;
    private List<Profesor> lista;

    //------
    private Clase current;
    private DataModel items = null;
    @EJB
    private co.com.catedra.operaciones.ClaseFacade ejbFacade;
    @EJB
    private EnvioCorreosFacade correosFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public ClaseController() {
    }

    public Clase getSelected() {
        if (current == null) {
            current = new Clase();
            selectedItemIndex = -1;
        }
        return current;
    }

    private ClaseFacade getFacade() {
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
        current = (Clase) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Clase();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ClaseCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Clase) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ClaseUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Clase) getItems().getRowData();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ClaseDeleted"));
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

    ////***+//
    public void crearClase() {

        NewClase.setHora(hora);
        NewClase.setDia(dia);
        NewClase.setProfesoridProfesor(profesorController.getProfesor());
        NewClase.setAsignaturaidAsignatura(asignaturaController.getAsignaturaSelecDocente());
        NewClase.setCursoidCurso(cursoController.getCursoSelec());
        ejbFacade.create(NewClase);
        correosFacade.SENDNewClass(profesorController.getSelected().getUsuarioidUsuario().getCorreo(), dia,cursoController.getCursoSelec(), hora, asignaturaController.getAsignaturaSelecDocente());

    }

    public void clasesPorCurso() {

        listaclases = ejbFacade.clasePorCurso(ec.getCurso(), luc.getProfesor());

    }

    public List<Profesor> obtenerProfesores() {

        lista = ejbFacade.ProfesorDisponible(dia, hora);
        return lista;
    }

    public Clase getNewClase() {
        return NewClase;
    }

    public void setNewClase(Clase NewClase) {
        this.NewClase = NewClase;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    
    public List<Profesor> getLista() {
        return lista;
    }

    public void setLista(List<Profesor> lista) {
        this.lista = lista;
    }

    public List<Clase> getListaclases() {
        return listaclases;
    }

    public void setListaclases(List<Clase> listaclases) {
        this.listaclases = listaclases;
    }

    public void setLuc(LoginUsuarioController luc) {
        this.luc = luc;
    }

    public void setEc(EstudianteController ec) {
        this.ec = ec;
    }

    public void setProfesorController(ProfesorController profesorController) {
        this.profesorController = profesorController;
    }

    public void setAsignaturaController(AsignaturaController asignaturaController) {
        this.asignaturaController = asignaturaController;
    }

    public void setCursoController(CursoController cursoController) {
        this.cursoController = cursoController;
    }

    public EnvioCorreosFacade getCorreosFacade() {
        return correosFacade;
    }

    public Time getHora() {
        return hora;
    }

    public void setHora(Time hora) {
        this.hora = hora;
    }

    @FacesConverter(forClass = Clase.class)
    public static class ClaseControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ClaseController controller = (ClaseController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "claseController");
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
            if (object instanceof Clase) {
                Clase o = (Clase) object;
                return getStringKey(o.getIdClase());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Clase.class.getName());
            }
        }

    }

}
