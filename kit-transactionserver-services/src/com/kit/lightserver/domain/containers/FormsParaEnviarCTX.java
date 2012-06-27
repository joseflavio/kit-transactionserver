package com.kit.lightserver.domain.containers;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kit.lightserver.domain.types.ConhecimentoSTY;
import com.kit.lightserver.domain.types.FormConhecimentoRowIdSTY;
import com.kit.lightserver.domain.types.NotafiscalSTY;

public final class FormsParaEnviarCTX {

    private final List<ConhecimentoSTY> conhecimentoList;
    private final List<NotafiscalSTY> notasfiscaisList;

    private final Map<Integer, List<NotafiscalSTY>> notasfiscaisPorConhecimentoMap = new HashMap<Integer, List<NotafiscalSTY>>();

    private final Logger LOGGER = LoggerFactory.getLogger(FormsParaEnviarCTX.class);

    public FormsParaEnviarCTX(final List<ConhecimentoSTY> conhecimentoList, final List<NotafiscalSTY> notasfiscaisList) {

        this.conhecimentoList = conhecimentoList;
        this.notasfiscaisList = notasfiscaisList;

        for (NotafiscalSTY notafiscalSTY : notasfiscaisList) {

            final int parentConhecimentoRowId = notafiscalSTY.getParentKnowledgeRowId();

            final List<NotafiscalSTY> notasFiscaisDoConhecimentoList = notasfiscaisPorConhecimentoMap.get( Integer.valueOf(parentConhecimentoRowId) );
            if (notasFiscaisDoConhecimentoList != null) {
                notasFiscaisDoConhecimentoList.add(notafiscalSTY);
            }
            else {
                List<NotafiscalSTY> newNotasFiscaisDoConhecimentoList = new LinkedList<NotafiscalSTY>();
                newNotasFiscaisDoConhecimentoList.add(notafiscalSTY);
                notasfiscaisPorConhecimentoMap.put(parentConhecimentoRowId, newNotasFiscaisDoConhecimentoList);
            }

        }// for

        /*
         * Sanity Test
         */
        for (ConhecimentoSTY conhecimentoSTY : conhecimentoList) {
            final FormConhecimentoRowIdSTY parentRowId = conhecimentoSTY.getKtFormRowId();
            final List<NotafiscalSTY> notasFiscaisDoConhecimentoList = notasfiscaisPorConhecimentoMap.get( Integer.valueOf(parentRowId.getKtFormRowId()) );
            if (notasFiscaisDoConhecimentoList == null || notasFiscaisDoConhecimentoList.size() == 0) {
                LOGGER.error("FormConhecimento without notasFiscais. parentConhecimentoRowId="+parentRowId+", notasFiscaisDoConhecimentoList="+notasFiscaisDoConhecimentoList);
            }
        }// for

    }

    public List<ConhecimentoSTY> getConhecimentoList() {
        return conhecimentoList;
    }

    public List<NotafiscalSTY> getNotasfiscaisPorConhecimento(final ConhecimentoSTY conhecimentoSTY) {
        final FormConhecimentoRowIdSTY parentRowId = conhecimentoSTY.getKtFormRowId();
        final List<NotafiscalSTY> notasfiscaisDoConhecimento = notasfiscaisPorConhecimentoMap.get( Integer.valueOf(parentRowId.getKtFormRowId()) );
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
