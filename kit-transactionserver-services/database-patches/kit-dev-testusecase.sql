USE "TESTDEV_JOSEFLAVIO_KEEPIN3_MIRA_DBV20111129";

-- No celular o nome do usuario é limitado a 8 caracteres (deviamos mudar iss)
INSERT INTO dbo.Authenticate (KTClientId, KTPassword, KTUsuarioConectado, KTDeveResetar, creationDate) VALUES ('CNORRIS', '666', 0, 1, GETDATE());

SELECT * FROM  dbo.Authenticate WHERE KTClientId='CNORRIS';

DELETE FROM  dbo.Authenticate WHERE KTClientId='CNORRIS';


USE "TESTDEV_JOSEFLAVIO_KEEPIN3_MIRA_DBV20111129";

INSERT INTO dbo.Conhecimentos (KTClientId, KTCreation, KTFieldNumeroDoConhecimento, KTFieldSerialDoConhecimento, KTFieldCodigoDaSubsidiaria, KTFieldRemetenteId, KTFieldNomeDoDestinatario, KTCelularEntregaStatus) 
VALUES ('CNORRIS', GETDATE(), 'NUM-6667', '321', '789', 'CNPJ-3101065', 'Destinarario Plisken', 'AN');

DECLARE @conhecimentoParentRowId int;
SELECT  @conhecimentoParentRowId=KTRowID FROM dbo.Conhecimentos WHERE KTClientId='CNORRIS' AND KTFieldNumeroDoConhecimento='NUM-6667'
SELECT @conhecimentoParentRowId;
SELECT @conhecimentoParentRowId, * FROM  dbo.Conhecimentos WHERE KTClientId='CNORRIS';


INSERT INTO dbo.Notasfiscais (KTCreation, KTParentRowId, receiptNumber, receiptSerial, deliveryStatus) VALUES
(GETDATE(), @conhecimentoParentRowId, 1234, 2, 'AN'); -- Notas fiscais NF 1234 2

SELECT * FROM  dbo.Notasfiscais WHERE KTParentConhecimentoRowId=@conhecimentoParentRowId;

UPDATE dbo.Conhecimentos SET KTControleProntoParaEnviar=1 WHERE KTClientId='CNORRIS';
UPDATE dbo.Notasfiscais SET KTControleProntoParaEnviar=1 WHERE KTParentRowId=@conhecimentoParentRowId;


DELETE FROM  dbo.Conhecimentos WHERE KTClientId='CNORRIS';


      