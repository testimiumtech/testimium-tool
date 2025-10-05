package com.testimium.tool.verifier;

import com.testimium.tool.datasource.connector.register.DataSourceRegistry;
import com.testimium.tool.domain.CommandParam;
import com.testimium.tool.exception.VerificationException;
import com.testimium.tool.logging.LogUtil;

public class SQLTableVerification extends AbstractVerificationType {

    private static SQLTableVerification verification = new SQLTableVerification();

    public static SQLTableVerification getInstance() {
        //TODO Fix Me for concurrent access
        return verification;
    }

    @Override
    public void verify(CommandParam commandParam) throws VerificationException {
        try {
            DataSourceRegistry.getConnectorMap();
            this.setInputParameter(commandParam.getInputParam());
            this.setAssertParameter(commandParam.getAssertParam());
            this.assertTest(this.getDatabaseResult(
                    (null != commandParam.getArgs() && (commandParam.getArgs().length == 1 || commandParam.getArgs().length > 1)) ? commandParam.getArgs()[0] : null),
                    (null != commandParam.getArgs() && commandParam.getArgs().length == 2) ? commandParam.getArgs()[1] : null);

        } catch (Exception ex) {
            LogUtil.logTestCaseErrorMsg("SQLTableVerification Class: " + ex.getMessage(), ex);
            throw new VerificationException("VerificationException " + ex.getMessage(), ex);
        } /*catch (JsonParsingException ex) {
            throw new VerificationException("JsonParsingException: " + ex.getMessage(), ex);
        } catch (AssertParamNotFoundException e) {
            throw new VerificationException("AssertParamNotFoundException: " + e.getMessage(), e);
        } catch (InputParamNotFoundException e) {
            throw new VerificationException("InputParamNotFoundException: " + e.getMessage(), e);
        } catch (DBException | DBConnectorException dbex) {
            throw new VerificationException("DBException " + dbex.getMessage(), dbex);
        }*/
    }

}
