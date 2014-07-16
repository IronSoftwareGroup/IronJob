/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gn.ironj.controller;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Bruno Condemi
 */
@ManagedBean(name = "applicationController")
@SessionScoped
public class ApplicationController {

    private String username;
    private String password;

    public ApplicationController() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String logout() {
        System.out.println("Logout");
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession httpSession = (HttpSession) facesContext.getExternalContext().getSession(false);
        httpSession.invalidate();
        httpSession=null;

        return "/index?faces-redirect=true";

    }

    public String login() {

        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        System.out.println("Login");
       

        if (request.getUserPrincipal()==null) {
            try {
                request.login(username, password);
                System.out.println("Success");
            } catch (ServletException ex) {
                Logger.getLogger(ApplicationController.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
        request=null;

        return "/app/home?faces-redirect=true";
    }

    public void doLogin() {

        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        System.out.println("Login");
        try {
            request.login(username, password);
            System.out.println("Success");
        } catch (ServletException ex) {
            Logger.getLogger(ApplicationController.class.getName()).log(Level.SEVERE, null, ex);

        }
        System.out.println(request.getRequestURI());

    }

}
