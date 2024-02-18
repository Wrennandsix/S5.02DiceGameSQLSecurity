package cat.itacademy.barcelonactiva.SanchezMoreno.Marc.s05.t02.n03SQLSecurity.testUnitarios;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import cat.itacademy.barcelonactiva.SanchezMoreno.Marc.s05.t02.n03SQLSecurity.domain.Game;
import cat.itacademy.barcelonactiva.SanchezMoreno.Marc.s05.t02.n03SQLSecurity.domain.Player;
import cat.itacademy.barcelonactiva.SanchezMoreno.Marc.s05.t02.n03SQLSecurity.exceptions.GamesNotPlayedException;
import cat.itacademy.barcelonactiva.SanchezMoreno.Marc.s05.t02.n03SQLSecurity.exceptions.UserNotFoundException;
import cat.itacademy.barcelonactiva.SanchezMoreno.Marc.s05.t02.n03SQLSecurity.repository.GameRepository;
import cat.itacademy.barcelonactiva.SanchezMoreno.Marc.s05.t02.n03SQLSecurity.repository.PlayerRepository;
import cat.itacademy.barcelonactiva.SanchezMoreno.Marc.s05.t02.n03SQLSecurity.services.impl.PlayerServiceImpl;
import cat.itacademy.barcelonactiva.SanchezMoreno.Marc.s05.t02.n03SQLSecurity.services.PlayerServices;
import org.junit.jupiter.api.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PlayerServiceImplTests {

	@Mock
	private PlayerRepository playerRepository;
	@Mock
	private GameRepository gameRepository;
	@InjectMocks
	private PlayerServices playerService = new PlayerServiceImpl(playerRepository);

	private Player player;
	private List<Game> games = new ArrayList<>();

	@BeforeEach
	void setup() {

		player = new Player("Pepe");
		player.setId(1);
		playerRepository.save(player);

		Game game1 = new Game(player, 3, 6);
		Game game2 = new Game(player, 1, 6);
		Game game3 = new Game(player, 5, 4);
		Game game4 = new Game(player, 1, 6);
		games.add(game1);
		games.add(game2);
		games.add(game3);
		games.add(game4);
		gameRepository.saveAll(games);
	}


	@Test
	public void testGetUserGames() {

		when(playerRepository.findById(player.getId())).thenReturn(Optional.of(player));

		when(gameRepository.findAll()).thenReturn(games);

		List<Game> result = playerService.getUserGames(player.getId());

		assertEquals(games, result);
	}

	@Test
	public void testGetUserGamesThrowsUserNotFoundException() {

		when(playerRepository.findById(player.getId())).thenReturn(Optional.empty());

		assertThrows(UserNotFoundException.class, () -> {
			playerService.getUserGames(player.getId());
		});
	}

	@Test
	public void testCalculateAllAverage() {

		when(gameRepository.findAll()).thenReturn(games);

		double averageRate = playerService.calculateAllAverageRate();

		assertEquals(50.0, averageRate);
	}

	@Test
	public void testCalculateAllAverageThrowGamesNotPlayedException() {

		assertThrows(GamesNotPlayedException.class, () -> {
			playerService.calculateAllAverageRate();
		});
	}

}
