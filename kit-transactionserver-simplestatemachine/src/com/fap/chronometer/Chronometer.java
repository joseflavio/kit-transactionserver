package com.fap.chronometer;

public final class Chronometer {

    private final String taskName;

    private long startTime = -1;

    private long endTime = -1;

    public Chronometer(final String taskName) {
        this.taskName = taskName;
    }

    public void start() {
        startTime = System.nanoTime();
    }

    public void stop() {
        endTime = System.nanoTime();
    }

    public double getElapsedTime() {
        return (endTime - startTime) / 1000000.0;
    }

    @Override
    public String toString() {
        if (startTime < 0) {
            return "Chronometer [taskName=" + taskName + ", (not started) ]";
        }
        else if (startTime < 0) {
            return "Chronometer [taskName=" + taskName + ", )not stopped) ]";
        }
        else {
            return "Chronometer [taskName=" + taskName + ", elapsedTime=" + getElapsedTime() + "ms]";
        }
    }

    static public double addElapsedTime(final Chronometer... chronometers) {
        double result = 0;
        for(int i=0; i<chronometers.length; ++i) {
            result += chronometers[i].getElapsedTime();
        }
        return result;
    }

}// class
