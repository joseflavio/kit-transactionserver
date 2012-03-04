package com.fap.framework.uniqueids;

final class ConnectionIdGeneratorTest {

    static public void main(final String[] args) {

        for (int i = 0; i < 200; ++i) {
            String shortUuidString = ConnectionIdGenerator.generateRandomConnectionId();
            System.out.println(i +": " + shortUuidString);
        }

    }

}// class
