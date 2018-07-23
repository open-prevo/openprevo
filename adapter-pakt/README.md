# OpenPrevo PAKT Adapter

Providing an adapter implementation for PAKT.

To configure the PAKT Environment and its DB2 datasource you have to provide values for the following properties:

```
pakt.datasource.username=
pakt.datasource.password=
pakt.datasource.jdbc-url=jdbc:db2://<host>:<port>/<database>
pakt.datasource.driver-class-name=com.ibm.db2.jcc.DB2Driver

pakt.cdMandant=
pakt.idUser=
pakt.serviceBaseUrl=http://<host>:<port>/PrevoServices/
pakt.retirementfunds.file=retirementFunds.yml
pakt.service.template.file.submitFzlVerwendung=submitFzlVerwendung.xml
```
