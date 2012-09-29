package com.kit.lightserver.types.response;

public final class ChannelProgressRSTY implements ClientResponseRSTY {

    private final int numberOfSteps;

    public ChannelProgressRSTY(final int numberOfSteps) {
        super();
        this.numberOfSteps = numberOfSteps;
    }

    public int getNumberOfSteps() {
        return numberOfSteps;
    }

    @Override
    public String toString() {
        return "ChannelProgressRSTY [numberOfSteps=" + numberOfSteps + "]";
    }

}// class
