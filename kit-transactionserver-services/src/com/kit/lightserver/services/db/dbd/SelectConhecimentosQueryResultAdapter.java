package com.kit.lightserver.services.db.dbd;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.dajo.framework.db.SelectQueryResultAdapter;
import org.dajo.framework.db.util.MsSqlBitConverter;

import com.kit.lightserver.domain.types.ConhecimentoSTY;

public final class SelectConhecimentosQueryResultAdapter implements SelectQueryResultAdapter<List<ConhecimentoSTY>> {

    @Override
    public List<ConhecimentoSTY> adaptResultSet(final ResultSet rs) throws SQLException {

        final List<ConhecimentoSTY> result = new LinkedList<ConhecimentoSTY>();
        while (rs.next()) {

            final int conhecimentoKtRowId = rs.getInt("KTRowId");
            final String ktClientId = rs.getString("KTClientUserId");

            final boolean isReceived = MsSqlBitConverter.convert(rs.getInt("KTFlagRecebido"));
            final boolean isRead = MsSqlBitConverter.convert(rs.getInt("KTFlagLido"));
            final boolean isEdited = MsSqlBitConverter.convert(rs.getInt("KTFlagEditado"));

            final String numeroConhecimento = rs.getString("KTFieldNumeroDoConhecimento"); // knowledgeNumber
            final String serialConhecimento = rs.getString("KTFieldSerialDoConhecimento"); // knowledgeSerial
            final String codigoFilial = rs.getString("KTFieldCodigoDaSubsidiaria"); // subsidiaryCode

            final String remetenteCNPJ = rs.getString("KTFieldRemetenteId"); // senderId
            final String destinatarioNome = rs.getString("KTFieldNomeDoDestinatario"); // recipientName
            //final String statusDaEntregaStr = rs.getString("KTCelularEntregaStatus"); // deliveryStatus
            //final StatusEntregaEnumSTY statusDaEntrega = StatusEntregaSTYParser.parse(statusDaEntregaStr);// StatusEntregaEnumSTY.AN_AINDA_NAO_ENTREGUE;

            final Date conhecimentoDataEntrega = null;

            final String title = "CO " + numeroConhecimento + " " + serialConhecimento + " " + codigoFilial;

            final ConhecimentoSTY conhecimentoSTY = new ConhecimentoSTY(conhecimentoKtRowId, ktClientId, isReceived, isRead, isEdited, title, remetenteCNPJ,
                    destinatarioNome, conhecimentoDataEntrega);

            result.add(conhecimentoSTY);

        }// while

        return result;

    }

}// class
