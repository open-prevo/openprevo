OpenPrevo consists of a central hub and two or more retirement fund nodes.

This demo is fully self containted and includes a hub.
As retirement fund you would normally not run a hub but only a node and connect to a hub provided by the OpenPrevo platform.

The nodes implement a very small API (TBD link to API as soon as https://github.com/open-prevo/openprevo/issues/125 is fixed) and can be easily adapted to any back end system you run.

In this example the back end is a simple set of XLS files.

- [system requirements](https://github.com/open-prevo/openprevo/blob/master/doc/gettingStarted.md)
- run startExcelDemo.(bat|sh)
- the hub and three nodes are started.
- each node runs its endpoints under a different port, see NodeXYZ/start.(bat|sh)
- each node has an input file with employer changes 
- the hub knows the nodes - see Hub/demo-nodes.yml - and polls them for employer changes
- on start up the test data contains matchings changes, so after a minute you should see the change XLS files written to each node directory
- the hub notifies a matching pair of employment termination / employment commencement only once. to reset its memory stop the hub and delete hub\openprevo.mv.db
- the nodes read their input files every time the hub polls them, so go ahead, change them ( i.e.) the dates and eventually see new notifications
- you can also see the changes in the API. I.e. change NodeBaloiseExcel/NodeBaloiseExcel-Input.xlsx and call http://localhost:8851/commencement-of-employment (baloise node runs on port 8851)


Implement your own node
https://github.com/open-prevo/openprevo/blob/master/doc/implementMyOwnNode.md
