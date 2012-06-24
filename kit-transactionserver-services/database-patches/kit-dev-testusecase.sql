USE [{dbname}_D];

INSERT INTO dbo.Conhecimentos (KTClientId, KTCreation, KTFieldNumeroDoConhecimento, KTFieldSerialDoConhecimento, KTFieldCodigoDaSubsidiaria, KTFieldRemetenteId, KTFieldNomeDoDestinatario, KTCelularEntregaStatus) 
VALUES ('CNORRIS', GETDATE(), 'NUM-6667', '321', '789', 'CNPJ-3101065', 'Destinarario Plisken', 'AN');

USE "TESTDEV_JOSEFLAVIO_KEEPIN3_MIRA_DBV20111129";
DECLARE @conhecimentoParentRowId int;
SELECT  @conhecimentoParentRowId=KTRowID FROM dbo.Conhecimentos WHERE KTClientId='CNORRIS' AND KTFieldNumeroDoConhecimento='NUM-6667'

INSERT INTO dbo.Notasfiscais (KTCreation, KTParentRowId, receiptNumber, receiptSerial, deliveryStatus) VALUES
(GETDATE(), @conhecimentoParentRowId, 1234, 2, 'AN'); -- Notas fiscais NF 1234 2

SELECT * FROM  dbo.Notasfiscais WHERE KTParentRowId=@conhecimentoParentRowId;

UPDATE dbo.Conhecimentos SET KTControleProntoParaEnviar=1 WHERE KTClientId='CNORRIS';
UPDATE dbo.Notasfiscais SET KTControleProntoParaEnviar=1 WHERE KTParentRowId=@conhecimentoParentRowId;


DELETE FROM  dbo.Conhecimentos WHERE KTClientId='CNORRIS';


      