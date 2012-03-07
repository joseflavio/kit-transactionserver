package com.fap.thread;

import java.util.concurrent.ThreadFactory;

public class RichThreadFactory implements ThreadFactory {

    private final String threadName;

    public RichThreadFactory(final String namePrefix, final HasThreadName o) {
        this.threadName = namePrefix + ":" + o.getThreadName();
    }

    @Override
    public Thread newThread(final Runnable r) {
        final Thread thread = new Thread(r, threadName);
        return thread;
    }

}// class
