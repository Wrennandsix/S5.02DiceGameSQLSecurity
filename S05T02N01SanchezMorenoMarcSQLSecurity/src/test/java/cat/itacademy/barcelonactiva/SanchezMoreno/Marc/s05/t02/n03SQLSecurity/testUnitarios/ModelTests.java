package cat.itacademy.barcelonactiva.SanchezMoreno.Marc.s05.t02.n03SQLSecurity.testUnitarios;



import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import cat.itacademy.barcelonactiva.SanchezMoreno.Marc.s05.t02.n03SQLSecurity.dto.GameDTO;

public class ModelTests {

    @Test
    @DisplayName("GameDTO instantiate properly when Win")
    void testGameWin() {
        GameDTO gameDTO = new GameDTO(1,6);
        assertEquals("You win!!!",gameDTO.getResult());
    }

    @Test
    @DisplayName("GameDTO instantiate properly when Lose")
    void testGameLose() {
        GameDTO gameDTO = new GameDTO(3,2);
        assertEquals("You Lose!!!",gameDTO.getResult());
    }
}