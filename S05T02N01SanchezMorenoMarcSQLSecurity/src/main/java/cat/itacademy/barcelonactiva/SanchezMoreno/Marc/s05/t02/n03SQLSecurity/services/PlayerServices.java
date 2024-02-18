package cat.itacademy.barcelonactiva.SanchezMoreno.Marc.s05.t02.n03SQLSecurity.services;

import org.springframework.stereotype.Service;

import cat.itacademy.barcelonactiva.SanchezMoreno.Marc.s05.t02.n03SQLSecurity.domain.Game;
import cat.itacademy.barcelonactiva.SanchezMoreno.Marc.s05.t02.n03SQLSecurity.domain.Player;
import cat.itacademy.barcelonactiva.SanchezMoreno.Marc.s05.t02.n03SQLSecurity.dto.GameDTO;
import cat.itacademy.barcelonactiva.SanchezMoreno.Marc.s05.t02.n03SQLSecurity.dto.PlayerDTO;
import java.util.List;



@Service
public interface PlayerServices {
	public void updateUser(Integer id, PlayerDTO userDTO);
	public void saveUser(PlayerDTO registroDto);
	public List<Player> getAllUsers();
	public List<Game> getUserGames(int user_id);
	public Player findById(int user_id);
	public Player findRepeatedUser(String name);
	public double calculateAllAverageRate();
	public List<Game> listGames();
	public Game saveGame(Game game);
	public double calculateUserAverageRate(int id);
	public Player userDTOToUser(PlayerDTO userRegisterDTO);
	public PlayerDTO userToDTO(Player userRegister);
	public Player getUser(Integer id);
	public GameDTO playGame(Integer id);
	public void deleteAllGames(Integer id);
	public List<PlayerDTO> getUsersAverageRate();
	public PlayerDTO getLoser();
	public PlayerDTO getWinner();
	public void recalculateAverage(Integer id);
	public Player userDTOToUserAnonymus(PlayerDTO userRegisterDTOAnonymus);
	public List<GameDTO> gameListToGameListDTO(List<Game> games);
	public GameDTO gameToGameDTO(Game game);
	public Game gameDTOToEntity(Player player, GameDTO gameDTO);
	public Player findUser(String username);
	
}	
