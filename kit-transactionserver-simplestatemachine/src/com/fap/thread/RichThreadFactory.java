package com.fap.thread;


public final class RichThreadFactory {

    private RichThreadFactory() {
    }

    static public Thread newThread(final NamedRunnable r, final HasThreadSufixName o) {
        final String threadName = r.getThreadNamePrefix() + ":" + o.getThreadNameSufix();
        final Thread thread = new Thread(r, threadName);
        return thread;
    }

}// class
