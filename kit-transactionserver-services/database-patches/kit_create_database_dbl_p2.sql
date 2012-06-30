USE MASTER
GO

CREATE DATABASE [{dbname}_DBL]
ALTER DATABASE [{dbname}_DBL] SET READ_COMMITTED_SNAPSHOT ON
GO


USE [{dbname}_DBL]
CREATE TABLE [dbo].[LogFormFieldDate](
    [KTFormFieldDateRowId] [UNIQUEIDENTIFIER] NOT NULL DEFAULT NEWID() PRIMARY KEY NONCLUSTERED,
    [KTInsertDBTime] [datetime2] NOT NULL DEFAULT SYSUTCDATETIME(),
    [KTFormType] [nchar](2) NOT NULL,
    [KTFormRowId] [bigint] NOT NULL,
    [KTFormFieldName] [nchar](32) NOT NULL,
    [KTFormFieldValue] [datetime2] NOT NULL,
    [KTFormFieldDebug] [nvarchar](2048) NOT NULL    
)
CREATE CLUSTERED INDEX CIDX_FORMFIELDDATE_INSERTDBTIME ON [LogFormFieldDate](KTInsertDBTime);
CREATE NONCLUSTERED INDEX NIDX_FORMFIELDDATE_UNIQUEFORM ON [LogFormFieldDate](KTFormRowId, KTFormType);


CREATE TABLE [dbo].[LogFormFieldString32](
    [KTFormFieldDateRowId] [UNIQUEIDENTIFIER] NOT NULL DEFAULT NEWID() PRIMARY KEY NONCLUSTERED,
    [KTInsertDBTime] [datetime2] NOT NULL DEFAULT SYSUTCDATETIME(),
    [KTFormType] [nchar](2) NOT NULL,
    [KTFormRowId] [bigint] NOT NULL,
    [KTFormFieldName] [nchar](32) NOT NULL,
    [KTFormFieldValue] [nchar](64) NOT NULL,
    [KTFormFieldDebug] [nvarchar](2048) NOT NULL    
)
CREATE CLUSTERED INDEX CIDX_FORMFIELDSTRING32_INSERTDBTIME ON [LogFormFieldString32](KTInsertDBTime);
CREATE NONCLUSTERED INDEX NIDX_FORMFIELDSTRING32_UNIQUEFORM ON [LogFormFieldString32](KTFormRowId, KTFormType);


