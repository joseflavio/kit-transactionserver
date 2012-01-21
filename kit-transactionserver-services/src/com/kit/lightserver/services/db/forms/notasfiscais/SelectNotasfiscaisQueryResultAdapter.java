package com.kit.lightserver.services.db.forms.notasfiscais;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.kit.lightserver.domain.types.NotafiscalSTY;
import com.kit.lightserver.domain.types.StatusEntregaEnumSTY;
import com.kit.lightserver.services.db.SelectQueryResultAdapter;
import com.kit.lightserver.services.db.conhecimentos.StatusEntregaSTYParser;

public final class SelectNotasfiscaisQueryResultAdapter implements SelectQueryResultAdapter<List<NotafiscalSTY>> {

    @Override
    public List<NotafiscalSTY> adaptResultSet(final ResultSet rs) throws SQLException {

        final List<NotafiscalSTY> result = new LinkedList<NotafiscalSTY>();
        while (rs.next()) {

            final int notafiscalKtRowId = rs.getInt("KTRowId");
            final int parentConhecimentoRowId = rs.getInt("knowledgeRowId");

            final boolean isReceived = true;
            final boolean isRead = true;
            final boolean isEdited = false;

            // final String tempKtStatus = rs.getString("KTStatus");
            final String numeroConhecimento = rs.getString("receiptNumber");
            final String serialConhecimento = rs.getString("receiptSerial");

            final String statusDaEntregaStr = rs.getString("deliveryStatus");
            final StatusEntregaEnumSTY statusDaEntrega = StatusEntregaSTYParser.parse(statusDaEntregaStr);// StatusEntregaEnumSTY.AN_AINDA_NAO_ENTREGUE;

            final Date dataDaEntrega = rs.getDate("deliveryDate");

            final String title = "NF " + numeroConhecimento + " " + serialConhecimento;

            final NotafiscalSTY conhecimentoSTY = new NotafiscalSTY(notafiscalKtRowId, isReceived, isRead, isEdited, parentConhecimentoRowId, title,
                    dataDaEntrega, statusDaEntrega);

            result.add(conhecimentoSTY);

        }

        return result;

    }

}// class
