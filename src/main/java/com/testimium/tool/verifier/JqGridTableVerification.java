package com.testimium.tool.verifier;

import com.testimium.tool.domain.CommandParam;
import com.testimium.tool.exception.VerificationException;

public class JqGridTableVerification extends AbstractVerificationType {

    private static JqGridTableVerification verification = new JqGridTableVerification();

    public static JqGridTableVerification getInstance() {
        //TODO Fix Me for concurrent access
        return verification;
    }

    @Override
    public void verify(CommandParam param) throws VerificationException {
        UiTableVerification.getInstance().verify(param);
    }
}
