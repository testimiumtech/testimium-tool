package com.testimium.tool.factory;

import com.testimium.tool.action.Tables;
import com.testimium.tool.exception.VerificationException;
import com.testimium.tool.verifier.AbstractVerificationType;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Sandeep Agrawal
 *
 */
public class VerificationTypeFactory {
    /**
     * This is used as a factory method AbstractTableVerification to get an instance of configured table.
     * TODO Fix Me for concurrent access
     * @param verifierType input param
     * @return VerificationException if verification failed
     */
    public static synchronized AbstractVerificationType  getVerificationInstance(String verifierType) throws VerificationException {
        synchronized(verifierType) {
            try {
           /* Class cls = Class.forName("com.testimium.tool.command.verification.type.database."+
                    Tables.valueOf(tableType.toUpperCase()).getTable()+"Verification");*/
                Class cls = Class.forName("com.testimium.tool.verifier." +
                        Tables.valueOf(verifierType.toUpperCase()).getTable() + "Verification");
                //cls.hashCode();
                Method method = cls.getDeclaredMethod("getInstance", null);
                //System.out.println("Got method: " + method);
                return (AbstractVerificationType) method.invoke(null, null);
            } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException |
                     InvocationTargetException | IllegalArgumentException e) {
                throw new VerificationException("SQLTableFactory failed with propertyKey '" + verifierType + "', " + e.getMessage());
            } catch (Exception ex) {
                throw new VerificationException("SQLTableFactory failed with propertyKey '" + verifierType + "', " + ex.getMessage());
            }
        }
    }
}
