/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ironsg.ironj.engine;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bruno Condemi
 */
public class LogManager {

    public LogManager() {
    }

    public static List<File> getLogsFromDirectory(String logDir) {
        Logger.getLogger(LogManager.class.getName()).log(Level.FINE, "Load the files list from directory: {0}", logDir); 
        List<File> logFile = new ArrayList<File>();
        File f = new File(logDir);
        if (f.isDirectory()) {
            String[] allFiles = f.list(new FilenameFilter() {
                public boolean accept(File dir, String fileName) {
                    return fileName.endsWith(".log");
                }
            });
            for (int i = 0; i < allFiles.length; i++) {
                logFile.add(new File(logDir + File.separator + allFiles[i]));
                Logger.getLogger(LogManager.class.getName()).log(Level.FINE, "Found file: {0}", allFiles[i]); 
        
            }
        }
        return logFile;
    }

}
