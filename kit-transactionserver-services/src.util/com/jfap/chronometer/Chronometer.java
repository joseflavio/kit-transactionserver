package com.jfap.chronometer;

public final class Chronometer {

    private final String taskName;

    private long startTime = -1;

    private long endTime = -1;

    public Chronometer(final String taskName) {
        this.taskName = taskName;
    }

    public void start() {
        startTime = System.currentTimeMillis();
    }

    public void stop() {
        endTime = System.currentTimeMillis();
    }

    public long getElapsedTime() {
        return endTime - startTime;
    }

    public String getLogString() {
        if(startTime < 0) {
            return "Chronometer [taskName=" + taskName + ", not started]";
        }
        else if(startTime < 0) {
            return "Chronometer [taskName=" + taskName + ", not stopped]";
        }
        else {
        return "Chronometer [taskName=" + taskName + ", elapsedTime="+getElapsedTime()+"]";
        }
    }

}// class
