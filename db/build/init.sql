SET IDENTITY_INSERT [Role] ON;
GO

INSERT INTO [Role] (Id,Name) VALUES (1,'Administrator');
INSERT INTO [Role] (Id,Name) VALUES (2,'AdminApprover');
INSERT INTO [Role] (Id,Name) VALUES (3,'StandardUser');
INSERT INTO [Role] (Id,Name) VALUES (4,'DataManager');
SET IDENTITY_INSERT [Role] OFF;
GO

SET IDENTITY_INSERT [User] ON;
GO

INSERT INTO [User] (Id,Username,Email,FullName,RoleId) VALUES (1,'admin','lunarkid@copilots.topcoder.com','Dedy, Wahyudi',1);
Insert Into [User] (Id,Username,Email,FullName,RoleId) Values (2,'adminapprover','dedywahyuditc@gmail.com','Test User1',2);
Insert Into [User] (Id,Username,Email,FullName,RoleId) Values (3,'datamanager','d3dyw4hyud1@gmail.com','Test User2',4);

SET IDENTITY_INSERT [User] OFF;
GO

SET IDENTITY_INSERT [PasswordHistory] ON;
GO

INSERT INTO PasswordHistory (Id,UserId,Password,CreatedDate) VALUES (1,1,'$2a$10$hv3WcEfczNgpfis9bZe5j.MSFZGdu0KYXJzfcFSI3pNdY6u6To2S6',{ts '2018-09-01 16:21:29'});
INSERT INTO PasswordHistory (Id,UserId,Password,CreatedDate) VALUES (2,2,'$2a$10$hv3WcEfczNgpfis9bZe5j.MSFZGdu0KYXJzfcFSI3pNdY6u6To2S6',{ts '2018-09-01 16:21:29'});
INSERT INTO PasswordHistory (Id,UserId,Password,CreatedDate) VALUES (3,3,'$2a$10$hv3WcEfczNgpfis9bZe5j.MSFZGdu0KYXJzfcFSI3pNdY6u6To2S6',{ts '2018-09-01 16:21:29'});

SET IDENTITY_INSERT [PasswordHistory] OFF;
GO
