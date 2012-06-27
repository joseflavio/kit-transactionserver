USE MASTER
GO

CREATE DATABASE [{dbname}_DBL]
ALTER DATABASE [{dbname}_DBL] SET READ_COMMITTED_SNAPSHOT ON
GO


USE [{dbname}_DBL]
CREATE TABLE [dbo].[FormFieldDate](
    [KTFormFieldDateRowId] [UNIQUEIDENTIFIER] NOT NULL DEFAULT NEWID() PRIMARY KEY NONCLUSTERED,
    [KTInsertDBTime] [datetime2] NOT NULL DEFAULT GETDATE(),
    [KTFormType] [nchar](2) NOT NULL,
    [KTFormRowId] [bigint] NOT NULL,
    [KTFormFieldName] [nchar](32) NOT NULL,
    [KTFormFieldValue] [datetime2] NOT NULL,
    [KTFormFieldDebug] [nvarchar](2048) NOT NULL    
)
CREATE CLUSTERED INDEX CIDX_FORMFIELDDATE_INSERTDBTIME ON [FormFieldDate](KTInsertDBTime);
CREATE NONCLUSTERED INDEX NIDX_FORMFIELDDATE_UNIQUEFORM ON [FormFieldDate](KTFormRowId, KTFormType);



CREATE TABLE [dbo].[FormFieldString32](
    [KTFormFieldDateRowId] [UNIQUEIDENTIFIER] NOT NULL DEFAULT NEWID() PRIMARY KEY NONCLUSTERED,
    [KTInsertDBTime] [datetime2] NOT NULL DEFAULT GETDATE(),
    [KTFormType] [nchar](2) NOT NULL,
    [KTFormRowId] [bigint] NOT NULL,
    [KTFormFieldName] [nchar](32) NOT NULL,
    [KTFormFieldValue] [nchar](64) NOT NULL,
    [KTFormFieldDebug] [nvarchar](2048) NOT NULL    
)
CREATE CLUSTERED INDEX CIDX_FORMFIELDSTRING32_INSERTDBTIME ON [FormFieldString32](KTInsertDBTime);
CREATE NONCLUSTERED INDEX NIDX_FORMFIELDSTRING32_UNIQUEFORM ON [FormFieldString32](KTFormRowId, KTFormType);






USE [{dbname}_DBL]
CREATE TABLE [dbo].[FormConhecimentos](

    [KTRowId] [bigint] NOT NULL PRIMARY KEY NONCLUSTERED IDENTITY,
    [KTClientUserId] [nvarchar](32) NOT NULL,    
    [KTControleProntoParaEnviar] [bit] NOT NULL DEFAULT 0,
    [KTControleDeveDeletar] [bit] NOT NULL DEFAULT 0,
    
    [KTFieldNumeroDoConhecimento] [nvarchar](8) NOT NULL,
    [KTFieldSerialDoConhecimento] [nvarchar](3) NOT NULL,
    [KTFieldCodigoDaSubsidiaria] [nvarchar](3) NOT NULL,
    [KTFieldRemetenteId] [nvarchar](14) NOT NULL,
    [KTFieldNomeDoDestinatario] [nvarchar](20) NOT NULL,
           
    [KTFlagRecebido] [bit] NOT NULL DEFAULT 0,
    [KTFlagRecebidoUpdateDBTime] [datetime2] NULL,
    [KTFlagLido] [bit] NOT NULL DEFAULT 0,
    [KTFlagLidoUpdateDBTime] [datetime2] NULL,
    [KTFlagEditado] [bit] NOT NULL DEFAULT 0,
    [KTFlagEditadoUpdateDBTime] [datetime2] NULL,
    [KTFlagHistorico] [bit] NOT NULL DEFAULT 0,
    [KTFlagHistoricoUpdateDBTime] [datetime2] NULL,
    
    [KTCelularDataPrimeiraLeitura] [datetime2] NULL,
    [KTCelularEntregaData] [datetime2] NULL,
    [KTCelularEntregaDataString] [nvarchar](48) NULL,
    [KTCelularEntregaStatus] [nvarchar](8) NULL DEFAULT 'AN',
    [KTCelularEntregaUpdateDBTime] [datetime2] NULL,
    
    [KTInsertDBTime] [datetime2] NOT NULL DEFAULT GETDATE(),
    [KTLastUpdateDBTime] [datetime2] NOT NULL DEFAULT GETDATE(),
    [KTRowVersion] rowversion NOT NULL
    
) ON [PRIMARY]

CREATE CLUSTERED INDEX CIDX_FORMCONHECIMENTOS_LASTUPDATEDBTIME ON [FormConhecimentos](KTLastUpdateDBTime);
CREATE NONCLUSTERED INDEX NIDX_FORMCONHECIMENTOS_VALIDS ON [FormConhecimentos](KTFlagHistorico, KTControleProntoParaEnviar);
CREATE NONCLUSTERED INDEX NIDX_FORMCONHECIMENTOS_RECEBIDO ON [FormConhecimentos](KTFlagRecebido);
CREATE NONCLUSTERED INDEX NIDX_FORMCONHECIMENTOS_LIDO ON [FormConhecimentos](KTFlagLido);
CREATE NONCLUSTERED INDEX NIDX_FORMCONHECIMENTOS_EDITADO ON [FormConhecimentos](KTFlagEditado);
CREATE NONCLUSTERED INDEX NIDX_FORMCONHECIMENTOS_HISTORICO ON [FormConhecimentos](KTFlagHistorico);




CREATE TABLE [dbo].[FormNotasfiscais](
    
    [KTRowId] [bigint] NOT NULL PRIMARY KEY NONCLUSTERED IDENTITY,
    [KTClientUserId] [nvarchar](32) NOT NULL,
    [KTParentConhecimentoRowId] [bigint] NOT NULL,
    [KTControleProntoParaEnviar] [bit] NOT NULL DEFAULT 0,
    [KTControleDeveDeletar] [bit] NOT NULL DEFAULT 0,
    
    [KTFieldReceiptNumber] [varchar](16) NOT NULL,
    [KTFieldReceiptSerial] [varchar](8) NOT NULL,
    
    [KTFlagRecebido] [bit] NOT NULL DEFAULT 0,
    [KTFlagRecebidoUpdateDBTime] [datetime2] NULL,
    [KTFlagLido] [bit] NOT NULL DEFAULT 0,
    [KTFlagLidoUpdateDBTime] [datetime2] NULL,
    [KTFlagEditado] [bit] NOT NULL DEFAULT 0,
    [KTFlagEditadoUpdateDBTime] [datetime2] NULL,
    [KTFlagHistorico] [bit] NOT NULL DEFAULT 0,
    [KTFlagHistoricoUpdateDBTime] [datetime2] NULL,
    
    [KTCelularDataPrimeiraLeitura] [datetime2] NULL,
    [KTCelularEntregaData] [datetime2] NULL,
    [KTCelularEntregaDataString] [nvarchar](48) NULL,
    [KTCelularEntregaStatus] [nvarchar](8) NOT NULL DEFAULT 'AN',
    [KTCelularEntregaUpdateDBTime] [datetime2] NULL,
    
    [KTInsertDBTime] [datetime2] NOT NULL DEFAULT GETDATE(),
    [KTLastUpdateDBTime] [datetime2] NOT NULL DEFAULT GETDATE(),
    [KTRowVersion] rowversion NOT NULL
    
) ON [PRIMARY]
