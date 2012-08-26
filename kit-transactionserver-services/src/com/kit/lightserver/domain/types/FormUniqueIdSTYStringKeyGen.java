package com.kit.lightserver.domain.types;

import com.kit.lightserver.domain.containers.Map2KeyGenerator;

public final class FormUniqueIdSTYStringKeyGen implements Map2KeyGenerator<FormUniqueIdSTY> {

    @Override
    public String generateStringKey(final FormUniqueIdSTY formId) {
        String formType = formId.getFormType().name();
        String formIdValue = formId.getFormId().getValue();
        return formType + "-" + formIdValue;
    }

}// class
