package com.fpt.ida.idasignhub.data.model;


import java.io.Serializable;
import java.util.List;

public class SignHubLocal implements Serializable {
    private static final long serialVersionUID = 1L;

    List<SignHubLocalType> config;

    public List<SignHubLocalType> getConfig() {
        return config;
    }

    public void setConfig(List<SignHubLocalType> config) {
        this.config = config;
    }

    @Override
    public String toString() {
        return "SignHubLocal{" +
                "config=" + config +
                '}';
    }
}
