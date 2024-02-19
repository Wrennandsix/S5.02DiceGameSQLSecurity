package cat.itacademy.barcelonactiva.SanchezMoreno.Marc.s05.t02.n03SQLSecurity.services.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cat.itacademy.barcelonactiva.SanchezMoreno.Marc.s05.t02.n03SQLSecurity.domain.Game;
import cat.itacademy.barcelonactiva.SanchezMoreno.Marc.s05.t02.n03SQLSecurity.domain.Player;
import cat.itacademy.barcelonactiva.SanchezMoreno.Marc.s05.t02.n03SQLSecurity.dto.GameDTO;
import cat.itacademy.barcelonactiva.SanchezMoreno.Marc.s05.t02.n03SQLSecurity.dto.PlayerDTO;
import cat.itacademy.barcelonactiva.SanchezMoreno.Marc.s05.t02.n03SQLSecurity.exceptions.AlreadyHasNameException;
import cat.itacademy.barcelonactiva.SanchezMoreno.Marc.s05.t02.n03SQLSecurity.exceptions.ExistingNameException;
import cat.itacademy.barcelonactiva.SanchezMoreno.Marc.s05.t02.n03SQLSecurity.exceptions.GamesNotPlayedException;
import cat.itacademy.barcelonactiva.SanchezMoreno.Marc.s05.t02.n03SQLSecurity.exceptions.NotExistingUsersException;
import cat.itacademy.barcelonactiva.SanchezMoreno.Marc.s05.t02.n03SQLSecurity.exceptions.UserNotFoundException;
import cat.itacademy.barcelonactiva.SanchezMoreno.Marc.s05.t02.n03SQLSecurity.exceptions.YouAlreadyAnonymousException;
import cat.itacademy.barcelonactiva.SanchezMoreno.Marc.s05.t02.n03SQLSecurity.repository.GameRepository;
import cat.itacademy.barcelonactiva.SanchezMoreno.Marc.s05.t02.n03SQLSecurity.repository.PlayerRepository;
import cat.itacademy.barcelonactiva.SanchezMoreno.Marc.s05.t02.n03SQLSecurity.services.PlayerServices;

@Service
public class PlayerServiceImpl implements PlayerServices {

	@Autowired
	private PlayerRepository usersRepo;

	@Autowired
	private GameRepository gameRepo;

	public PlayerServiceImpl(PlayerRepository playerRepository) {
		this.usersRepo = playerRepository;

	}

	@Override
	public List<Game> getUserGames(int id) {
		Optional<Player> user = usersRepo.findById(id);
		if (user.isEmpty()) {
			throw new UserNotFoundException();
		}

		List<Game> games = gameRepo.findAll().stream().filter(game -> game.getUserId() == id)
				.collect(Collectors.toList());

		if (games.isEmpty()) {
			throw new GamesNotPlayedException();
		}

		return games;
	}

	@Override
	public Player findRepeatedUser(String name) {
		List<Player> userList = usersRepo.findAll();

		Optional<Player> repeatedUser = userList.stream()
				.filter(u -> u.getName() != null && !u.getName().equalsIgnoreCase("ANONYMOUS"))
				.filter(u -> u.getName() != null && u.getName().equalsIgnoreCase(name)).findFirst();

		return repeatedUser.orElse(null);
	}

	@Override
	public void saveUser(PlayerDTO registerDTO) {

		if (registerDTO.getName() == null || registerDTO.getName().trim().isEmpty()) {
			Player registerUserAnonymus = userDTOToUserAnonymus(registerDTO);
			usersRepo.save(registerUserAnonymus);
			return;
		}
		Player registerUser = userDTOToUser(registerDTO);
		Player repeatedUser = findRepeatedUser(registerUser.getName());

		if (repeatedUser != null && registerUser.getName() != null
				&& registerUser.getName().equalsIgnoreCase(repeatedUser.getName())) {
			throw new ExistingNameException();

		} else {
			usersRepo.save(registerUser);
		}
	}

	@Override
	public double calculateAllAverageRate() {

		List<Game> games = gameRepo.findAll();

		if (games == null || games.isEmpty()) {
			throw new GamesNotPlayedException();
		}

		long victories = games.stream().filter(game -> game.getResult().equalsIgnoreCase("Victory!")).count();

		long defeats = games.size() - victories;

		double averageRate = 0;
		if (victories + defeats > 0) {
			averageRate = (double) victories / (victories + defeats) * 100;
		}

		return averageRate;
	}

	@Override
	public double calculateUserAverageRate(int id) {

		List<Game> games = getUserGames(id);

		if (games == null || games.isEmpty()) {
			throw new GamesNotPlayedException();
		}

		double averageRate = 0;

		long victories = games.stream().filter(game -> game.getResult().equalsIgnoreCase("Victory!")).count();

		long defeats = games.size() - victories;

		averageRate = (double) victories / (victories + defeats) * 100;

		return averageRate;
	}

	@Override
	public List<Game> listGames() {
		return gameRepo.findAll();
	}

	@Override
	public Game saveGame(Game game) {		
		return gameRepo.save(game);
	}

	@Override
	public List<Player> getAllUsers() {
		return usersRepo.findAll();
	}

	@Override
	public Player findById(int id) {
		return usersRepo.findById(id).get();
	}

	@Override
	public Player userDTOToUser(PlayerDTO userRegisterDTO) {
		return new Player(userRegisterDTO.getName());
	}

	@Override
	public Player userDTOToUserAnonymus(PlayerDTO userRegisterDTOAnonymus) {
		return new Player("ANONYMOUS");
	}

	@Override
	public PlayerDTO userToDTO(Player userRegister) {
		return new PlayerDTO(userRegister.getName(), userRegister.getAverageRate());
	}

	@Override
	public GameDTO gameToGameDTO(Game game) {
		return new GameDTO(game.getDice1(), game.getDice2());
	}

	@Override
	public List<GameDTO> gameListToGameListDTO(List<Game> games) {

		return games.stream().map(this::gameToGameDTO).collect(Collectors.toList());
	}

	@Override
	public void updateUser(Integer id, PlayerDTO userDTO) {

		Player user = getUser(id);

		if (userDTO.getName() == null || userDTO.getName().trim().isEmpty()) {
			if ("ANONYMOUS".equalsIgnoreCase(user.getName())) {
				throw new YouAlreadyAnonymousException();
			}
			user.setName("ANONYMOUS");
			usersRepo.save(user);
			return;
		}

		if (userDTO.getName().equalsIgnoreCase(user.getName())) {
			throw new AlreadyHasNameException();
		}

		Optional<Player> existingUser = usersRepo.findByName(userDTO.getName());

		if (existingUser != null && existingUser.isPresent()) {
			if (existingUser.get().getId() != user.getId()) {
				throw new ExistingNameException();
			}
		}
		user.setName(userDTO.getName());
		usersRepo.save(user);
	}

	@Override
	public Player getUser(Integer id) {

		Optional<Player> user = usersRepo.findById(id);
		if (user.isEmpty()) {
			throw new UserNotFoundException();
		}
		return user.get();
	}

	@Override
	public GameDTO playGame(Integer id) {

		Optional<Player> user = usersRepo.findById(id);
		if (user.isEmpty()) {
			throw new UserNotFoundException();
		}

		Game game = new Game(id);
		saveGame(game);
		recalculateAverage(id);
		GameDTO gameDTO = gameToGameDTO(game);

		return gameDTO;
	}

	@Override
	public void recalculateAverage(Integer id) {

		Double newAverage = calculateUserAverageRate(id);

		Player user = getUser(id);
		user.setAverageRate(newAverage);

		usersRepo.save(user);

	}

	@Override
	public void deleteAllGames(Integer id) {
		List<Game> userGames = getUserGames(id);

		userGames.forEach(gameRepo::delete);

		Player currentUser = getUser(id);
		currentUser.setAverageRate(null);
		usersRepo.save(currentUser);
	}

	@Override
	public List<PlayerDTO> getUsersListAverageRate() {

		List<Player> userList = usersRepo.findAll();
		if (userList.isEmpty()) {
			throw new NotExistingUsersException();
		}
		List<PlayerDTO> userDTOList = new ArrayList<>();

		if (userList != null) {
			userList.forEach(u -> {
				if (u != null) {
					userDTOList.add(new PlayerDTO(u.getName(), u.getAverageRate()));
				}
			});
		}

		return userDTOList;
	}

	@Override
	public PlayerDTO getLoser() {

		List<PlayerDTO> userDTOList = getUsersListAverageRate();
		return userDTOList.stream().filter(u -> u.getAverageRate() != null)
				.min(Comparator.comparing(PlayerDTO::getAverageRate)).orElseThrow(GamesNotPlayedException::new);
	}

	@Override
	public PlayerDTO getWinner() {

		List<PlayerDTO> userDTOList = getUsersListAverageRate();
		return userDTOList.stream().filter(u -> u.getAverageRate() != null)
				.max(Comparator.comparing(PlayerDTO::getAverageRate)).orElseThrow(GamesNotPlayedException::new);
	}

}
