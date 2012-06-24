USE [{dbname}_DBD];

INSERT INTO dbo.FormConhecimentos (
KTClientUserId, KTControleProntoParaEnviar, KTFieldNumeroDoConhecimento, KTFieldSerialDoConhecimento, KTFieldCodigoDaSubsidiaria, KTFieldRemetenteId, KTFieldNomeDoDestinatario) 
VALUES (
'ALEX', 1, 'NUM-6667', '321', '789', 'CNPJ-3101065', 'Para Plisken');


DECLARE @conhecimentoParentRowId bigint;
SELECT  @conhecimentoParentRowId=KTRowID FROM [dbo].[FormConhecimentos] WHERE KTClientUserId='ALEX' AND KTFieldNumeroDoConhecimento='NUM-6667'

INSERT INTO [dbo].[FormNotasfiscais] (KTParentConhecimentoRowId, KTClientUserId, KTControleProntoParaEnviar, KTFieldReceiptNumber, KTFieldReceiptSerial) VALUES
(@conhecimentoParentRowId, 'ALEX', 1, 1234, 2); -- Notas fiscais NF 1234 2






SELECT * FROM  dbo.Notasfiscais WHERE KTParentRowId=@conhecimentoParentRowId;

UPDATE dbo.Conhecimentos SET KTControleProntoParaEnviar=1 WHERE KTClientId='CNORRIS';
UPDATE dbo.Notasfiscais SET KTControleProntoParaEnviar=1 WHERE KTParentRowId=@conhecimentoParentRowId;


DELETE FROM  dbo.Conhecimentos WHERE KTClientId='CNORRIS';


      