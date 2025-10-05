package com.testimium.tool.verifier;

import com.testimium.tool.domain.CommandParam;
import com.testimium.tool.exception.VerificationException;

public class SlickGridTableVerification extends AbstractVerificationType {

    private static SlickGridTableVerification verification = new SlickGridTableVerification();

    public static SlickGridTableVerification getInstance() {
        //TODO Fix Me for concurrent access
        return verification;
    }

    @Override
    public void verify(CommandParam commandParam) throws VerificationException {
        UiTableVerification.getInstance().verify(commandParam);
    }

}
