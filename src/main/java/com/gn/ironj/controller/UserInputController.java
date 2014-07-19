/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gn.ironj.controller;

import com.gn.ironj.controller.util.JsfUtil;
import com.gn.ironj.engine.JobProcessor;
import com.gn.ironj.entity.Activity;
import com.gn.ironj.entity.Params;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Bruno Condemi
 */
@ManagedBean(name = "userInputController")
@SessionScoped
public class UserInputController implements Serializable {

    private int inputActivity = 0;
    private String user, password;
    private Activity activity;
    private String status;
    private String log;
   
    private List<Params> params;
    @EJB
    private com.gn.ironj.services.ParamsFacade ejbParams;
    @EJB
    private com.gn.ironj.services.ActivityFacade ejbActivity;
    @EJB
    private com.gn.ironj.services.ConfigFacade ejbConfig;

    public UserInputController() {
    }

    /*
     *Getter and setter 
     */
    public int getInputActivity() {
        return inputActivity;
    }

    public void setInputActivity(int input) {
        inputActivity = input;

    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public List<Params> getParams() {
        ejbParams.findByActivity(inputActivity);
        return params;
    }

    public void setParams(List<Params> params) {
        this.params = params;
    }

    public void loadData() {
        Logger.getLogger(ApplicationController.class.getName()).log(Level.INFO, "Loading data for user " + user);
        tryLogin();
        if (inputActivity > 0) {
            activity = ejbActivity.find(inputActivity);
            params = ejbParams.findByActivity(inputActivity);
        }
        status = "Pronto per essere inviato";     
    }

    

    public void process() {
        log = "";
        String kitchen = getKitchen();
        try {
            log = JobProcessor.process(kitchen, activity.getPath(), activity.getName(), activity.getLog(), params);
            Logger.getLogger(ApplicationController.class.getName()).log(Level.INFO, "Activity {0} completed", activity.getName());
            JsfUtil.addSuccessMessage("Lavoro completato :-)");
            status = "Completato";
        } catch (Exception ex) {
            Logger.getLogger(UserInputController.class.getName()).log(Level.SEVERE, null, ex);
            status = "Terminato in errore";
            JsfUtil.addErrorMessage("Mi dispiace ma si è verificato un errore nell'esecuzione del lavoro :-(");
        }
    }

    private String getKitchen() {
        String kitchen = ejbConfig.getPropertyValue("KITCHEN");
        return kitchen;
    }

    private void tryLogin() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        Logger.getLogger(ApplicationController.class.getName()).log(Level.INFO, "Application was called from externalsystem");
        if (request.getUserPrincipal() == null) {
            try {
                Logger.getLogger(ApplicationController.class.getName()).log(Level.INFO, "User  {0} is not logged in, try to create new session", user);
                request.login(user, password);
                Logger.getLogger(ApplicationController.class.getName()).log(Level.INFO, "User  {0} is now authenticated", user);
            } catch (ServletException ex) {
                Logger.getLogger(ApplicationController.class.getName()).log(Level.SEVERE, null, ex);
                try {
                    FacesContext.getCurrentInstance().getExternalContext().redirect(request.getContextPath() + "/faces/index.xhtml");
                } catch (IOException ex1) {
                    Logger.getLogger(UserInputController.class.getName()).log(Level.SEVERE, null, ex1);
                }

            }
        }
    }

}
