package ch.prevo.open.encrypted.model;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.time.LocalDate.of;
import static org.junit.Assert.assertSame;

public class InsurantInformationTest {

    @Test
    public void compareTo() throws Exception {
        InsurantInformation i0 = new InsurantInformation("1", "1");
        InsurantInformation i1 = new InsurantInformation("1", "1", of(2000, 1, 1));
        InsurantInformation i2 = new InsurantInformation("2", "1", of(2000, 1, 1));
        InsurantInformation i3 = new InsurantInformation("2", "2", of(2000, 1, 1));
        InsurantInformation i4 = new InsurantInformation("2", "2", of(2000, 2, 1));

        List<InsurantInformation> list = Arrays.asList(i4, i2, i3, i0, i1);
        Collections.sort(list);

        assertSame(i0, list.get(0));
        assertSame(i1, list.get(1));
        assertSame(i2, list.get(2));
        assertSame(i3, list.get(3));
        assertSame(i4, list.get(4));
    }

}