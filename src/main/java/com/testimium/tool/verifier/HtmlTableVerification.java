package com.testimium.tool.verifier;

import com.testimium.tool.domain.CommandParam;
import com.testimium.tool.exception.VerificationException;

public class HtmlTableVerification extends AbstractVerificationType {

    private static HtmlTableVerification verification = new HtmlTableVerification();

    public static HtmlTableVerification getInstance() {
        //TODO Fix Me for concurrent access
        return verification;
    }

    @Override
    public void verify(CommandParam param) throws VerificationException {
        UiTableVerification.getInstance().verify(param);
    }
}
