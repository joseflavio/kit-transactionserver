package com.kit.lightserver.statemachine.states.domain.filters;

import com.fap.collections.SmartCollectionMatchFilter;

import com.kit.lightserver.domain.types.FormSTY;
import com.kit.lightserver.domain.types.FormUniqueIdSTY;

public class ConhecimentoMatchFilter implements SmartCollectionMatchFilter<FormSTY> {

    private final FormUniqueIdSTY formId;

    public ConhecimentoMatchFilter(final FormUniqueIdSTY formId) {
        this.formId = formId;
    }

    @Override
    public boolean match(final FormSTY t) {
        // TODO Auto-generated method stub
        return false;
    }

}
