package com.kit.lightserver.services.db.dbd;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.dajo.framework.db.SelectQueryResultAdapter;

import com.kit.lightserver.domain.types.NotafiscalSTY;
import com.kit.lightserver.domain.types.StatusEntregaEnumSTY;
import com.kit.lightserver.domain.util.DateCopier;

public final class SelectNotasfiscaisQueryResultAdapter implements SelectQueryResultAdapter<List<NotafiscalSTY>> {

    @Override
    public List<NotafiscalSTY> adaptResultSet(final ResultSet rs) throws SQLException {

        final List<NotafiscalSTY> result = new LinkedList<NotafiscalSTY>();
        while (rs.next()) {

            final int notafiscalKtRowId = rs.getInt("KTRowId");
            final int parentConhecimentoRowId = rs.getInt("KTParentConhecimentoRowId");

            final boolean isReceived = true;
            final boolean isRead = true;
            final boolean isEdited = false;

            // final String tempKtStatus = rs.getString("KTStatus");
            final String numeroConhecimento = rs.getString("KTFieldReceiptNumber");
            final String serialConhecimento = rs.getString("KTFieldReceiptSerial");

            final String statusDaEntregaStr = rs.getString("KTCelularEntregaStatus");
            final StatusEntregaEnumSTY statusDaEntrega = StatusEntregaSTYParser.parse(statusDaEntregaStr);

            final java.sql.Timestamp  sqlDataDaEntrega = rs.getTimestamp("KTCelularEntregaData"); // DEVERIA REMOVER
            final Date dataDaEntrega = DateCopier.newInstance( sqlDataDaEntrega );

            final String title = "NF " + numeroConhecimento + " " + serialConhecimento;

            final NotafiscalSTY conhecimentoSTY = new NotafiscalSTY(notafiscalKtRowId, isReceived, isRead, isEdited, parentConhecimentoRowId, title,
                    dataDaEntrega, statusDaEntrega);

            result.add(conhecimentoSTY);

        }

        return result;

    }

}// class
