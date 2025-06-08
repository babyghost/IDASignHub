package com.fpt.ida.idasignhub.data;

public class ParamRequest {
    String pathFolder;

    public void setPathFolder(String pathFolder) {
        this.pathFolder = pathFolder;
    }

    public String getPathFolder() {
        return pathFolder;
    }

    public ParamRequest() {}
    public ParamRequest(String pathFolder) {
        this.pathFolder = pathFolder;
    }
}
