/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ironsg.ironj.engine;

import com.ironsg.ironj.controller.UserInputController;
import com.ironsg.ironj.entity.Params;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bruno Condemi
 */
public class JobProcessor {

     public static void process(String kitchen, String activity,List<Params> params) {
        
        Logger.getLogger(UserInputController.class.getName()).log(Level.INFO, "Kitchen variable is: "+kitchen);
        
        try {
            
            StringBuilder sb = new StringBuilder(256);
            sb.append(kitchen);
            sb.append(" ");
            sb.append("-file ");
            sb.append(activity);
                       
            for (Iterator<Params> it = params.iterator(); it.hasNext();) {
                Params p = it.next();
                System.out.println(p.getValue());
   
                sb.append(" -param ");
                sb.append(p.getParamsPK().getName());
                sb.append("=");
                sb.append(p.getValue());           
            }
                      
            Logger.getLogger(UserInputController.class.getName()).log(Level.INFO, 
                    "Running job from command line: "+sb.toString());
          
            Process p = Runtime.getRuntime().exec(sb.toString());
            p.waitFor();
            params = null;
      } catch (IOException ex) {
            Logger.getLogger(UserInputController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(UserInputController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        

    }
}
