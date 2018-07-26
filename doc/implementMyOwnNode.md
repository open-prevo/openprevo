If none of the default implementation meet your requirements, feel
free to implement your own. 


## Implement your own node adapter

If you can run the default node implementation in your infrastructure, but need to integrate with your own
back end, you can implement your own adapter:

- interfaces are defined in the `node-api` module
- for an example, have a look at `adapter-example-spring` 
- specifically: the adapter must be provided via SPI (implement `ch.prevo.open.node.data.provider.ProviderFactory`, 
and provide a reference under `META-INF/services`)
- your adapter will be used by the default `node` implementation at runtime


## Implement a new node REST api
If, for instance, you don't have java available in your infrastructure, you can implement your own node:

- use the existing `node` module as an example
- specifically, the REST endpoints in the package `ch.prevo.open.node.api` must be implemented
- a separate `swagger` documentation is pending ([issue 178](https://github.com/open-prevo/openprevo/issues/178))
- alternatively, have a look at the swagger-ui available at runtime, e.g. by starting the demo and 
opening [swagger-ui](http://localhost:8851/swagger-ui.html)


## Contribute to OpenPrevo

If you'd like, [get in touch](https://gitter.im/open-prevo/Lobby) to contribute changes or new codeback to the OpenPrevo
project.
