USE "TESTDEV_JOSEFLAVIO_KEEPIN3_MIRA_DBV20111129";

SELECT * FROM [dbo].[Authenticate] where [KTClientId]='TELES'

UPDATE dbo.Conhecimentos SET KTControleProntoParaEnviar=1

SELECT TOP 1000 * FROM [dbo].[Conhecimentos] where [KTFlagHistorico]=0 and [KTControleDeveDeletar]=0

SELECT KTRowId, KTClientId, KTStatus, KTFlagRecebido, KTFlagLido, KTFlagEditado, knowledgeNumber, knowledgeSerial, subsidiaryCode, senderId, recipientName, deliveryStatus, deliveryDate FROM dbo.Conhecimentos WHERE KTClientId='adaniel' and KTFlagHistorico=0 and KTControleProntoParaEnviar=1

