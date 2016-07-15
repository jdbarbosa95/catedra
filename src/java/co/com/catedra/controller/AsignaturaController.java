package co.com.catedra.controller;

import co.com.catedra.eo.Asignatura;
import co.com.catedra.controller.util.JsfUtil;
import co.com.catedra.controller.util.PaginationHelper;
import co.com.catedra.eo.Curso;
import co.com.catedra.operaciones.AsignaturaFacade;

import java.io.Serializable;
import java.util.ArrayList;
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

@ManagedBean(name = "asignaturaController")
@SessionScoped
public class AsignaturaController implements Serializable {

    @ManagedProperty(value = "#{usuarioController}")
    private UsuarioController usuarioController;
    

    @ManagedProperty(value = "#{loginUsuarioController}")
    private LoginUsuarioController luc;
    
    
   
    //private String cursoNombreSelec;
    private Curso cursoNombreSelec;

    private List<Asignatura> lista;
    private Asignatura materia;
    private Asignatura asignaturaSelecDocente;

    /////////////////////////////
    private Asignatura current;
    private DataModel items = null;
    @EJB
    private co.com.catedra.operaciones.AsignaturaFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public AsignaturaController() {
    }

    public Asignatura getSelected() {
        if (current == null) {
            current = new Asignatura();
            selectedItemIndex = -1;
        }
        return current;
    }

    /**
     *
     */
    public void obtenerIdCursoProfesor() {
        if (usuarioController.getCursoSelecUsuario() == null) {
            System.out.println(" no se captura id curso");
        }
      //  List<Asignatura> listaRet;

       // listaRet
         lista       = ejbFacade.buscarMisAsignaturasProfesorCurso(luc.getUsuario(), usuarioController.getCursoSelecUsuario());
      //  lista.clear();
      //  lista.add(new Asignatura(-1, "---"));
      //  lista.addAll(listaRet);

    }

    private AsignaturaFacade getFacade() {
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
        current = (Asignatura) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Asignatura();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("AsignaturaCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Asignatura) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("AsignaturaUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Asignatura) getItems().getRowData();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("AsignaturaDeleted"));
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

    public List<Asignatura> getItemsAvailableSelectOneMisAsignaturas() {
        return ejbFacade.buscarMisAsignaturas(luc.getProfesor());
    }

    public List<Asignatura> getItemsAvailableSelectOneMisAsignaturasEstudiante() {
        return ejbFacade.buscarMisAsignaturasEstudiante(luc.getEstudiante());
    }
    public void obtenerAsisgnaturasEstuAcudiente() {
        // lista = ejbFacade.buscarAsignaturasEstudianteAcudiente(usuarioController.getUsuarioSelec());
        lista = ejbFacade.buscarAsignaturasEstudianteAcudiente(usuarioController.getUsuarioSelec());
    }

    public void setLuc(LoginUsuarioController luc) {
        this.luc = luc;
    }

    public List<Asignatura> getLista() {
        return lista;
    }

    public void setLista(List<Asignatura> lista) {
        this.lista = lista;
    }

   

    public Curso getCursoNombreSelec() {
        return cursoNombreSelec;
    }

    public void setCursoNombreSelec(Curso cursoNombreSelec) {
        this.cursoNombreSelec = cursoNombreSelec;
    }

    public void setUsuarioController(UsuarioController usuarioController) {
        this.usuarioController = usuarioController;
    }

    public Asignatura getAsignaturaSelecDocente() {
        return asignaturaSelecDocente;
    }

    public void setAsignaturaSelecDocente(Asignatura asignaturaSelecDocente) {
        this.asignaturaSelecDocente = asignaturaSelecDocente;
    }

    public Asignatura getMateria() {
        return materia;
    }

    public void setMateria(Asignatura materia) {
        this.materia = materia;
    }

    @FacesConverter(forClass = Asignatura.class)
    public static class AsignaturaControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            AsignaturaController controller = (AsignaturaController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "asignaturaController");
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
            if (object instanceof Asignatura) {
                Asignatura o = (Asignatura) object;
                return getStringKey(o.getIdAsignatura());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Asignatura.class.getName());
            }
        }

    }

}
