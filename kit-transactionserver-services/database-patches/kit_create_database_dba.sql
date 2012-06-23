USE MASTER
GO

CREATE DATABASE [{dbname}]
ALTER DATABASE [{dbname}] SET READ_COMMITTED_SNAPSHOT ON
GO

USE [{dbname}]
GO

CREATE TABLE [dbo].[AuthenticatePassword_TMP] (
    [KTClientUserId] [nvarchar](32) NOT NULL PRIMARY KEY,
    [KTPassword] [nvarchar](50) NOT NULL,
    [KTActive] [bit] NOT NULL,
    [KTDeleted] [bit] NOT NULL,
    [KTLastUpdateDBTime] [datetime2] NOT NULL,
    [KTRowVersion] rowversion NOT NULL,
    
)
GO

INSERT INTO AuthenticatePassword_TMP (KTClientUserId, KTPassword, KTActive, KTDeleted, KTLastUpdateDBTime) SELECT KTClientId, KTPassword, 1, 0, GETDATE() FROM Authenticate

USE [KEEPIN_V02_DEVTEST_DBA]
INSERT INTO AuthenticatePassword (KTClientUserId, KTPassword, KTActive, KTDeleted, KTLastUpdateDBTime) SELECT KTClientId, KTPassword, 1, 0, GETDATE() FROM [KEEPIN_V01_DEMO01].[dbo].[Authenticate]
