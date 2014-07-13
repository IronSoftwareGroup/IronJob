/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gn.ironj.controller;

import com.gn.ironj.engine.JobProcessor;
import com.gn.ironj.entity.Activity;
import com.gn.ironj.entity.Params;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Bruno Condemi
 */
@ManagedBean(name = "userInputController")
@SessionScoped
public class UserInputController implements Serializable {

    private int inputActivity = 0;
    private Activity activity;
    private List<Params> params;
    @EJB
    private com.gn.ironj.services.ParamsFacade ejbParams;
     @EJB
    private com.gn.ironj.services.ActivityFacade ejbActivity;
     @EJB
     private com.gn.ironj.services.ConfigFacade ejbConfig;

    public UserInputController() {
    }

    
    public int getInputActivity() {
        return inputActivity;
    }

    public void setInputActivity(int input) {
        inputActivity = input;
  
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }
    
    

    public List<Params> getParams() {
       
            ejbParams.findByActivity(inputActivity);
        
        return params;

    }

    public void setParams(List<Params> params) {
        this.params = params;
    }
    
    public void loadData(){
       if(inputActivity>0){
        activity = ejbActivity.find(inputActivity);
        params = ejbParams.findByActivity(inputActivity);
       }
    }

    public void process() {
        String kitchen = getKitchen();
        Logger.getLogger(UserInputController.class.getName()).log(Level.INFO, "Kitchen variable is: "+kitchen);
        
        JobProcessor.process(kitchen, activity.getPath(), params);

    }
    private String getKitchen() {
        String kitchen = ejbConfig.getPropertyValue("KITCHEN");
        return kitchen;
    }
    
    
}
