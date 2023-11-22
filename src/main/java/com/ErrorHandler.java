package com;

import com.control.Controller;

import java.util.List;
import java.util.ArrayList;

/**
 * ErrorHandler acts as a messenger between processes and the Controller.
 *      When a processes enters a state that requires the user to be
 *      notified, either an error or another state, the ErrorHandler
 *      will pass the message for the user from the process to the
 *      Controller. Inner process might not be "aware" of the Controller,
 *      but the Controller determines how the user will be notified, so the
 *      message must be relayed to the Controller via the ErrorHandler.
 *      If there is not an assigned Controller when Loafr enters an error state,
 *      ErrorHandler prints the message to stdout and ErrorHandler ends execution.
 *
 * @auhor Leah Lehmeier
 */
public class ErrorHandler
{
     private static Controller controller = null;
     private static List<String> errorLog = new ArrayList<>();

     public static void setController(Controller input)
     {
         controller = input;
     }

    /**
     * If a Controller has not yet been assigned, this function will handle the error.
     *      If a Controller has been assigned, this function will pass the
     *      message to the Controller where it will be displayed to the user.
     * @param message      a message for the user about the state of Loafr.
     */
     public static void logError(String message)
     {
         // If a Controller has not been assigned yet, handle error state here
         // Else, handle error state in Controller
         if (null == controller)
         {
             System.out.println(message);
         }
         else
         {
             controller.alertOutput(message);
         }
     }

     // I'm not sure if this method is necessary
     public static void processingTimeAlert(String message)
     {
         controller.alertOutput(message);
     }
}
