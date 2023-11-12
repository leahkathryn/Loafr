package com;

import com.control.Controller;

import java.util.List;
import java.util.ArrayList;

public class ErrorHandler
{
     private static Controller controller = null;
     private static List<String> errorLog = new ArrayList<>();

     public static void logError(String message)
     {
         if (null == controller)
         {
             System.out.println(message);
         }
         else
         {
             controller.alertOuput(message);
             System.exit(0);
         }
     }

     public static void processingTimeAlert(String message)
     {
         controller.alertOutput(message);
     }
}
