package com.gn.ironj.controller;

import com.gn.ironj.entity.Params;
import com.gn.ironj.controller.util.JsfUtil;
import com.gn.ironj.controller.util.JsfUtil.PersistAction;
import com.gn.ironj.services.ParamsFacade;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@ManagedBean(name = "paramsController")
@SessionScoped
public class ParamsController implements Serializable {

    @EJB
    private com.gn.ironj.services.ParamsFacade ejbFacade;
    private List<Params> items = null;
    private Params selected;
    private int selectedActivityId;

    public ParamsController() {
    }

    public Params getSelected() {
        return selected;
    }

    public void setSelected(Params selected) {
        this.selected = selected;
    }
    
    

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
        selected.setParamsPK(new com.gn.ironj.entity.ParamsPK());
    }

    private ParamsFacade getFacade() {
        return ejbFacade;
    }

    public Params prepareCreate() {
    
        selected = new Params();
        initializeEmbeddableKey();
        selected.getParamsPK().setActivityId(selectedActivityId);
        return selected;
    }

    public String prepareView(int actId){
        selectedActivityId=actId;
        items=null;
        return "/admin/params/List";
    }
    
    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Params").getString("ParamsCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Params").getString("ParamsUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Params").getString("ParamsDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Params> getItems() {
       
        if (items == null) {
            items = getFacade().findByActivity(selectedActivityId);
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(selected);
                } else {
                    getFacade().remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Params").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Params").getString("PersistenceErrorOccured"));
            }
        }
    }

    public List<Params> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Params> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Params.class)
    public static class ParamsControllerConverter implements Converter {

        private static final String SEPARATOR = "#";
        private static final String SEPARATOR_ESCAPED = "\\#";

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ParamsController controller = (ParamsController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "paramsController");
            return controller.getFacade().find(getKey(value));
        }

        com.gn.ironj.entity.ParamsPK getKey(String value) {
            com.gn.ironj.entity.ParamsPK key;
            String values[] = value.split(SEPARATOR_ESCAPED);
            key = new com.gn.ironj.entity.ParamsPK();
            key.setActivityId(Integer.parseInt(values[0]));
            key.setName(values[1]);
            return key;
        }

        String getStringKey(com.gn.ironj.entity.ParamsPK value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value.getActivityId());
            sb.append(SEPARATOR);
            sb.append(value.getName());
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Params) {
                Params o = (Params) object;
                return getStringKey(o.getParamsPK());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Params.class.getName()});
                return null;
            }
        }

    }

}
