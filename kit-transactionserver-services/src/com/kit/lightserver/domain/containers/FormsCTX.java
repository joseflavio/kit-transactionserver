package com.kit.lightserver.domain.containers;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.kit.lightserver.domain.types.ConhecimentoSTY;
import com.kit.lightserver.domain.types.NotafiscalSTY;

public final class FormsCTX {

    private final List<ConhecimentoSTY> conhecimentoList;
    private final List<NotafiscalSTY> notasfiscaisList;

    private final Map<Integer, List<NotafiscalSTY>> notasfiscaisPorConhecimentoMap = new HashMap<Integer, List<NotafiscalSTY>>();

    public FormsCTX(final List<ConhecimentoSTY> conhecimentoList, final List<NotafiscalSTY> notasfiscaisList) {

        this.conhecimentoList = conhecimentoList;
        this.notasfiscaisList = notasfiscaisList;

        for (NotafiscalSTY notafiscalSTY : notasfiscaisList) {

            final int parentConhecimentoRowId = notafiscalSTY.getParentKnowledgeRowId();

            final List<NotafiscalSTY> notasFiscaisDoConhecimentoList = notasfiscaisPorConhecimentoMap.get(parentConhecimentoRowId);
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
            final int parentConhecimentoRowId = conhecimentoSTY.getKtRowId();
            final List<NotafiscalSTY> notasFiscaisDoConhecimentoList = notasfiscaisPorConhecimentoMap.get(parentConhecimentoRowId);
            if (notasFiscaisDoConhecimentoList == null) {
                throw new RuntimeException("Invalid state. notasFiscaisDoConhecimentoList="+notasFiscaisDoConhecimentoList+", parentConhecimentoRowId="+parentConhecimentoRowId);
            }
            else if (notasFiscaisDoConhecimentoList.size() == 0) {
                throw new RuntimeException("Invalid state. notasFiscaisDoConhecimentoList="+notasFiscaisDoConhecimentoList+", parentConhecimentoRowId="+parentConhecimentoRowId);
            }
        }// for

    }

    public List<ConhecimentoSTY> getConhecimentoList() {
        return conhecimentoList;
    }

    public List<NotafiscalSTY> getNotasfiscaisPorConhecimento(final ConhecimentoSTY conhecimentoSTY) {
        final int parentRowId = conhecimentoSTY.getKtRowId();
        final List<NotafiscalSTY> notasfiscaisDoConhecimento = notasfiscaisPorConhecimentoMap.get(parentRowId);
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
