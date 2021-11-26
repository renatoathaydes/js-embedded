package embedded;

import com.athaydes.js.embedded.JsEmbed;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsEmbedTest {
    JsEmbed js = new JsEmbed();

    @Test
    public void canRunJsBasic() {
        assertEquals(5, js.eval("2 + 3"));
    }
}
