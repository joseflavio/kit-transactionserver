USE MASTER
GO

CREATE DATABASE [{KEEPIN_V02_DEVTEST_DBG}]
ALTER DATABASE [{KEEPIN_V02_DEVTEST_DBG}] SET READ_COMMITTED_SNAPSHOT ON
GO

USE KEEPIN_V02_DEVTEST_DBG

CREATE TABLE [dbo].[ActivityGpsHistory] (
    [KTInsertDBTime] [datetime2] NOT NULL DEFAULT SYSUTCDATETIME(),       
    [KTClientUserId] [nvarchar](32) NOT NULL,
    [KTClientInstallIdAB] [nchar](33) NOT NULL,
    [KTConnectionId] [nchar](19) NOT NULL,
    [KTAvailable] [bit] NOT NULL,
    [KTLat] [int] NOT NULL,
    [KTLng] [int] NOT NULL,
    [KTAccuracy] [int] NOT NULL,
    [KTActivityLogicalClock] [int] NOT NULL,
    [KTActivityTime] [datetime2] NOT NULL
)

CREATE NONCLUSTERED INDEX NIDX_ACTIVITYGPSHISTORY_KTCONNECTIONID ON [ActivityGpsHistory](KTConnectionId);
CREATE NONCLUSTERED INDEX NIDX_ACTIVITYGPSHISTORY_KTCLIENTUSERID ON [ActivityGpsHistory](KTClientUserId);
CREATE NONCLUSTERED INDEX NIDX_ACTIVITYGPSHISTORY_KTCLIENTINSTALLIDAB ON [ActivityGpsHistory](KTClientInstallIdAB);
CREATE NONCLUSTERED INDEX NIDX_ACTIVITYGPSHISTORY_KTAVAILABLE ON [ActivityGpsHistory](KTAvailable);
CREATE NONCLUSTERED INDEX NIDX_ACTIVITYGPSHISTORY_KTACCURACY ON [ActivityGpsHistory](KTAccuracy);
CREATE NONCLUSTERED INDEX NIDX_ACTIVITYGPSHISTORY_KTACTIVITYLOGICALCLOCK ON [ActivityGpsHistory](KTActivityLogicalClock);
CREATE CLUSTERED INDEX CIDX_ACTIVITYGPSHISTORY_KTLATKTLNG ON [ActivityGpsHistory](KTLat,KTLng);



CREATE TABLE [dbo].[ActivityGpsLast] (           
    [KTClientUserId] [nvarchar](32) NOT NULL,
    [KTClientInstallIdAB] [nchar](33) NOT NULL,
    [KTConnectionId] [nchar](19) NOT NULL,
    [KTAvailable] [bit] NOT NULL,
    [KTLat] [int] NOT NULL,
    [KTLng] [int] NOT NULL,
    [KTAccuracy] [int] NOT NULL,
    [KTActivityLogicalClock] [int] NOT NULL,
    [KTActivityTime] [datetime2] NOT NULL,
    [KTLastUpdateDBTime] [datetime2] NOT NULL DEFAULT SYSUTCDATETIME(),
    [KTRowVersion] rowversion NOT NULL
    CONSTRAINT PK_ACTIVITYGPSLAST PRIMARY KEY NONCLUSTERED ([KTClientUserId], [KTClientInstallIdAB])
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
