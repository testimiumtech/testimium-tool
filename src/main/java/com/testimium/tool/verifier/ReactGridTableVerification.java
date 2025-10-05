package com.testimium.tool.verifier;

import com.testimium.tool.domain.CommandParam;
import com.testimium.tool.exception.VerificationException;

public class ReactGridTableVerification extends AbstractVerificationType {

    private static ReactGridTableVerification verification = new ReactGridTableVerification();

    public static ReactGridTableVerification getInstance() {
        //TODO Fix Me for concurrent access
        return verification;
    }

    @Override
    public void verify(CommandParam commandParam) throws VerificationException {
        UiTableVerification.getInstance().verify(commandParam);
    }

}
