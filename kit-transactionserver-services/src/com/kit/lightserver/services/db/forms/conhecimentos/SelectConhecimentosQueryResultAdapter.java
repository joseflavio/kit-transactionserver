package com.kit.lightserver.services.db.forms.conhecimentos;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.kit.lightserver.domain.types.ConhecimentoSTY;
import com.kit.lightserver.domain.types.StatusEntregaEnumSTY;
import com.kit.lightserver.services.db.SelectQueryResultAdapter;
import com.kit.lightserver.services.db.common.MsSqlBitConverter;

public final class SelectConhecimentosQueryResultAdapter implements SelectQueryResultAdapter<List<ConhecimentoSTY>> {

    @Override
    public List<ConhecimentoSTY> adaptResultSet(final ResultSet rs) throws SQLException {

        final List<ConhecimentoSTY> result = new LinkedList<ConhecimentoSTY>();
        while (rs.next()) {

            final int conhecimentoKtRowId = rs.getInt("KTRowId");
            final String ktClientId = rs.getString("KTClientId");

            final boolean isReceived = MsSqlBitConverter.convert(rs.getInt("KTFlagRecebido"));
            final boolean isRead = MsSqlBitConverter.convert(rs.getInt("KTFlagLido"));
            final boolean isEdited = MsSqlBitConverter.convert(rs.getInt("KTFlagEditado"));

            // final String tempKtStatus = rs.getString("KTStatus");
            final String numeroConhecimento = rs.getString("knowledgeNumber");
            final String serialConhecimento = rs.getString("knowledgeSerial");
            final String codigoFilial = rs.getString("subsidiaryCode");

            final String remetenteCNPJ = rs.getString("senderId");
            final String destinatarioNome = rs.getString("recipientName");
            final String statusDaEntregaStr = rs.getString("deliveryStatus");
            final StatusEntregaEnumSTY statusDaEntrega = StatusEntregaSTYParser.parse(statusDaEntregaStr);// StatusEntregaEnumSTY.AN_AINDA_NAO_ENTREGUE;

            final Date conhecimentoDataEntrega = rs.getDate("deliveryDate");

            final String title = "CO " + numeroConhecimento + " " + serialConhecimento + " " + codigoFilial;

            final ConhecimentoSTY conhecimentoSTY = new ConhecimentoSTY(conhecimentoKtRowId, ktClientId, isReceived, isRead, isEdited, title, remetenteCNPJ,
                    destinatarioNome, conhecimentoDataEntrega, statusDaEntrega);

            result.add(conhecimentoSTY);

        }

        return result;

    }

}// class
