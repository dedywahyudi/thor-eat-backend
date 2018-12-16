
CREATE TABLE [Organization]
(
    [Id] bigint IDENTITY(1,1) NOT NULL
  , [Name] nvarchar(200) NOT NULL
  , PRIMARY KEY ([Id])
  , CONSTRAINT AK_Name UNIQUE([Name])
);
GO

CREATE TABLE [Role]
(
    [Id] bigint IDENTITY(1,1) NOT NULL
  , [Name] nvarchar(200) NOT NULL
  , PRIMARY KEY ([Id])
);
GO


CREATE TABLE [User]
(
    [Id] bigint IDENTITY(1,1) NOT NULL
  , [Username] nvarchar(200) NOT NULL
  , [Email] nvarchar(200) NOT NULL
  , [FullName] nvarchar(200) NOT NULL
  , [RoleId] bigint NOT NULL
  , [AccessToken] nvarchar(2048)
  , [AccessTokenExpiresDate] datetime
  , PRIMARY KEY ([Id])
  , CONSTRAINT [FK_User_Role] FOREIGN KEY ([RoleId]) REFERENCES [Role] ([Id])
  , CONSTRAINT [AK_Username] UNIQUE([Username])
  , CONSTRAINT [AK_Email] UNIQUE([Email])
);
GO

CREATE TABLE [PasswordHistory]
(
    [Id] bigint IDENTITY(1,1) NOT NULL
  , [UserId] bigint NOT NULL
  , [Password] nvarchar(2048) NOT NULL
  , [CreatedDate] datetime NOT NULL
  , PRIMARY KEY ([Id])
  , CONSTRAINT [FK_PasswordHistory_User] FOREIGN KEY ([UserId]) REFERENCES [User] ([Id])
);
GO

CREATE TABLE [Division]
(
    [Id] nvarchar(9) NOT NULL
  , [Name] nvarchar(100) NOT NULL
  , [Region] nvarchar(50)
  , [ParentDivisionId] nvarchar(9)
  , PRIMARY KEY ([Id])
);
GO

CREATE TABLE [SubDivision]
(
    [Id] bigint IDENTITY(1,1) NOT NULL
  , [Name] nvarchar(100) NOT NULL
  , [DivisionId] nvarchar(9) NOT NULL
  , [ParentSubDivisionId] bigint
  , PRIMARY KEY ([Id])
  , CONSTRAINT [FK_SubDivision_Division] FOREIGN KEY ([DivisionId]) REFERENCES [Division] ([Id])
  , CONSTRAINT [FK_SubDivision_SubDivision] FOREIGN KEY ([ParentSubDivisionId]) REFERENCES [SubDivision] ([Id])
);
GO

CREATE TABLE [ProductLine]
(
    [Id] bigint IDENTITY(1,1) NOT NULL
  , [Name] nvarchar(200) NOT NULL
  , [DivisionId] nvarchar(9) NOT NULL
  , [SubDivisionId] nvarchar(9)
  , PRIMARY KEY ([Id])
  , CONSTRAINT [FK_ProductLine_Division] FOREIGN KEY ([DivisionId]) REFERENCES [Division] ([Id])
  , CONSTRAINT [FK_ProductLine_SubDivision] FOREIGN KEY ([SubDivisionId]) REFERENCES [Division] ([Id])
);
GO

CREATE TABLE [ProductLineContact]
(
    [ProductLineId] bigint NOT NULL
  , [Id] bigint IDENTITY(1,1) NOT NULL
  , [ContactEmail] nvarchar(200)
  , PRIMARY KEY ([Id])
  , CONSTRAINT [FK_ProductLineContact_ProductLine] FOREIGN KEY ([ProductLineId]) REFERENCES [ProductLine] ([Id])
);
GO

CREATE TABLE [Standard]
(
    [Id] bigint IDENTITY(1,1) NOT NULL
  , [OrganizationId] bigint NOT NULL
  , [Name] nvarchar(200) NOT NULL
  , [Description] nvarchar(max)
  , [Edition] nvarchar(200)
  , [Date] date
  , [CreatedBy] bigint
  , [CreatedDate] datetime
  , PRIMARY KEY ([Id])
  , CONSTRAINT [FK_Standard_Organization] FOREIGN KEY ([OrganizationId]) REFERENCES [Organization] ([Id])
  , CONSTRAINT [FK_Standard_User] FOREIGN KEY ([CreatedBy]) REFERENCES [User] ([Id])
);
GO

CREATE TABLE [StandardDivision]
(
    [Id] bigint IDENTITY(1,1) NOT NULL
  , [StandardId] bigint
  , [DivisionId] nvarchar(9) NOT NULL
  , [SubDivisionId] nvarchar(9)
  , [ProductLineId] bigint
  , [StandardParticipant] nvarchar(200)
  , [CriticalToBusiness] bit NOT NULL
  , [Comment] nvarchar(max)
  , [IsApproved] bit NOT NULL
  , PRIMARY KEY ([Id])
  , CONSTRAINT [FK_StandardDivision_Standard] FOREIGN KEY ([StandardId]) REFERENCES [Standard] ([Id])
  , CONSTRAINT [FK_StandardDivision_Division] FOREIGN KEY ([DivisionId]) REFERENCES [Division] ([Id])
  , CONSTRAINT [FK_StandardDivision_SubDivision] FOREIGN KEY ([SubDivisionId]) REFERENCES [Division] ([Id])
  , CONSTRAINT [FK_StandardDivision_ProductLine] FOREIGN KEY ([ProductLineId]) REFERENCES [ProductLine] ([Id])
);
GO

CREATE TABLE [StandardDivisionContact]
(
    [Id] bigint IDENTITY(1,1) NOT NULL
  , [StandardDivisionId] bigint NOT NULL
  , [ContactEmail] nvarchar(200)
  , PRIMARY KEY ([Id])
  , CONSTRAINT [FK_StandardDivisionContact_StandardDivision] FOREIGN KEY ([StandardDivisionId]) REFERENCES [StandardDivision] ([Id])
);
GO

CREATE TABLE [CnSIssue]
(
    [Id] bigint IDENTITY(1,1) NOT NULL
  , [DivisionId] nvarchar(9) NOT NULL
  , [Description] nvarchar(max)
  , [ActionPlan] nvarchar(max)
  , [PersonsResponsible] nvarchar(max)
  , [EstimatedCompletionDate] date
  , [EngineerReviewDate] date
  , [EngineerLeader] nvarchar(200)
  , [FinancialImpact] nvarchar(max)
  , [ImpactSummary] nvarchar(max)
  , [VPGMName] nvarchar(200)
  , [CriticalRoleLeadership] bit
  , [SuccessionPlan] nvarchar(max)
  , [OffensiveDefensive] nvarchar(max)
  , [ExternalPartners] nvarchar(max)
  , [VPGMReviewDate] date
  , [Priority] int
  , [Region] nvarchar(50)
  , [RoadmapAlignment] nvarchar(max)
  , [StandardId] bigint DEFAULT NULL
  , [CreatorName] nvarchar(800)
  , [CreatedBy] nvarchar(200)
  , [CreatedDate] datetime
  , [Read] bit
  , PRIMARY KEY ([Id])
  , CONSTRAINT [FK_CnSIssue_Division] FOREIGN KEY ([DivisionId]) REFERENCES [Division] ([Id])
  , CONSTRAINT [FK_CnsIssue_Standard] FOREIGN KEY ([StandardId]) REFERENCES [Standard] ([Id])
);
GO

CREATE TABLE [History]
(
    [Id] bigint IDENTITY(1,1) NOT NULL
  , [RecordId] bigint NOT NULL
  , [RecordName] nvarchar(max)
  , [Operation] nchar(6)  -- "Insert", "Update", or "Delete"
  , [RecordType] nchar(8) -- "Standard" or "CnSIssue"
  , [UserId] bigint
  , [ModifiedDate] datetime
  , PRIMARY KEY ([Id])
  , CONSTRAINT [FK_History_User] FOREIGN KEY ([UserId]) REFERENCES [User] ([Id])
);
GO

CREATE TABLE [PendingStandard]
(
    [Id] bigint IDENTITY(1,1) NOT NULL
  , [OrganizationId] bigint
  , [Name] nvarchar(200)
  , [Description] nvarchar(max)
  , [Edition] nvarchar(200)
  , [Date] date
  , [CreatedBy] bigint
  , [CreatedDate] datetime
  , [NewDivisionId] bigint
  , [OldDivisionId] bigint
  , [DivisionId] nvarchar(200)
  , PRIMARY KEY ([Id])
  , CONSTRAINT [FK_PendingStandard_Organization] FOREIGN KEY ([OrganizationId]) REFERENCES [Organization] ([Id])
  , CONSTRAINT [FK_PendingStandard_User] FOREIGN KEY ([CreatedBy]) REFERENCES [User] ([Id])
);
GO

CREATE TABLE [ChangeRequest]
(
    [Id] bigint IDENTITY(1,1) NOT NULL
  , [requestedByUserId] bigint NOT NULL
  , [requestedDate] datetime NOT NULL
  , [type] nchar(6)  -- "Insert", "Update", or "Delete"
  , [RecordType] nchar(8) -- "Standard", "CnSIssue" or "Org"
  , [standardId] bigint
  , [pendingStandardId] bigint NOT NULL
  , PRIMARY KEY ([Id])
  , CONSTRAINT [FK_ChangeRequest_User] FOREIGN KEY ([requestedByUserId]) REFERENCES [User] ([Id])
  , CONSTRAINT [FK_ChangeRequest_PendingStandard] FOREIGN KEY ([pendingStandardId]) REFERENCES [PendingStandard] ([Id])
);
GO
