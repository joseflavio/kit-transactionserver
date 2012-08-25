package com.kit.lightserver.domain.filters;

import com.fap.collections.SmartCollectionMatchFilter;

import com.kit.lightserver.domain.types.FormClientRowIdSTY;
import com.kit.lightserver.domain.types.TemplateEnumSTY;

public final class FormRowIdFilter implements SmartCollectionMatchFilter<FormClientRowIdSTY> {

    private final TemplateEnumSTY template;

    public FormRowIdFilter(final TemplateEnumSTY template) {
        this.template = template;
    }

    @Override
    public boolean match(final FormClientRowIdSTY formClientRowId) {
        if( template.equals(formClientRowId.getFormType()) == true ) {
            return true;
        }
        return false;
    }

}// class
