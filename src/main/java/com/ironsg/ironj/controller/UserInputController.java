/*
 * Copyright (C) 2014 Bruno Condemi
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.ironsg.ironj.controller;

import com.ironsg.ironj.controller.util.JsfUtil;
import com.ironsg.ironj.engine.processors.ProcessorException;
import com.ironsg.ironj.engine.processors.jdbc.JdbcProcessor;
import com.ironsg.ironj.engine.processors.kettle.JobProcessor;
import com.ironsg.ironj.entity.Activity;
import com.ironsg.ironj.entity.Connector;
import com.ironsg.ironj.entity.Params;
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
 * @author Iron Software Group
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
    private com.ironsg.ironj.services.ParamsFacade ejbParams;
    @EJB
    private com.ironsg.ironj.services.ActivityFacade ejbActivity;
    @EJB
    private com.ironsg.ironj.services.ConfigFacade ejbConfig;
    @EJB
    private com.ironsg.ironj.services.ConnectorFacade ejbConnector;
   
    private com.ironsg.ironj.engine.processors.jdbc.JdbcProcessor jdbcProcessor;

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
        Logger.getLogger(ApplicationController.class.getName()).log(Level.INFO, "Loading data for user {0}", user);
        tryLogin();
        if (inputActivity > 0) {
            activity = ejbActivity.find(inputActivity);
            params = ejbParams.findByActivity(inputActivity);
        }
        status = "Pronto per essere inviato";     
    }

    

    public void process() {
        log = "";
        Connector connector = ejbConnector.findById(activity.getConnectorId());
        if(activity.getType().equalsIgnoreCase("JDBC")){
           Logger.getLogger(UserInputController.class.getName()).log(Level.INFO, "JDBC Invoked from action {0} ", activity.getName());
                jdbcProcessor = new JdbcProcessor();
                
            try {
                jdbcProcessor.process(activity,connector, params);
            } catch (ProcessorException ex) {
                Logger.getLogger(UserInputController.class.getName()).log(Level.SEVERE, null, ex);
            }
                Logger.getLogger(UserInputController.class.getName()).log(Level.INFO, "JDBC action success ");
        }else{
        
        try {
           JobProcessor jobProcessor = new JobProcessor();
            jobProcessor.process(activity,connector, params);
            Logger.getLogger(ApplicationController.class.getName()).log(Level.INFO, "Activity {0} completed", activity.getName());
            JsfUtil.addSuccessMessage("Lavoro completato :-)");
            status = "Completato";
        } catch (Exception ex) {
            Logger.getLogger(UserInputController.class.getName()).log(Level.SEVERE, null, ex);
            status = "Terminato in errore";
            JsfUtil.addErrorMessage("Mi dispiace ma si è verificato un errore nell'esecuzione del lavoro :-(");
        }
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
