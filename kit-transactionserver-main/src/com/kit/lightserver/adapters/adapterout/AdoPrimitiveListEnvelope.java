package com.kit.lightserver.adapters.adapterout;

import java.util.Arrays;
import java.util.List;

import com.kit.lightserver.types.response.ClientResponseRSTY;

public final class AdoPrimitiveListEnvelope implements AdoResponseEnvelope {

    private final List<ClientResponseRSTY> responseList;

    public AdoPrimitiveListEnvelope(final ClientResponseRSTY... responsesArray) {
        responseList = Arrays.asList(responsesArray);
    }

    public AdoPrimitiveListEnvelope(final List<ClientResponseRSTY> clientResponses) {
        this.responseList = clientResponses;
    }

    public List<ClientResponseRSTY> getAdoClientResponseList() {
        return responseList;
    }

}// class
