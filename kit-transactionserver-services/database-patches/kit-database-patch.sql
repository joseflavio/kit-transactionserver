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
ALTER TABLE dbo.LogConexoes ADD KTRowInsertJDateTimeString nchar(32) NULL DEFAULT NULL;
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
-- Tabela Authenticate
-- ----------------------------------------------------------------------------------------------------------
EXEC sp_rename 'Authenticate.KTLastAuthentication', 'KTLastAuthenticationDbDateTime', 'COLUMN'
ALTER TABLE dbo.Authenticate ADD KTUsuarioConectado bit NOT NULL DEFAULT 0;
ALTER TABLE dbo.Authenticate ADD KTDeveResetar bit NOT NULL DEFAULT 0;
ALTER TABLE dbo.Authenticate ADD KTLastDisconnectionDbDateTime datetime NULL DEFAULT NULL;
CREATE NONCLUSTERED INDEX Index_KTClientId ON dbo.Authenticate(KTClientId);


-- ----------------------------------------------------------------------------------------------------------
-- Tabela Conhecimentos
-- ----------------------------------------------------------------------------------------------------------
EXEC sp_rename 'Conhecimentos.knowledgeNumber', 'KTFieldNumeroDoConhecimento', 'COLUMN'

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

-- Em teoria todos conhecimentos devem estar migrados ou devem ser marcados como nao prontos para enviar
UPDATE dbo.Conhecimentos SET KTFlagHistorico=1, KTStatus='moved_to_new_version' WHERE KTStatus='historic'
UPDATE dbo.Conhecimentos SET KTControleDeveDeletar=1, KTStatus='moved_to_new_version' WHERE KTStatus='deleteRequestByExternal'


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


