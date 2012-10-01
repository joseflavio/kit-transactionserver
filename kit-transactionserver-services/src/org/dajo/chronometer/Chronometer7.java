package org.dajo.chronometer;


public class Chronometer7 extends Chronometer {

    public Chronometer7(final String taskName) {
        super(taskName);
    }

    public Chronometer7(final Object taskClass, final String subTaskName) {
        super(taskClass, subTaskName);
    }

    public ChronometerResource getAsResource() {
        ChronometerResource resource = new ChronometerResource(this);
        super.start();
        return resource;

    }

    static public final class ChronometerResource implements AutoCloseable {
        private final Chronometer7 chronometer;
        public ChronometerResource(final Chronometer7 chronometer) {
            this.chronometer = chronometer;
        }
        @Override
        public void close() {
            chronometer.close();
        }
    }

}// class
