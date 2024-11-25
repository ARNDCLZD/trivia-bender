import com.adaptionsoft.games.uglytrivia.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import java.util.stream.IntStream;

class SomeTest {

    private Game game;

    @BeforeEach
    void setUp() {
        game = new Game();
    }

    // Méthodes utilitaires pour accéder aux champs privés via réflexion
    private int[] getPrivatePlaces(Game game) throws NoSuchFieldException, IllegalAccessException {
        Field placesField = Game.class.getDeclaredField("places");
        placesField.setAccessible(true);
        return (int[]) placesField.get(game);
    }

    private int getPlayerPlace(Game game, int playerIndex) throws NoSuchFieldException, IllegalAccessException {
        return getPrivatePlaces(game)[playerIndex];
    }

    private boolean[] getPrivatePenaltyBox(Game game) throws NoSuchFieldException, IllegalAccessException {
        Field penaltyBoxField = Game.class.getDeclaredField("inPenaltyBox");
        penaltyBoxField.setAccessible(true);
        return (boolean[]) penaltyBoxField.get(game);
    }

    private boolean isPlayerInPenaltyBox(Game game, int playerIndex) throws NoSuchFieldException, IllegalAccessException {
        return getPrivatePenaltyBox(game)[playerIndex];
    }

    // Tests
    @Test
    void testAddPlayer() {
        assertTrue(game.add("Player1"));
        assertTrue(game.add("Player2"));
        assertEquals(2, game.howManyPlayers());
    }

    @Test
    void testIsPlayable() {
        game.add("Player1");
        assertFalse(game.isPlayable());

        game.add("Player2");
        assertTrue(game.isPlayable());
    }

    @Test
    void testRoll() throws NoSuchFieldException, IllegalAccessException {
        game.add("Player1");
        game.add("Player2");

        game.roll(3);
        assertEquals(3, getPlayerPlace(game, 0));
    }

    @Test
    void testRollWrapAround() throws NoSuchFieldException, IllegalAccessException {
        game.add("Player1");
        game.add("Player2");

        game.roll(12); // Déplacement dépasse la limite
        assertEquals(0, getPlayerPlace(game, 0)); // Vérifie que la position revient à 0
    }

    @Test
    void testWasCorrectlyAnswered() {
        game.add("Player1");
        game.add("Player2");

        boolean result = game.wasCorrectlyAnswered();
        assertTrue(result);
    }

    @Test
    void testWrongAnswer() throws NoSuchFieldException, IllegalAccessException {
        game.add("Player1");
        game.add("Player2");

        boolean result = game.wrongAnswer();
        assertTrue(result);
        assertTrue(isPlayerInPenaltyBox(game, 0));
    }

    @Test
    void testWinCondition() {
        game.add("Player1");
        IntStream.range(0, 6).forEach(i -> game.wasCorrectlyAnswered());
        assertFalse(game.wasCorrectlyAnswered());
    }
}
