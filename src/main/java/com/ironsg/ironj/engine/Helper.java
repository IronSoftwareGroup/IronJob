/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ironsg.ironj.engine;

/**
 *
 * @author Bruno Condemi
 */
public class Helper {
    
    
    public static OperationSystem getOperatingSystem(){
        String OS = System.getProperty("os.name").toLowerCase();
        if(OS.contains("win")){
            return OperationSystem.WINDOWS;
        }else if(OS.contains("nix")||(OS.contains("nux"))|(OS.contains("aix"))){
            return OperationSystem.LINUX;
        }else if(OS.contains("sunos")){
            return OperationSystem.SOLARIS;
        }else if(OS.contains("mac")){
            return OperationSystem.MAC;
        }else {
            return OperationSystem.UNKNOW;
        }   
    }
    
    public static boolean imWin(){
        if(getOperatingSystem()==OperationSystem.WINDOWS){
            return true;
        }else {
            return false;
        }
    }
    
    
    public enum OperationSystem{
        WINDOWS,LINUX,UNIX,MAC,SOLARIS,UNKNOW
    }

}
