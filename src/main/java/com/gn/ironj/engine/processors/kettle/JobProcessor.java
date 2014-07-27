/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gn.ironj.engine.processors.kettle;

import com.gn.ironj.controller.UserInputController;
import com.gn.ironj.engine.Helper;
import com.gn.ironj.engine.processors.IProcessor;
import com.gn.ironj.engine.processors.ProcessorException;
import com.gn.ironj.entity.Activity;
import com.gn.ironj.entity.Connector;
import com.gn.ironj.entity.Params;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bruno Condemi
 */
public class JobProcessor implements IProcessor {

    public JobProcessor() {
    }

    
    
    @Override
    public void process(Activity activity, Connector connector, List<Params> params) throws ProcessorException {

        String kitchen = connector.getUrl();
        String log = activity.getLog();
        String name = activity.getName();
        Logger.getLogger(UserInputController.class.getName()).log(Level.INFO, "Kitchen variable is: {0}", kitchen);
        boolean imWin = Helper.imWin();
        Logger.getLogger(UserInputController.class.getName()).log(Level.INFO, "I''m a Windows Server: {0}", imWin);

        StringBuilder logBuilder = new StringBuilder(4096);

        StringBuilder sb = new StringBuilder(256);
        if (imWin) {
            sb.append("cmd /c ");
        }
        sb.append(kitchen);
        sb.append(" ");
        if (imWin) {
            sb.append("/file ");
        } else {
            sb.append("-file ");
        }
        sb.append(activity.getPath());
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

        Logger.getLogger(UserInputController.class.getName()).log(Level.INFO, "Running job from command line: {0}", sb.toString());

        Process p=null;
        try {
            p = Runtime.getRuntime().exec(sb.toString());
        } catch (IOException ex) {
            Logger.getLogger(JobProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

        BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

        // read the output from the command     
        String s = null;
        try {
            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);
                logBuilder.append(s);
                logBuilder.append("\n");
            }
        } catch (IOException ex) {
            Logger.getLogger(JobProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            // read any errors from the attempted command
            while ((s = stdError.readLine()) != null) {
                System.err.println(s);
                logBuilder.append(s);
            }
        } catch (IOException ex) {
            Logger.getLogger(JobProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }

        int returnCode = 0;
        try {
            returnCode = p.waitFor();
        } catch (InterruptedException ex) {
            Logger.getLogger(JobProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (returnCode > 0) {
            Logger.getLogger(UserInputController.class.getName()).log(Level.SEVERE, "Error on job execution return code is: {0}", returnCode);
            throw new ProcessorException();
        }

    }

}
