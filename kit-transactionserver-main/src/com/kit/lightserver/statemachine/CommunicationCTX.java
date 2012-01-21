package com.kit.lightserver.statemachine;

import java.util.LinkedList;
import java.util.List;

import com.kit.lightserver.domain.types.FormSTY;

public final class CommunicationCTX {

    private final LinkedList<FormSTY> formsToSendOrderedList = new LinkedList<FormSTY>();

    private final List<FormSTY> formSentList = new LinkedList<FormSTY>();

    public void addToFormsToSendOrderedList(final FormSTY formSTY) {
        formsToSendOrderedList.add(formSTY);
    }

    public int getFormsToSendOrderedListSize() {
        return formsToSendOrderedList.size();
    }

    public List<FormSTY> extractFormsToSendInOrder(final int maxNumberOfFormsToExtract) {

        final List<FormSTY> result = new LinkedList<FormSTY>();

        final int maxIndex;
        if( formsToSendOrderedList.size() < maxNumberOfFormsToExtract) {
            maxIndex = formsToSendOrderedList.size();
        }
        else {
            maxIndex = maxNumberOfFormsToExtract;
        }

        for (int i=0; i<maxIndex; i++) {
            final FormSTY head = formsToSendOrderedList.pop();
            result.add(head);
        }

        return result;

    }

    public void addToFormSentList(final FormSTY formSTY) {
        formSentList.add(formSTY);
    }

    public List<FormSTY> getFormsSentWaitingForConfirmationList() {
        return formSentList;
    }

    public void clearSentListWaitingForConfirmation() {
        formSentList.clear();
    }

}// class
