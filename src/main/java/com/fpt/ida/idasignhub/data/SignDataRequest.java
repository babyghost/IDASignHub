package com.fpt.ida.idasignhub.data;

public class SignDataRequest {
    String base64Data;
    ConfigSignature configSignature;

    public String getBase64Data() {
        return base64Data;
    }

    public void setBase64Data(String base64Data) {
        this.base64Data = base64Data;
    }

    public ConfigSignature getConfigSignature() {
        return configSignature;
    }

    public void setConfigSignature(ConfigSignature configSignature) {
        this.configSignature = configSignature;
    }
}
