USE [{dbname}_DBL]
CREATE TABLE [dbo].[FormConhecimentos](

    [ID] [uniqueidentifier] NOT NULL UNIQUE NONCLUSTERED, 
    
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
    [KTFlagRemovido] [bit] NOT NULL DEFAULT 0,
    [KTFlagRemovidoUpdateDBTime] [datetime2] NULL,
        
    [KTInsertDBTime] [datetime2] NOT NULL DEFAULT SYSUTCDATETIME(),
    [KTLastUpdateDBTime] [datetime2] NOT NULL DEFAULT SYSUTCDATETIME(),
    [KTRowVersion] rowversion NOT NULL
    
) ON [PRIMARY]

CREATE CLUSTERED INDEX CIDX_FORMCONHECIMENTOS_LASTUPDATEDBTIME ON [FormConhecimentos](KTLastUpdateDBTime);
CREATE NONCLUSTERED INDEX NIDX_FORMCONHECIMENTOS_CLIENTUSERID ON [FormConhecimentos](KTClientUserId);
CREATE NONCLUSTERED INDEX NIDX_FORMCONHECIMENTOS_VALIDS ON [FormConhecimentos](KTFlagRemovido, KTControleProntoParaEnviar);
CREATE NONCLUSTERED INDEX NIDX_FORMCONHECIMENTOS_RECEBIDO ON [FormConhecimentos](KTFlagRecebido);
CREATE NONCLUSTERED INDEX NIDX_FORMCONHECIMENTOS_LIDO ON [FormConhecimentos](KTFlagLido);
CREATE NONCLUSTERED INDEX NIDX_FORMCONHECIMENTOS_EDITADO ON [FormConhecimentos](KTFlagEditado);
CREATE NONCLUSTERED INDEX NIDX_FORMCONHECIMENTOS_HISTORICO ON [FormConhecimentos](KTFlagRemovido);


CREATE TABLE [dbo].[FormNotasfiscais](

    [ID] [uniqueidentifier] NOT NULL UNIQUE NONCLUSTERED,
    [PID] [uniqueidentifier] NOT NULL,
    
    [KTRowId] [bigint] NOT NULL PRIMARY KEY NONCLUSTERED IDENTITY,
    [KTClientUserId] [nvarchar](32) NOT NULL,
    
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
    [KTFlagRemovido] [bit] NOT NULL DEFAULT 0,
    [KTFlagRemovidoUpdateDBTime] [datetime2] NULL,
     
    [KTInsertDBTime] [datetime2] NOT NULL DEFAULT SYSUTCDATETIME(),
    [KTLastUpdateDBTime] [datetime2] NOT NULL DEFAULT SYSUTCDATETIME(),
    [KTRowVersion] rowversion NOT NULL
    
) ON [PRIMARY]

CREATE CLUSTERED INDEX CIDX_FORMNOTASFISCAIS_LASTUPDATEDBTIME ON [FormNotasfiscais](KTLastUpdateDBTime);
CREATE NONCLUSTERED INDEX NIDX_FORMNOTASFISCAIS_PID ON [FormNotasfiscais](PID);
CREATE NONCLUSTERED INDEX NIDX_FORMNOTASFISCAIS_CLIENTUSERID ON [FormNotasfiscais](KTClientUserId);
CREATE NONCLUSTERED INDEX NIDX_FORMNOTASFISCAIS_VALIDS ON [FormNotasfiscais](KTFlagRemovido, KTControleProntoParaEnviar);
CREATE NONCLUSTERED INDEX NIDX_FORMNOTASFISCAIS_RECEBIDO ON [FormNotasfiscais](KTFlagRecebido);
CREATE NONCLUSTERED INDEX NIDX_FORMNOTASFISCAIS_LIDO ON [FormNotasfiscais](KTFlagLido);
CREATE NONCLUSTERED INDEX NIDX_FORMNOTASFISCAIS_EDITADO ON [FormNotasfiscais](KTFlagEditado);
CREATE NONCLUSTERED INDEX NIDX_FORMNOTASFISCAIS_HISTORICO ON [FormNotasfiscais](KTFlagRemovido);





    [KTCelularDataPrimeiraLeitura] [datetime2] NULL,
    [KTCelularEntregaData] [datetime2] NULL,
    [KTCelularEntregaDataString] [nvarchar](48) NULL,
    [KTCelularEntregaStatus] [nvarchar](8) NULL DEFAULT 'AN',
    [KTCelularEntregaUpdateDBTime] [datetime2] NULL,
    
       [KTCelularDataPrimeiraLeitura] [datetime2] NULL,
    [KTCelularEntregaData] [datetime2] NULL,
    [KTCelularEntregaDataString] [nvarchar](48) NULL,
    [KTCelularEntregaStatus] [nvarchar](8) NOT NULL DEFAULT 'AN',
    [KTCelularEntregaUpdateDBTime] [datetime2] NULL, 
    
    
    
    
    