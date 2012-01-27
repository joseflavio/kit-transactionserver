USE "TESTDEV_JOSEFLAVIO_KEEPIN3_MIRA_DBV20111129";

-- ----------------------------------------------------------------------------------------------------------
-- Table KTStatus (Deve ser removida depois)
-- ----------------------------------------------------------------------------------------------------------
INSERT INTO dbo.KTStatus (KTStatus) VALUES ('moved_to_new_version')
INSERT INTO dbo.KTStatus (KTStatus) VALUES ('atualizado_para_nova_versao')
SELECT KTStatus, count(KTRowId) FROM dbo.Conhecimentos GROUP BY KTStatus


-- ----------------------------------------------------------------------------------------------------------
-- Tabela LogConexoes
-- ----------------------------------------------------------------------------------------------------------
EXEC sp_rename 'LogConexoes.DataHora', 'KTRowInsertDbDateTime', 'COLUMN';
EXEC sp_rename 'LogConexoes.idA', 'KTCelularIdA', 'COLUMN';
EXEC sp_rename 'LogConexoes.idB', 'KTCelularIdB', 'COLUMN';
EXEC sp_rename 'LogConexoes.status', 'KTStatusDaConexao', 'COLUMN';
ALTER TABLE dbo.LogConexoes ADD KTCelularIdAB nchar(33) NULL DEFAULT NULL;
ALTER TABLE dbo.LogConexoes ADD KTConexaoID nchar(96) NULL DEFAULT NULL;
ALTER TABLE dbo.LogConexoes ADD KTRowInsertJDateAno int NULL DEFAULT NULL;
ALTER TABLE dbo.LogConexoes ADD KTRowInsertJDateMes int NULL DEFAULT NULL;
ALTER TABLE dbo.LogConexoes ADD KTRowInsertJDateDia int NULL DEFAULT NULL;
ALTER TABLE dbo.LogConexoes ADD KTRowInsertJDateTimeString nchar(48) NULL DEFAULT NULL;
ALTER TABLE dbo.LogConexoes ADD KTRowInsertJDateTimeNative datetime NULL; -- TIMESTAMP NAO ACEITA DEFAULT
CREATE NONCLUSTERED INDEX Index_KTRowInsertJDateAno ON dbo.LogConexoes(KTRowInsertJDateAno);
CREATE NONCLUSTERED INDEX Index_KTRowInsertJDateMes ON dbo.LogConexoes(KTRowInsertJDateMes);
CREATE NONCLUSTERED INDEX Index_KTRowInsertJDateDia ON dbo.LogConexoes(KTRowInsertJDateDia);
CREATE NONCLUSTERED INDEX Index_KTConexaoID ON dbo.LogConexoes(KTConexaoID);
CREATE NONCLUSTERED INDEX Index_KTCelularIdAB ON dbo.LogConexoes(KTCelularIdAB);
CREATE NONCLUSTERED INDEX Index_KTClientID ON dbo.LogConexoes(KTClientID);
CREATE NONCLUSTERED INDEX Index_KTStatusDaConexao ON dbo.LogConexoes(KTStatusDaConexao);

UPDATE [dbo].[LogConexoes] SET [KTCelularIdAB] = [KTCelularIdA]+':'+[KTCelularIdB]



-- ----------------------------------------------------------------------------------------------------------
-- ----------------------------------------------------------------------------------------------------------
-- Tabela Authenticate
-- ----------------------------------------------------------------------------------------------------------
-- ----------------------------------------------------------------------------------------------------------

EXEC sp_rename 'Authenticate.KTLastAuthentication', 'KTLastAuthenticationDbDateTime', 'COLUMN'
ALTER TABLE dbo.Authenticate ADD KTUsuarioConectado bit NOT NULL DEFAULT 0;
ALTER TABLE dbo.Authenticate ADD KTDeveResetar bit NOT NULL DEFAULT 0;
ALTER TABLE dbo.Authenticate ADD KTLastDisconnectionDbDateTime datetime NULL DEFAULT NULL;
CREATE NONCLUSTERED INDEX Index_KTClientId ON dbo.Authenticate(KTClientId);



-- ----------------------------------------------------------------------------------------------------------
-- ----------------------------------------------------------------------------------------------------------
-- Tabela Conhecimentos      
      [KTFirstRead]
      [KTLastUpdate]
      [deliveryStatus]
      [KTClientUpdate]
      
      [KTLastEdit]
      
-- ----------------------------------------------------------------------------------------------------------
      [comments]
      [DtPrevEntrega]
      [dtPrevSaida]
      [filialDestino]
      [CD_SETOR]
      [fl_dtEntrega]      
      [flVerificado] -> DELETAR?     
      [usuarioExclusao]
      [dtExclusao]
      [dtEmiManif]
      [FILIALMANIF]
      [NRMANIFESTO]
-- ----------------------------------------------------------------------------------------------------------

-- Campos que sao enviados para o celular
EXEC sp_rename 'Conhecimentos.knowledgeNumber', 'KTFieldNumeroDoConhecimento', 'COLUMN'
EXEC sp_rename 'Conhecimentos.knowledgeSerial', 'KTFieldSerialDoConhecimento', 'COLUMN'
EXEC sp_rename 'Conhecimentos.subsidiaryCode', 'KTFieldCodigoDaSubsidiaria', 'COLUMN'
EXEC sp_rename 'Conhecimentos.senderId', 'KTFieldRemetenteId', 'COLUMN'
EXEC sp_rename 'Conhecimentos.recipientName', 'KTFieldNomeDoDestinatario', 'COLUMN'

-- Campos que sao recebidos do celular
EXEC sp_rename 'Conhecimentos.deliveryStatus', 'KTCelularEntregaStatus', 'COLUMN'

-- -- ADICIONANDO COLUNAS --
ALTER TABLE dbo.Conhecimentos ADD KTCelularEntregaData datetime NULL;
ALTER TABLE dbo.Conhecimentos ADD KTCelularEntregaDataString nchar(48) NULL DEFAULT NULL;
ALTER TABLE dbo.Conhecimentos ADD KTCelularEntregaUpdateDBTime datetime NULL;

ALTER TABLE dbo.Conhecimentos ADD KTControleProntoParaEnviar bit NOT NULL DEFAULT 0;
ALTER TABLE dbo.Conhecimentos ADD KTControleDeveDeletar bit NOT NULL DEFAULT 0;

ALTER TABLE dbo.Conhecimentos ADD KTFlagRecebido bit NOT NULL DEFAULT 0;
ALTER TABLE dbo.Conhecimentos ADD KTFlagRecebidoUpdateDBTime datetime NULL;

ALTER TABLE dbo.Conhecimentos ADD KTFlagLido bit NOT NULL DEFAULT 0;
ALTER TABLE dbo.Conhecimentos ADD KTFlagLidoUpdateDBTime datetime NULL;

ALTER TABLE dbo.Conhecimentos ADD KTFlagEditado bit NOT NULL DEFAULT 0;
ALTER TABLE dbo.Conhecimentos ADD KTFlagEditadoUpdateDBTime datetime NULL;

ALTER TABLE dbo.Conhecimentos ADD KTFlagHistorico bit NOT NULL DEFAULT 0;
ALTER TABLE dbo.Conhecimentos ADD KTFlagHistoricoUpdateDBTime datetime NULL;

CREATE NONCLUSTERED INDEX Index_KTClientId ON dbo.Conhecimentos(KTClientId);
CREATE NONCLUSTERED INDEX Index_KTClientId_KTFlagHistorico_KTControleProntoParaEnviar ON dbo.Conhecimentos(KTClientId, KTFlagHistorico, KTControleProntoParaEnviar);

-- Migrando os dados (em teoria todos conhecimentos devem estar migrados ou devem ser marcados como nao prontos para enviar)

UPDATE dbo.Conhecimentos SET KTControleProntoParaEnviar=0, KTStatus='moved_to_new_version' WHERE KTStatus='new'
UPDATE dbo.Conhecimentos SET KTControleProntoParaEnviar=0, KTStatus='moved_to_new_version' WHERE KTStatus='sent'
UPDATE dbo.Conhecimentos SET KTControleProntoParaEnviar=1, KTFlagRecebido=1, KTStatus='moved_to_new_version' WHERE KTStatus='received'
UPDATE dbo.Conhecimentos SET KTControleProntoParaEnviar=1, KTFlagRecebido=1, KTFlagLido=1, KTStatus='moved_to_new_version' WHERE KTStatus='read'
UPDATE dbo.Conhecimentos SET KTControleProntoParaEnviar=1, KTControleDeveDeletar=1, KTStatus='moved_to_new_version' WHERE KTStatus='deleteRequestByExternal'
UPDATE dbo.Conhecimentos SET KTControleProntoParaEnviar=1, KTControleDeveDeletar=1, KTStatus='moved_to_new_version' WHERE KTStatus='deleteRequestSent'
UPDATE dbo.Conhecimentos SET KTControleProntoParaEnviar=1, KTFlagHistorico=1, KTStatus='moved_to_new_version' WHERE KTStatus='historic'

SELECT * FROM [dbo].[Conhecimentos] where KTStatus != 'moved_to_new_version' -- Verificar


UPDATE dbo.Conhecimentos SET KTCelularEntregaData=deliveryDate -- Passo que pode ser bastante demorado



-- Descartando colunas nao usadas
DROP INDEX Conhecimentos.IX_DELDateSt;
DROP INDEX Conhecimentos.IX_CTRC_Status;
DROP INDEX Conhecimentos.IX_DTEntrega;
DROP INDEX Conhecimentos.IX_DTPrevSaida;
DROP INDEX Conhecimentos.IX_DTSaida2;

ALTER TABLE Conhecimentos DROP CONSTRAINT DF_Conhecimentos_KTStatus;   
ALTER TABLE Conhecimentos DROP CONSTRAINT FK_Conhecimentos_KTStatus;

ALTER TABLE Conhecimentos DROP COLUMN KTClientRead;
ALTER TABLE Conhecimentos DROP COLUMN deliveryDate;
ALTER TABLE Conhecimentos DROP COLUMN KTLastStatusChange;
ALTER TABLE Conhecimentos DROP COLUMN KTStatus;
ALTER TABLE Conhecimentos DROP COLUMN KTLastRead;
ALTER TABLE Conhecimentos DROP COLUMN KTLastUpdate;



-- ----------------------------------------------------------------------------------------------------------
-- Tabela NotasFiscais
-- ----------------------------------------------------------------------------------------------------------
EXEC sp_rename 'NotasFiscais.knowledgeRowId', 'KTParentRowId', 'COLUMN'

ALTER TABLE dbo.NotasFiscais ADD KTControleProntoParaEnviar bit NOT NULL DEFAULT 0;
ALTER TABLE dbo.NotasFiscais ADD KTControleDeveDeletar bit NOT NULL DEFAULT 0;

ALTER TABLE dbo.NotasFiscais ADD KTFlagRecebido bit NOT NULL DEFAULT 0;
ALTER TABLE dbo.NotasFiscais ADD KTFlagRecebidoUpdateDBTime datetime NULL;

ALTER TABLE dbo.NotasFiscais ADD KTFlagHistorico bit NOT NULL DEFAULT 0;
ALTER TABLE dbo.NotasFiscais ADD KTFlagHistoricoUpdateDBTime datetime NULL;

CREATE NONCLUSTERED INDEX Index_KTParentConhecimentoRowId ON dbo.NotasFiscais(KTParentConhecimentoRowId);

UPDATE dbo.NotasFiscais SET KTFlagHistorico=1, KTStatus='moved_to_new_version' WHERE KTStatus='historic'
UPDATE dbo.NotasFiscais SET KTControleDeveDeletar=1, KTStatus='moved_to_new_version' WHERE KTStatus='deleteRequestByExternal'


