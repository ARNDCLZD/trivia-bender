package com.adaptionsoft.games.uglytrivia;

import com.adaptionsoft.games.trivia.runner.GameRunner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @Test
    void test_game_output() throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
        GameRunner.main(null);

        System.out.flush();
        String actualOutput = outputStream.toString();

        String absolutePath = System.getProperty("user.dir");
        File file = new File(absolutePath + "/src/test/resources/mockOutput.txt");
        List<String> expectedLines = Files.readAllLines(file.toPath());
        String expectedOutput = String.join(System.lineSeparator(), expectedLines);

        assertEquals(expectedOutput.trim(), actualOutput.trim());
        System.setOut(originalOut);

    }
}