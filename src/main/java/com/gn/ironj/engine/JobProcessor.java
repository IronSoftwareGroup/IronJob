/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gn.ironj.engine;

import com.gn.ironj.controller.UserInputController;
import com.gn.ironj.entity.Params;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import sun.management.OperatingSystemImpl;

/**
 *
 * @author Bruno Condemi
 */
public class JobProcessor {

    public static void process(String kitchen, String activity, String name, String log, List<Params> params) throws Exception {

        Logger.getLogger(UserInputController.class.getName()).log(Level.INFO, "Kitchen variable is: " + kitchen);
        boolean imWin = Helper.imWin();
        Logger.getLogger(UserInputController.class.getName()).log(Level.INFO, "I'm a Windows Server: " + imWin);

       

            StringBuilder sb = new StringBuilder(256);
            sb.append("cmd /c ");
            sb.append(kitchen);
            sb.append(" ");
            if (imWin) {
                sb.append("/file ");
            } else {
                sb.append("-file ");
            }
            sb.append(activity);
            if (log.length() > 0) {
                sb.append(" ");
                sb.append(log);
                if (imWin) {
                    sb.append("\\");
                } else {
                    sb.append("/");
                }

                DateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmssSS");
                sb.append(df.format(new Date()));
                sb.append("_");
                name = name.replace(" ", "_");
                sb.append(name);
                sb.append(".log");
            }

            for (Iterator<Params> it = params.iterator(); it.hasNext();) {
                Params p = it.next();
                System.out.println(p.getValue());
                if (imWin) {
                    sb.append(" /param ");
                } else {
                    sb.append(" -param ");
                }
                sb.append(p.getParamsPK().getName());
                sb.append("=");
                sb.append(p.getValue());
            }

            Logger.getLogger(UserInputController.class.getName()).log(Level.INFO,
                    "Running job from command line: " + sb.toString());

            Process p = Runtime.getRuntime().exec(sb.toString());
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

        BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

        // read the output from the command
        System.out.println("Here is the standard output of the command:\n");
        String s = null;
        while ((s = stdInput.readLine()) != null) {
            System.out.println(s);
        }

        // read any errors from the attempted command
        System.out.println("Here is the standard error of the command (if any):\n");
        while ((s = stdError.readLine()) != null) {
            System.out.println(s);
        }
       
            int returnCode=p.waitFor();
            if(returnCode>0){
                throw new Exception("Error executing "+sb.toString());
            }
            params = null;
        

    }
}
