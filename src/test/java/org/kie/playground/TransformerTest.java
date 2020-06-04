package org.kie.playground;

import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransformerTest {
    @Test
    public void checkContents() {
        var ds = new ListDataStream<String>();
        // prepend "x" to all strings it sees
        FunctionTransformer<String, String> transf =
                FunctionTransformer.of(s -> "x" + s);

        ds.subscribe(transf);

        var rec = new RecordingSubscriber<String>();
        transf.subscribe(rec);

        ds.append("a");
        ds.append("b");
        ds.append("c");


        assertEquals(List.of("xa", "xb", "xc"), rec.getData());
    }
}
