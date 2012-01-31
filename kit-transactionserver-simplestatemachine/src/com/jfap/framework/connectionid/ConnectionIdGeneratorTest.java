package com.jfap.framework.connectionid;

final class ConnectionIdGeneratorTest {

    static public void main(final String[] args) {
        String shortUuidString = ConnectionIdGenerator.generateRandomConnectionId();
        System.out.println(shortUuidString);
    }

}// class
