package ch.prevo.open.node;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan({"ch.prevo.open.node","ch.prevo.pakt"})
@SpringBootApplication
public class NodeApplication {

    public static void main(String[] args) {
        SpringApplication.run(NodeApplication.class, args);
    }
}
