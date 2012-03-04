package com.fap.framework.adapters;


public final class FilepathVO {

    private final FilepathType type;
    private final String filepath;

    public FilepathVO(final FilepathType type, final String filepath) {
        this.type = type;
        this.filepath = filepath;
    }

    public FilepathType getType() {
        return type;
    }

    public String getFilepath() {
        return filepath;
    }

    @Override
    public String toString() {
        return "FilepathVO [type=" + type + ", filepath=" + filepath + "]";
    }

    public enum FilepathType {
        CLASS_PATH, FILESYSTEM
    }// enum

}// class
