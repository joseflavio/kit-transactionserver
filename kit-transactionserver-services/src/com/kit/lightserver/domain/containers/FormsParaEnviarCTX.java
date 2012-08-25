package com.kit.lightserver.domain.containers;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kit.lightserver.domain.types.ConhecimentoSTY;
import com.kit.lightserver.domain.types.FormUniqueIdSTY;
import com.kit.lightserver.domain.types.NotafiscalSTY;

public final class FormsParaEnviarCTX {

    private final List<ConhecimentoSTY> conhecimentoList;
    private final List<NotafiscalSTY> notasfiscaisList;

    private final Map2<FormUniqueIdSTY, List<NotafiscalSTY>> notasfiscaisPorConhecimentoMap = new HashMap2<>();

    private final Logger LOGGER = LoggerFactory.getLogger(FormsParaEnviarCTX.class);

    public FormsParaEnviarCTX(final List<ConhecimentoSTY> conhecimentoList, final List<NotafiscalSTY> notasfiscaisList) {

        this.conhecimentoList = conhecimentoList;
        this.notasfiscaisList = notasfiscaisList;

        for (NotafiscalSTY notafiscalSTY : notasfiscaisList) {

            FormUniqueIdSTY parentFormId = notafiscalSTY.getParentFormId();

            final List<NotafiscalSTY> notasFiscaisDoConhecimentoList = notasfiscaisPorConhecimentoMap.getByKey( parentFormId );
            if (notasFiscaisDoConhecimentoList != null) {
                notasFiscaisDoConhecimentoList.add(notafiscalSTY);
            }
            else {
                List<NotafiscalSTY> newNotasFiscaisDoConhecimentoList = new LinkedList<NotafiscalSTY>();
                newNotasFiscaisDoConhecimentoList.add(notafiscalSTY);
                notasfiscaisPorConhecimentoMap.put(parentFormId, newNotasFiscaisDoConhecimentoList);
            }

        }// for

        /*
         * Sanity Test
         */
        for (ConhecimentoSTY conhecimentoSTY : conhecimentoList) {
            final FormUniqueIdSTY parentFormId = conhecimentoSTY.getFormUniqueId();
            final List<NotafiscalSTY> notasFiscaisDoConhecimentoList = notasfiscaisPorConhecimentoMap.getByKey( parentFormId );
            if (notasFiscaisDoConhecimentoList == null || notasFiscaisDoConhecimentoList.size() == 0) {
                LOGGER.error("FormConhecimento without notasFiscais. parentConhecimentoRowId="+parentFormId+", notasFiscaisDoConhecimentoList="+notasFiscaisDoConhecimentoList);
            }
        }// for

    }

    public List<ConhecimentoSTY> getConhecimentoList() {
        return conhecimentoList;
    }

    public List<NotafiscalSTY> getNotasfiscaisPorConhecimento(final ConhecimentoSTY conhecimentoSTY) {
        final FormUniqueIdSTY parentFormId = conhecimentoSTY.getFormUniqueId();
        final List<NotafiscalSTY> notasfiscaisDoConhecimento = notasfiscaisPorConhecimentoMap.getByKey( parentFormId );
        return notasfiscaisDoConhecimento;
    }

    public int getAllFormsAvailableCount() {
        return conhecimentoList.size() + notasfiscaisList.size();
    }

    @Override
    public String toString() {
        return "FormsContext [conhecimentoList=" + conhecimentoList.size() + ", notasfiscaisList=" + notasfiscaisList.size()
                + ", notasfiscaisPorConhecimentoMap=" + notasfiscaisPorConhecimentoMap.size() + "]";
    }

}// class
