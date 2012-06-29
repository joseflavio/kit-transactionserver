USE MASTER
GO

CREATE DATABASE [{dbname}_DBL]
ALTER DATABASE [{dbname}_DBL] SET READ_COMMITTED_SNAPSHOT ON
GO

USE [{dbname}_DBL]
CREATE TABLE [dbo].[LogConexoesIniciadas](
    [KTInsertDBTime] [datetime2] NOT NULL DEFAULT SYSUTCDATETIME(),
    [KTConnectionId] [nchar](19) NOT NULL PRIMARY KEY NONCLUSTERED,
    [KTClientUserId] [nvarchar](32) NOT NULL,
    [KTClientInstallIdAB] [nchar](33) NOT NULL,
    [KTClientNetworkAddress] [nchar](42) NOT NULL,    
    [KTConnectionAuthStatus] [smallint] NOT NULL
) ON [PRIMARY]

CREATE CLUSTERED INDEX CIDX_LOGCONEXOESINICIADAS ON [LogConexoesIniciadas](KTInsertDBTime);
CREATE NONCLUSTERED INDEX NIDX_LOGCONEXOESINICIADAS_CLIENTUSERID ON [LogConexoesIniciadas](KTClientUserId);
CREATE NONCLUSTERED INDEX NIDX_LOGCONEXOESINICIADAS_CONNAUTHSTATUS ON [LogConexoesIniciadas](KTConnectionAuthStatus);
GO

USE [{dbname}_DBL]
CREATE TABLE [dbo].[LogConexoesFinalizadas](
    [KTInsertDBTime] [datetime2] NOT NULL DEFAULT SYSUTCDATETIME(),    
    [KTConnectionId] [nchar](19) NOT NULL PRIMARY KEY NONCLUSTERED,
    [KTClientUserId] [nvarchar](32) NOT NULL,
    [KTConnectionEndStatus] [smallint] NOT NULL,
    [KTConnectionTotalTime] [int] NOT NULL,
    [KTFormsSent] [int] NOT NULL,
    [KTFormsReceived] [int] NOT NULL,
    [KTBytesSent] [int] NOT NULL,
    [KTBytesReceived] [int] NOT NULL,
    [KTPrimitivesSent] [int] NOT NULL,
    [KTPrimitivesReceived] [int] NOT NULL
) ON [PRIMARY]

CREATE CLUSTERED INDEX CIDX_LOGCONEXOESFINALIZADAS ON [LogConexoesFinalizadas](KTInsertDBTime);
CREATE NONCLUSTERED INDEX NIDX_LOGCONEXOESFINALIZADAS_CLIENTUSERID ON [LogConexoesFinalizadas](KTConnectionEndStatus);
CREATE NONCLUSTERED INDEX NIDX_LOGCONEXOESFINALIZADAS_CONNAUTHSTATUS ON [LogConexoesFinalizadas](KTConnectionEndStatus);
GO