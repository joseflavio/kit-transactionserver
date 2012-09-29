USE MASTER
GO

CREATE DATABASE [{dbname}]
ALTER DATABASE [{dbname}] SET READ_COMMITTED_SNAPSHOT ON
GO

USE [{dbname}]
GO

USE KEEPIN_V02_DEVTEST_DBG

CREATE TABLE [dbo].[ActivityGpsHistory] (
    [KTInsertDBTime] [datetime2] NOT NULL DEFAULT SYSUTCDATETIME(),
    [KTConnectionId] [nchar](19) NOT NULL,    
    [KTClientUserId] [nvarchar](32) NOT NULL,
    [KTClientInstallIdAB] [nchar](33) NOT NULL,
    [KTAvailable] [bit] NOT NULL,
    [KTLat] [int] NOT NULL,
    [KTLng] [int] NOT NULL,
    [KTAccuracy] [int] NOT NULL,
    [KTActivityLogicalClock] [int] NOT NULL,
    [KTActivityTime] [datetime2] NOT NULL
)




CREATE TABLE [dbo].[ActivityGps] (
    [KTInsertDBTime] [datetime2] NOT NULL DEFAULT SYSUTCDATETIME(),
    [KTConnectionId] [nchar](19) NOT NULL PRIMARY KEY NONCLUSTERED,    
    [KTClientUserId] [nvarchar](32) NOT NULL PRIMARY KEY NONCLUSTERED,
    [KTClientInstallIdAB] [nchar](33) NOT NULL,
    
    [KTActive] [bit] NOT NULL,
    [KTDeleted] [bit] NOT NULL,
    [KTLastUpdateDBTime] [datetime2] NOT NULL DEFAULT SYSUTCDATETIME(),
    [KTRowVersion] rowversion NOT NULL,    
)


CREATE CLUSTERED INDEX CIDX_AUTHENTICATEPASSWORD_LASTUPDATEDBTIME ON [AuthenticatePassword](KTLastUpdateDBTime);
CREATE NONCLUSTERED INDEX NIDX_AUTHENTICATEPASSWORD_ACTIVEDELETED ON [AuthenticatePassword](KTActive, KTDeleted);
CREATE NONCLUSTERED INDEX NIDX_AUTHENTICATEPASSWORDS_PASSWORD ON [AuthenticatePassword](KTPassword);
CREATE NONCLUSTERED INDEX NIDX_AUTHENTICATEPASSWORDS_ROWVERSION ON [AuthenticatePassword](KTRowVersion);
GO
