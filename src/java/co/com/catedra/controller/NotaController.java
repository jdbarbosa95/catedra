package co.com.catedra.controller;

import co.com.catedra.controller.dto.NotaDto;
import co.com.catedra.eo.Nota;
import co.com.catedra.controller.util.JsfUtil;
import co.com.catedra.controller.util.PaginationHelper;
import co.com.catedra.eo.Asignatura;
import co.com.catedra.operaciones.NotaFacade;

import java.io.Serializable;
import java.util.ArrayList;
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

@ManagedBean(name = "notaController")
@SessionScoped
public class NotaController implements Serializable {

    @ManagedProperty(value = "#{loginUsuarioController}")
    private LoginUsuarioController luc;

    @ManagedProperty(value = "#{cursoController}")
    private CursoController cursoController;

    @ManagedProperty(value = "#{usuarioController}")
    private UsuarioController usuarioController;

    @ManagedProperty(value = "#{asignaturaController}")
    private AsignaturaController asignaturaController;
    @ManagedProperty(value = "#{logroController}")
    private LogroController logroController;

    @ManagedProperty(value = "#{estudianteController}")
    private EstudianteController estudianteController;

    @ManagedProperty(value = "#{periodoController}")
    private PeriodoController periodoController;

    /*@ManagedProperty(value = "#{loginUsuarioController}")
     private AsignaturaController ac;*/
    private Asignatura asignatura;
    private List<Nota> lista;
 private List<NotaDto> listaNotaEstMdf;
    private Nota current;
    private DataModel items = null;
    @EJB
    private co.com.catedra.operaciones.NotaFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public NotaController() {
    }

    public Nota getSelected() {
        if (current == null) {
            current = new Nota();
            selectedItemIndex = -1;
        }
        return current;
    }

    private NotaFacade getFacade() {
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
        current = (Nota) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Nota();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("NotaCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public void crearNotasEstudiante() {
       
            for (NotaDto notaDto : estudianteController.getListaEstNota()) {
            notaDto.getNota().setEstudianteidEstudiante(notaDto.getEstudiante());
            notaDto.getNota().setAsignaturaidAsignatura(asignaturaController.getAsignaturaSelecDocente());
            notaDto.getNota().setFechaRegistro(new Date());
            notaDto.getNota().setLogroidLogro(logroController.getLogroSel());
            notaDto.getNota().setPeriodoidPeriodo(usuarioController.getPeriodoSelecionado());
            getFacade().create(notaDto.getNota());
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("NotaCreated"));
        }
        estudianteController.setListaEstNota(new ArrayList());
            
        
        

    }

    
    public void mostrarNotasEstuDocent(){
    
    lista = ejbFacade.listarNotasEstuDocenteUpda(luc.getUsuario(),usuarioController.getCursoSelecUsuario(),asignaturaController.getAsignaturaSelecDocente(),periodoController.getPeriodoSelec());
      listaNotaEstMdf = new ArrayList();
        for (Nota n : lista) {
            NotaDto na = new NotaDto();
            na.setEstudiante(n.getEstudianteidEstudiante());
            na.setNota(new Nota());
           listaNotaEstMdf.add(na);
       }
    }
    
    public void actuliNotaEstuDocen() {

        try {
            
            for (Nota nota : lista) {
                
                getFacade().edit(nota);
              //  create(notaDto.getNota());
            }
            setListaNotaEstMdf(new ArrayList()); 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

        

   

    public String prepareEdit() {
        current = (Nota) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("NotaUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Nota) getItems().getRowData();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("NotaDeleted"));
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

    //--

    public void obtenerNotasEstudianteEstu() {
        lista = ejbFacade.buscarMisNotasEstudiante(luc.getUsuario(), asignaturaController.getAsignaturaSelecDocente(), periodoController.getPeriodoSelec());
    }

////-----
    /* public void obtenerNotasestudiantes() {
     lista = ejbFacade.buscarMisNotasEstudiante(luc.getEstudiante(),ac.getAsignaturaSelecDocente());
     }*/
    
    public List<Nota> getLista() {
        return lista;
    }

    public void setLista(List<Nota> lista) {
        this.lista = lista;
    }

    public void setLuc(LoginUsuarioController luc) {
        this.luc = luc;
    }

    /* public void setAc(AsignaturaController ac) {
     this.ac = ac;
     }*/
    public Asignatura getAsignatura() {
        return asignatura;
    }

    public void setAsignatura(Asignatura asignatura) {
        this.asignatura = asignatura;
    }

    public void setCursoController(CursoController cursoController) {
        this.cursoController = cursoController;
    }

    public void setUsuarioController(UsuarioController usuarioController) {
        this.usuarioController = usuarioController;
    }

    public void setAsignaturaController(AsignaturaController asignaturaController) {
        this.asignaturaController = asignaturaController;
    }

    public void setLogroController(LogroController logroController) {
        this.logroController = logroController;
    }

    public void setEstudianteController(EstudianteController estudianteController) {
        this.estudianteController = estudianteController;
    }

    public void setPeriodoController(PeriodoController periodoController) {
        this.periodoController = periodoController;
    }

    public void guardarNotas() {

    }

    public List<NotaDto> getListaNotaEstMdf() {
        return listaNotaEstMdf;
    }

    public void setListaNotaEstMdf(List<NotaDto> listaNotaEstMdf) {
        this.listaNotaEstMdf = listaNotaEstMdf;
    }

    @FacesConverter(forClass = Nota.class)
    public static class NotaControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            NotaController controller = (NotaController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "notaController");
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
            if (object instanceof Nota) {
                Nota o = (Nota) object;
                return getStringKey(o.getIdNota());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Nota.class.getName());
            }
        }

    }

}
