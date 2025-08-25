package shadowbuddy.app;

import org.junit.jupiter.api.Test;
import shadowbuddy.services.ShadowException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ShadowParserTest {
    @Test
    public void parse_todoCreation() throws ShadowException {
        String userInput = "todo return book";
        ShadowCommand userCommand = ShadowParser.parse(userInput);

        assertEquals(ShadowCommand.CommandType.TODO, userCommand.commandType);
        assertEquals("return book", userCommand.taskDescription);
    }
}
