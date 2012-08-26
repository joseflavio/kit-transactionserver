package com.kit.lightserver.services.db.dbd;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.dajo.framework.db.SelectQueryResultAdapter;

import com.kit.lightserver.domain.types.FormFlagsSTY;
import com.kit.lightserver.domain.types.FormIdSTY;
import com.kit.lightserver.domain.types.FormUniqueIdSTY;
import com.kit.lightserver.domain.types.NotafiscalSTY;
import com.kit.lightserver.domain.types.TemplateEnumSTY;

public final class SelectNotasfiscaisQueryResultAdapter implements SelectQueryResultAdapter<List<NotafiscalSTY>> {

    @Override
    public List<NotafiscalSTY> adaptResultSet(final ResultSet rs) throws SQLException {

        final List<NotafiscalSTY> result = new LinkedList<NotafiscalSTY>();
        while (rs.next()) {

            final FormIdSTY simpleFormId = FormIdSTY.newInstance( rs.getString( DBDTables.NOTASFISCAIS.FORMID ) );

            final int notafiscalKtRowId = rs.getInt( DBDTables.NOTASFISCAIS.FORM_CLIENT_ROWID );
            final String parentIdStr = rs.getString(  DBDTables.NOTASFISCAIS.PARENT_FORMID );
            final FormUniqueIdSTY parentFormId =  FormUniqueIdSTY.newInstance(TemplateEnumSTY.CO, parentIdStr);

            final boolean isReceived = true;
            final boolean isRead = true;
            final boolean isEdited = false;

            // final String tempKtStatus = rs.getString("KTStatus");
            final String numeroConhecimento = rs.getString("KTFieldReceiptNumber");
            final String serialConhecimento = rs.getString("KTFieldReceiptSerial");

            final FormFlagsSTY formFlags = new FormFlagsSTY(isReceived, isRead, isEdited);
            //final String statusDaEntregaStr = rs.getString("KTCelularEntregaStatus");
            //final StatusEntregaEnumSTY statusDaEntrega = StatusEntregaSTYParser.parse(statusDaEntregaStr);

            //final java.sql.Timestamp  sqlDataDaEntrega = rs.getTimestamp("KTCelularEntregaData"); // DEVERIA REMOVER
            //final Date dataDaEntrega = DateCopier.newInstance( sqlDataDaEntrega );

            final String title = "NF " + numeroConhecimento + " " + serialConhecimento;

            final NotafiscalSTY conhecimentoSTY = new NotafiscalSTY(simpleFormId, notafiscalKtRowId, formFlags, parentFormId, title);

            result.add(conhecimentoSTY);

        }

        return result;

    }

}// class
