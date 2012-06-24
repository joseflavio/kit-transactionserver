USE MASTER
GO

CREATE DATABASE [{dbname}_DBD]
ALTER DATABASE [{dbname}_DBD] SET READ_COMMITTED_SNAPSHOT ON
GO

USE [{dbname}_DBD]
CREATE TABLE [dbo].[FormConhecimentos](
    [KTRowId] [bigint] NOT NULL PRIMARY KEY NONCLUSTERED,
    [KTClientUserId] [nvarchar](32) NOT NULL,    
    [KTFieldNumeroDoConhecimento] [nvarchar](8) NOT NULL,
    [KTFieldSerialDoConhecimento] [nvarchar](3) NOT NULL,
    [KTFieldCodigoDaSubsidiaria] [nvarchar](3) NOT NULL,
    [KTFieldRemetenteId] [nvarchar](14) NOT NULL,
    [KTFieldNomeDoDestinatario] [nvarchar](20) NOT NULL,
    [KTCelularEntregaStatus] [nvarchar](8) NULL,    
    [KTControleProntoParaEnviar] [bit] NOT NULL,
    [KTControleDeveDeletar] [bit] NOT NULL,    
    [KTFlagRecebido] [bit] NOT NULL,
    [KTFlagRecebidoUpdateDBTime] [datetime2] NULL,
    [KTFlagLido] [bit] NOT NULL,
    [KTFlagLidoUpdateDBTime] [datetime2] NULL,
    [KTFlagEditado] [bit] NOT NULL,
    [KTFlagEditadoUpdateDBTime] [datetime2] NULL,
    [KTFlagHistorico] [bit] NOT NULL,
    [KTFlagHistoricoUpdateDBTime] [datetime2] NULL,
    [KTCelularEntregaData] [datetime2] NULL,
    [KTCelularEntregaDataString] [nchar](48) NULL,
    [KTCelularEntregaUpdateDBTime] [datetime2] NULL,    
    [KTCelularDataPrimeiraLeitura] [datetime2] NULL,
    [KTInsertDBTime] [datetime2] NOT NULL,
    [KTLastUpdateDBTime] [datetime2] NOT NULL,
    [KTRowVersion] rowversion NOT NULL
) ON [PRIMARY]

CREATE CLUSTERED INDEX CIDX_FORMCONHECIMENTOS_LASTUPDATEDBTIME ON [FormConhecimentos](KTLastUpdateDBTime);
CREATE NONCLUSTERED INDEX NIDX_FORMCONHECIMENTOS_VALIDS ON [FormConhecimentos](KTFlagHistorico, KTControleProntoParaEnviar);
CREATE NONCLUSTERED INDEX NIDX_FORMCONHECIMENTOS_RECEBIDO ON [FormConhecimentos](KTFlagRecebido);
CREATE NONCLUSTERED INDEX NIDX_FORMCONHECIMENTOS_LIDO ON [FormConhecimentos](KTFlagLido);
CREATE NONCLUSTERED INDEX NIDX_FORMCONHECIMENTOS_EDITADO ON [FormConhecimentos](KTFlagEditado);
CREATE NONCLUSTERED INDEX NIDX_FORMCONHECIMENTOS_HISTORICO ON [FormConhecimentos](KTFlagHistorico);

