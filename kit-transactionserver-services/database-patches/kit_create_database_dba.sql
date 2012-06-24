USE MASTER
GO

CREATE DATABASE [{dbname}]
ALTER DATABASE [{dbname}] SET READ_COMMITTED_SNAPSHOT ON
GO

USE [{dbname}]
GO

CREATE TABLE [dbo].[AuthenticatePassword] (
    [KTClientUserId] [nvarchar](32) NOT NULL PRIMARY KEY NONCLUSTERED,
    [KTPassword] [nvarchar](50) NOT NULL,
    [KTActive] [bit] NOT NULL,
    [KTDeleted] [bit] NOT NULL,
    [KTLastUpdateDBTime] [datetime2] NOT NULL,
    [KTRowVersion] rowversion NOT NULL,
    
)
CREATE CLUSTERED INDEX CIDX_AUTHENTICATEPASSWORD_LASTUPDATEDBTIME ON [AuthenticatePassword](KTLastUpdateDBTime);
CREATE NONCLUSTERED INDEX NIDX_AUTHENTICATEPASSWORD_ACTIVEDELETED ON [AuthenticatePassword](KTActive, KTDeleted);
CREATE NONCLUSTERED INDEX NIDX_AUTHENTICATEPASSWORDS_PASSWORD ON [AuthenticatePassword](KTPassword);
CREATE NONCLUSTERED INDEX NIDX_AUTHENTICATEPASSWORDS_ROWVERSION ON [AuthenticatePassword](KTRowVersion);
GO


--------------------------------------------------------------

INSERT INTO [dbo].[AuthenticatePassword] (KTClientUserId, KTPassword, KTActive, KTDeleted, KTLastUpdateDBTime) SELECT KTClientId, KTPassword, 1, 0, GETDATE() FROM [{origin_dbname}].[dbo].[Authenticate]


