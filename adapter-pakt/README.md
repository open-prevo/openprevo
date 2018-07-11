# OpenPrevo PAKT Adapter

Providing an adapter implementation for PAKT.

To configure the DB2 datasource you have to provide values for the following properties:

```
-Dpakt.datasource.username=db2inst1 
-Dpakt.datasource.password=db2inst1-pwd 
-Dpakt.datasource.jdbc-url=jdbc:db2://localhost:50000/pakt 
-Dpakt.datasource.driver-class-name=com.ibm.db2.jcc.DB2Driver
```