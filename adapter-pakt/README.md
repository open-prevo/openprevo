# OpenPrevo PAKT Adapter

Providing an adapter implementation for PAKT.

To configure the PAKT Environment and its DB2 datasource you have to provide values for the following properties:

```
-Dpakt.cdMandant=1234
-Dpakt.idUser=someuser
-Dpakt.serviceBaseUrl=http://localhost:8080/PrevoServices/services/
-Dpakt.datasource.username=db2inst1 
-Dpakt.datasource.password=db2inst1-pwd 
-Dpakt.datasource.jdbc-url=jdbc:db2://localhost:50000/pakt 
-Dpakt.datasource.driver-class-name=com.ibm.db2.jcc.DB2Driver
```
