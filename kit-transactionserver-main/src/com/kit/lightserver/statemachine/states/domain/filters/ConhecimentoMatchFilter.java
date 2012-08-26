package com.kit.lightserver.statemachine.states.domain.filters;

import com.fap.collections.SmartCollectionMatchFilter;

import com.kit.lightserver.domain.types.ConhecimentoSTY;
import com.kit.lightserver.domain.types.FormSTY;
import com.kit.lightserver.domain.types.FormUniqueIdComparator;
import com.kit.lightserver.domain.types.FormUniqueIdSTY;

public class ConhecimentoMatchFilter implements SmartCollectionMatchFilter<FormSTY> {

    private final FormUniqueIdSTY formId;

    public ConhecimentoMatchFilter(final FormUniqueIdSTY formId) {
        this.formId = formId;
    }

    @Override
    public boolean match(final FormSTY form) {
        if( form instanceof ConhecimentoSTY ) {
            ConhecimentoSTY conhecimento = (ConhecimentoSTY)form;
            FormUniqueIdSTY currentId = conhecimento.getFormUniqueId();
            if( FormUniqueIdComparator.compare(currentId, formId) == 0 ) {
                return true;
            }
        }
        return false;
    }

}
