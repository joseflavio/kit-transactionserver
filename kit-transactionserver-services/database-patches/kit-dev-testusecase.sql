USE [{dbname}_DBD];

INSERT INTO [dbo].[FormConhecimentos] (
ID, KTClientUserId, KTControleProntoParaEnviar, KTFieldNumeroDoConhecimento, KTFieldSerialDoConhecimento, KTFieldCodigoDaSubsidiaria, KTFieldRemetenteId, KTFieldNomeDoDestinatario) 
VALUES ('B59DFA1F-50CF-4610-BC54-AA1681827764', 'ALEX', 1, 'NUM-6667', '321', '789', 'CNPJ-3101065', 'Para Plisken');


INSERT INTO [dbo].[FormNotasfiscais] (
ID, PID, KTClientUserId, KTControleProntoParaEnviar, KTFieldReceiptNumber, KTFieldReceiptSerial) VALUES
(NEWID(), 'B59DFA1F-50CF-4610-BC54-AA1681827764', 'ALEX', 1, 1234, 2); -- Notas fiscais NF 1234 2






SELECT * FROM  dbo.Notasfiscais WHERE KTParentRowId=@conhecimentoParentRowId;

UPDATE dbo.Conhecimentos SET KTControleProntoParaEnviar=1 WHERE KTClientId='CNORRIS';
UPDATE dbo.Notasfiscais SET KTControleProntoParaEnviar=1 WHERE KTParentRowId=@conhecimentoParentRowId;


DELETE FROM  dbo.Conhecimentos WHERE KTClientId='CNORRIS';

-- FULL CLEAN UP
DELETE FROM from FormConhecimentos;


      