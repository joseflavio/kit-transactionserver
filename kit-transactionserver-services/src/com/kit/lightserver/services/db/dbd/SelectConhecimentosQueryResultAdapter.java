package com.kit.lightserver.services.db.dbd;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.dajo.framework.db.SelectQueryResultAdapter;
import org.dajo.framework.db.util.MsSqlBitConverter;

import com.kit.lightserver.domain.types.ConhecimentoSTY;
import com.kit.lightserver.domain.types.FormFlagsSTY;
import com.kit.lightserver.domain.types.FormIdSTY;

public final class SelectConhecimentosQueryResultAdapter implements SelectQueryResultAdapter<List<ConhecimentoSTY>> {

    @Override
    public List<ConhecimentoSTY> adaptResultSet(final ResultSet rs) throws SQLException {

        final List<ConhecimentoSTY> result = new LinkedList<ConhecimentoSTY>();
        while (rs.next()) {

            final FormIdSTY formId = FormIdSTY.newInstance( rs.getString("ID") );
            final int coKtRowId = rs.getInt("KTRowId");
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

            final String title = "CO " + numeroConhecimento + " " + serialConhecimento + " " + codigoFilial;

            final FormFlagsSTY formFlags = new FormFlagsSTY(isReceived, isRead, isEdited);

            final ConhecimentoSTY conhecimentoSTY = new ConhecimentoSTY(formId, coKtRowId, ktClientId, formFlags, title, remetenteCNPJ, destinatarioNome);

            result.add(conhecimentoSTY);

        }// while

        return result;

    }

}// class
