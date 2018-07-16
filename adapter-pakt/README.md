# OpenPrevo PAKT Adapter

Providing an adapter implementation for PAKT.

To configure the DB2 datasource you have to provide values for the following properties:

```
-Dpakt.datasource.username=db2inst1 
-Dpakt.datasource.password=db2inst1-pwd 
-Dpakt.datasource.jdbc-url=jdbc:db2://localhost:50000/pakt 
-Dpakt.datasource.driver-class-name=com.ibm.db2.jcc.DB2Driver
```

To configure the PAKT environment you have to provide values for the following properties:

```
-Dpakt.env.cdMandant=1234
-Dpakt.env.idUser=someuser
-Dpakt.env.serviceBaseUrl=http://localhost:8080/PrevoServices/services/
```