package seedu.address.logic.parser;

import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.ExportCommand;

public class ExportCommandParserTest {

    private final ExportCommandParser parser = new ExportCommandParser();

    @Test
    public void parse_noArguments_returnsDefaultCommand() {
        assertParseSuccess(parser, "", new ExportCommand());
    }

    @Test
    public void parse_filePathWithoutExtension_appendsCsv() {
        assertParseSuccess(parser, "reports",
                new ExportCommand(Paths.get("reports.csv")));
    }

    @Test
    public void parse_directoryPathWithTrailingSlash_treatedAsDirectory() {
        assertParseSuccess(parser, "exports/", new ExportCommand(Paths.get("exports"), true));
    }

    @Test
    public void parse_invalidPath_throwsParseException() {
        String invalidPath = "invalid\0path";
        assertParseFailure(parser, invalidPath,
                String.format("The provided export path is invalid: %s", invalidPath));
    }
}
