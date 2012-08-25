package com.kit.lightserver.statemachine.types;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.kit.lightserver.domain.types.FormSTY;

public final class CommunicationCTX {

    private final LinkedList<FormSTY> formsToSendOrderedList = new LinkedList<>();

    private final List<FormSTY> formSentList = new LinkedList<>();

    public void addToFormsToSendOrderedList(final FormSTY formSTY) {
        formsToSendOrderedList.add(formSTY);
    }

    public int getAllFormsListSize() {
        return formsToSendOrderedList.size();
    }

    public List<FormSTY> extractFormsToSendInOrder(final int maxNumberOfFormsToExtract) {

        final List<FormSTY> result = new LinkedList<>();

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

    public void addToFormSentList(final Collection<FormSTY> formSTY) {
        formSentList.addAll(formSTY);
    }

    public List<FormSTY> getFormsSentWaitingForConfirmationList() {
        return formSentList;
    }

    public void clearSentListWaitingForConfirmation() {
        formSentList.clear();
    }

}// class
