package com.fpt.ida.idasignhub.data;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import java.util.logging.Logger;

public class SignHubCallback implements CallbackHandler {
    private final Logger logger = Logger.getLogger(SignHubCallback.class.getName());

    @Override
    public void handle(Callback[] arg0) {
        logger.info("Chưa cắm USB ký số");
    }
}
