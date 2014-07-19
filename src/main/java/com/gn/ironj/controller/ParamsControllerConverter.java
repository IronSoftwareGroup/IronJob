/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.gn.ironj.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author Bruno Condemi
 */
@FacesConverter("com.ironsg.ironj.controller.ParamsControllerConverter")
    public  class ParamsControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            System.out.println("concerti in data");
            if (value == null || value.length() == 0) {
                return null;
            }
            UserInputController controller = (UserInputController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "userInputController");
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            Date d = null;
            
            try {
                d=df.parse(value);
            } catch (ParseException ex) {
                Logger.getLogger(UserInputController.class.getName()).log(Level.SEVERE, null, ex);
            }
            return value;
        }

 

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            System.out.println("concerti in Stringa");
            if (object == null) {
                return null;
            }
            if (object instanceof Date) {
                DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                Date d = (Date)object;
                String r = df.format((d));
                return r;
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), String.class.getName()});
                String r = (String)object;
                return r;
            }
        }

    }
