package com.adaptionsoft.games.uglytrivia;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

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
        assertFalse(game.isPlayable()); // Pas assez de joueurs

        game.add("Player2");
        assertTrue(game.isPlayable()); // Suffisamment de joueurs
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
        assertTrue(isPlayerInPenaltyBox(game, 0)); // Vérifie que le joueur 0 est dans la boîte de pénalité
    }



    @Test
    void testWinCondition() {
        game.add("Player1");
        IntStream.range(0, 6).forEach(i -> game.wasCorrectlyAnswered());
        assertFalse(game.wasCorrectlyAnswered()); // Le joueur a gagné après avoir accumulé 6 pièces
    }
}