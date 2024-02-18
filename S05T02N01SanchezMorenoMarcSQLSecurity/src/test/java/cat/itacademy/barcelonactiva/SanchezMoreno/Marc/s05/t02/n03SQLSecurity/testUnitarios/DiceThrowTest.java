package cat.itacademy.barcelonactiva.SanchezMoreno.Marc.s05.t02.n03SQLSecurity.testUnitarios;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;

import cat.itacademy.barcelonactiva.SanchezMoreno.Marc.s05.t02.n03SQLSecurity.domain.Game;

public class DiceThrowTest {

	@RepeatedTest(35)
	@DisplayName("Test dice throws")
	void testNewRandomDice() {
		int id = 1;
		Game game = new Game(id);

		assertTrue(game.getDice1() > 0 && game.getDice1() <= 6);
		assertTrue(game.getDice2() > 0 && game.getDice2() <= 6);
	}

}