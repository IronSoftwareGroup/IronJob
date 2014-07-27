package com.gn.ironj.controller;

import com.gn.ironj.controller.util.JsfUtil;
import com.gn.ironj.controller.util.JsfUtil.PersistAction;
import com.gn.ironj.engine.LogManager;
import com.gn.ironj.entity.Activity;
import com.gn.ironj.entity.Connector;
import com.gn.ironj.entity.Params;
import com.gn.ironj.services.ActivityFacade;
import com.gn.ironj.services.ConnectorFacade;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
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
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletResponse;

@ManagedBean(name = "activityController")
@SessionScoped
public class ActivityController implements Serializable {

    @EJB
    private com.gn.ironj.services.ActivityFacade ejbFacade;
    @EJB
    private com.gn.ironj.services.ParamsFacade ejbParams;
    private List<Activity> items = null;
    private List<Params> params = null;
    private Activity selected;
    private Params selectedParam;
    private List<File> logFile;
    private List<String> logLine;
    @EJB
    private ConnectorFacade ejbConnector;
    private List<Connector> connecters;

    public ActivityController() {
    }

    public Activity getSelected() {
        return selected;
    }

    public void setSelected(Activity selected) {
        this.selected = selected;
    }

    public Params getSelectedParam() {
        return selectedParam;
    }

    public void setSelectedParam(Params selectedParam) {
        this.selectedParam = selectedParam;
    }

    public List<Connector> getConnecters() {
        return connecters;
    }

    public void setConnecters(List<Connector> connecters) {
        this.connecters = connecters;
    }

        
       

    public List<File> getLogFile() {
        return logFile;
    }

    public void setLogFile(List<File> logFile) {
        this.logFile = logFile;
    }

    public List<String> getLogLine() {
        return logLine;
    }

    public void setLogLine(List<String> logLine) {
        this.logLine = logLine;
    }
    
    public List<Connector> getAvailableConnector(){
       return ejbConnector.findAll();
    }

    public void copyActivity() {
        String name = selected.getName();
        String dsc = selected.getDescription();
        String log = selected.getLog();
        String path = selected.getPath();
        String type = selected.getType();
        selected = new Activity();
        initializeEmbeddableKey();
        selected.setName(name);
        selected.setDescription(dsc);
        selected.setPath(path);
        selected.setLog(log);
        selected.setType(type);

    }

    public String loadLogFile() {
        Logger.getLogger(LogManager.class.getName()).log(Level.FINE, "Parsing log dir for path ectraction : {0}", selected.getLog());
        int i = selected.getLog().indexOf("logfile=");
        if(i>0){
            String logPath = selected.getLog().substring(i + 8, selected.getLog().length());
            Logger.getLogger(LogManager.class.getName()).log(Level.FINE, "Log dir is: {0}", logPath);
            Logger.getLogger(LogManager.class.getName()).log(Level.FINE, "Call LogMamanger for files load");
            logFile = LogManager.getLogsFromDirectory(logPath);
            Logger.getLogger(LogManager.class.getName()).log(Level.FINE, "Number of files retrieved: {0}", logFile.size());
        }
         return "Log";
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private ActivityFacade getFacade() {
        return ejbFacade;
    }

    public Activity prepareCreate() {
        selected = new Activity();
        connecters=ejbConnector.findAll();
        initializeEmbeddableKey();
        
        
        return selected;
    }

    public List<Params> getParams() {
        if (params == null) {

            params = ejbParams.findByActivity(selected.getId());
        }

        return params;
    }

    public void setParams(List<Params> params) {
        System.out.println("imposto parametri  ");
        this.params = params;
    }

    public String run() {

        return "/app/UserInput?id=" + selected.getId() + "&faces-redirect=true";
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Activity").getString("ActivityCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Activity").getString("ActivityUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Activity").getString("ActivityDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Activity> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    //LEggere il log e metterlo in nell array di stringhe per visualizzarlo
    public String viewLog(String log) {
       return null;
    

    }

    public void downloadLog(File f) {

        FileInputStream input = null;
        try {
            FacesContext fc = FacesContext.getCurrentInstance();
            HttpServletResponse response = (HttpServletResponse) fc.getExternalContext().getResponse();
            response.reset();
            response.setContentType("text");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + f.getName() + "\"");

            OutputStream output = response.getOutputStream();
            input = new FileInputStream(f.getAbsolutePath());
            byte[] buffer = new byte[1024]; // Adjust if you want
            int bytesRead;
            while ((bytesRead = input.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
            fc.responseComplete();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(UserInputController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(UserInputController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(UserInputController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
    
    public void deleteLog(File f){
        f.delete();
        loadLogFile();
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
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Activity").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Activity").getString("PersistenceErrorOccured"));
            }
        }
    }

    public List<Activity> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Activity> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Activity.class)
    public static class ActivityControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ActivityController controller = (ActivityController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "activityController");
            return controller.getFacade().find(getKey(value));
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
            if (object instanceof Activity) {
                Activity o = (Activity) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Activity.class.getName()});
                return null;
            }
        }

    }

}
