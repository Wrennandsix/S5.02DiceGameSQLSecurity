package cat.itacademy.barcelonactiva.SanchezMoreno.Marc.s05.t02.n03SQLSecurity.testUnitarios;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import cat.itacademy.barcelonactiva.SanchezMoreno.Marc.s05.t02.n03SQLSecurity.domain.Game;
import cat.itacademy.barcelonactiva.SanchezMoreno.Marc.s05.t02.n03SQLSecurity.domain.Player;
import cat.itacademy.barcelonactiva.SanchezMoreno.Marc.s05.t02.n03SQLSecurity.dto.PlayerDTO;
import cat.itacademy.barcelonactiva.SanchezMoreno.Marc.s05.t02.n03SQLSecurity.exceptions.GamesNotPlayedException;
import cat.itacademy.barcelonactiva.SanchezMoreno.Marc.s05.t02.n03SQLSecurity.exceptions.UserNotFoundException;
import cat.itacademy.barcelonactiva.SanchezMoreno.Marc.s05.t02.n03SQLSecurity.repository.GameRepository;
import cat.itacademy.barcelonactiva.SanchezMoreno.Marc.s05.t02.n03SQLSecurity.repository.PlayerRepository;
import cat.itacademy.barcelonactiva.SanchezMoreno.Marc.s05.t02.n03SQLSecurity.services.impl.PlayerServiceImpl;
import cat.itacademy.barcelonactiva.SanchezMoreno.Marc.s05.t02.n03SQLSecurity.services.PlayerServices;
import org.junit.jupiter.api.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
	private Player playerAnonymous;
	private PlayerDTO playerDTO;
	private List<Game> games = new ArrayList<>();
	private List<Player> players = new ArrayList<>();

	@BeforeEach
	void setup() {
		playerAnonymous = new Player("");
		playerAnonymous.setId(2);
		playerRepository.save(playerAnonymous);
		player = new Player("Pepe");
		player.setId(1);
		playerRepository.save(player);
		playerDTO = new PlayerDTO("PepeDTO");
		playerRepository.save(playerService.userDTOToUser(playerDTO));
		players.add(player);
		players.add(playerAnonymous);
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
	public void testGetUserGamesThrowsGamesNotPlayedException() {

		when(playerRepository.findById(player.getId())).thenReturn(Optional.of(player));

		when(gameRepository.findAll()).thenReturn(new ArrayList<>());

		assertThrows(GamesNotPlayedException.class, () -> {
			playerService.getUserGames(player.getId());
		});
	}

	@Test
	public void testGetUserGamesThrowsUserNotFoundException() {

		when(playerRepository.findById(player.getId())).thenReturn(Optional.empty());

		assertThrows(UserNotFoundException.class, () -> {
			playerService.getUserGames(player.getId());
		});
	}

	@Test
	void testFindRepeatedUserWhenUserExists() {
		String nameExisting = "Pepe";

		when(playerRepository.findAll()).thenReturn(players);

		Player repeatedUser = playerService.findRepeatedUser(nameExisting);

		assertNotNull(repeatedUser);
		assertEquals(nameExisting, repeatedUser.getName());
	}

	@Test
	void testFindRepeatedUserWhenUserDoesNotExist() {
		String nameToFind = "Cocoa";

		when(playerRepository.findAll()).thenReturn(players);

		Player repeatedUser = playerService.findRepeatedUser(nameToFind);

		assertNull(repeatedUser);
	}

	@Test
	public void testSaveUserWithNullName() {

		verify(playerRepository, times(3)).save(any());
	}

	@Test
	public void testCalculateAllAverage() {

		when(gameRepository.findAll()).thenReturn(games);

		double averageRate = playerService.calculateAllAverageRate();

		assertEquals(50.0, averageRate);
	}

	@Test
	public void testCalculateAllAverageThrowGamesNotPlayedException() {

		when(gameRepository.findAll()).thenReturn(new ArrayList<>());

		assertThrows(GamesNotPlayedException.class, () -> {
			playerService.calculateAllAverageRate();
		});
	}

	@Test
	void testCalculateUserAverageRateWithGames() {

		when(playerRepository.findById(player.getId())).thenReturn(Optional.of(player));

		when(gameRepository.findAll()).thenReturn(games);

		double expectedAverageRate = 50.00;

		assertEquals(expectedAverageRate, playerService.calculateUserAverageRate(player.getId()));
	}

	@Test
	void testCalculateUserAverageRateThrowGamesNotPlayedException() {

		when(playerRepository.findById(player.getId())).thenReturn(Optional.of(player));

		when(gameRepository.findAll()).thenReturn(new ArrayList<>());

		assertThrows(GamesNotPlayedException.class, () -> {
			playerService.calculateUserAverageRate(player.getId());
		});
	}

	@Test
	public void testPlayerToAnonymousPlayer() {

		Player player = playerService.userDTOToUserAnonymus(playerDTO);

		assertEquals("ANONYMOUS", player.getName());
	}

	@Test
	void testUpdateUser() {
		
		Player existingUser = new Player("PepeDTO2");

		when(playerRepository.findById(1)).thenReturn(Optional.of(existingUser));
		playerService.updateUser(1, playerDTO);

		assertEquals(playerDTO.getName(), existingUser.getName());
		verify(playerRepository, times(1)).save(existingUser);
	}

	@Test
	public void testGetUsersListAverageRate() {

		List<Player> userList = new ArrayList<>();
		userList.add(new Player("Player1", 80.0));
		userList.add(new Player("Player2", 70.0));

	
		when(playerRepository.findAll()).thenReturn(userList);

		PlayerServiceImpl playerService = new PlayerServiceImpl(playerRepository);

		List<PlayerDTO> userDTOList = playerService.getUsersListAverageRate();

		assertNotNull(userDTOList);
		assertEquals(2, userDTOList.size());
		assertEquals("Player1", userDTOList.get(0).getName());
		assertEquals(80.0, userDTOList.get(0).getAverageRate());
		assertEquals("Player2", userDTOList.get(1).getName());
		assertEquals(70.0, userDTOList.get(1).getAverageRate());
	}

	@Test
	void testGetLoser() {

		Player david = new Player("David");
		Player gerard = new Player("Gerard");
		players.add(david);
		players.add(gerard);
		david.setAverageRate(99.9);
		gerard.setAverageRate(0.0);

		when(playerRepository.findAll()).thenReturn(players);

		PlayerDTO expected = playerService.userToDTO(gerard);
		PlayerDTO loser = playerService.getLoser();

		assertEquals(expected.getName(), loser.getName());
		assertEquals(expected.getAverageRate(), loser.getAverageRate());
	}

	@Test
	void testGetWinner() {

		Player david = new Player("David");
		Player gerard = new Player("Gerard");
		players.add(david);
		players.add(gerard);
		david.setAverageRate(99.9);
		gerard.setAverageRate(0.0);

		when(playerRepository.findAll()).thenReturn(players);

		PlayerDTO expected = playerService.userToDTO(david);
		PlayerDTO winner = playerService.getWinner();

		assertEquals(expected.getName(), winner.getName());
		assertEquals(expected.getAverageRate(), winner.getAverageRate());
	}

}
