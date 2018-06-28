package ch.prevo.open.node.adapter;

import static org.assertj.core.api.Java6Assertions.assertThat;

import java.util.Set;
import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import ch.prevo.open.encrypted.model.InsurantInformation;
import ch.prevo.open.node.NodeApplication;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = NodeApplication.class)
public class JsonAdapterTest {

    @Inject
    private JsonAdapter jsonAdapter;

    @Test
    public void getInsurantInformation() {
        Set<InsurantInformation> insurantInformation = jsonAdapter.getJobStartInformation();
        assertThat(insurantInformation).hasSize(3);
    }
}