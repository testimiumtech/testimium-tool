package com.testimium.tool.factory;

import com.testimium.tool.action.CommandTypes;
import com.testimium.tool.command.Command;
import com.testimium.tool.action.Commands;
import com.testimium.tool.exception.CommandNotFoundException;
import com.testimium.tool.logging.LogUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Sandeep Agrawal
 *
 */
public class CommandFactory {
    /**
     * This is used as a factory method to get an instance of passed command.
     * //TODO Fix Me for concurrent access
     * @param command
     * @return
     * @throws CommandNotFoundException
     */
    //@Deprecated
    public static synchronized Command getCommandInstance(String command) throws CommandNotFoundException {
        synchronized (command) {
            try {
                Class cls = Class.forName("com.testimium.tool.command." +
                        Commands.valueOf(command.toUpperCase()).getOperation() + "Cmd");
                //cls.hashCode();
                Method method = cls.getDeclaredMethod("getInstance", null);
                //System.out.println("Got method: " + method);
                return (Command) method.invoke(null, null);
            } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException |
                     InvocationTargetException | IllegalArgumentException e) {
                throw new CommandNotFoundException(command);
            }
        }
    }


   /* public static synchronized Command getCommandInstance(String command) throws CommandNotFoundException {
        synchronized (command) {
            Command cmd = null;
            try {
                cmd = CommandTypes.valueOf(CommandTypes.class, command.toUpperCase().trim()).getInstance();
            } catch (Exception ex) {
                LogUtil.logTestCaseErrorMsg(command + " Not Found Exception ", ex);
                new CommandNotFoundException(command);
            }

            return cmd;
        }
    }*/
}
