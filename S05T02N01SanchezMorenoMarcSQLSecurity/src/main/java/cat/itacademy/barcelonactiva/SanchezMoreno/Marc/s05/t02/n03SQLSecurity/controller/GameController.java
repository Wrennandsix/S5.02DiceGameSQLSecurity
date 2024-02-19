package cat.itacademy.barcelonactiva.SanchezMoreno.Marc.s05.t02.n03SQLSecurity.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cat.itacademy.barcelonactiva.SanchezMoreno.Marc.s05.t02.n03SQLSecurity.domain.Game;
import cat.itacademy.barcelonactiva.SanchezMoreno.Marc.s05.t02.n03SQLSecurity.dto.GameDTO;
import cat.itacademy.barcelonactiva.SanchezMoreno.Marc.s05.t02.n03SQLSecurity.dto.PlayerDTO;
import cat.itacademy.barcelonactiva.SanchezMoreno.Marc.s05.t02.n03SQLSecurity.services.PlayerServices;

@RestController
@CrossOrigin(origins = "http://localhost:9001")
@RequestMapping("/players")
public class GameController {

	private final PlayerServices userService;

	public GameController(PlayerServices userService) {
		this.userService = userService;
	}

	@PostMapping("")
	public ResponseEntity<String> newUser(@RequestBody PlayerDTO userRegister) {
		userService.saveUser(userRegister);
		return new ResponseEntity<>("user added successfully", HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<String> updateUser(@PathVariable("id") Integer id, @RequestBody PlayerDTO userDTORequest) {
		userService.updateUser(id, userDTORequest);
		return new ResponseEntity<>("user updated successfully", HttpStatus.OK);
	}

	@PostMapping("/{id}/game")
	public ResponseEntity<GameDTO> play(@PathVariable("id") Integer id) {
		GameDTO newGameDTO = userService.playGame(id);
		return new ResponseEntity<>(newGameDTO, HttpStatus.OK);
	}

	@DeleteMapping("{id}/games")
	public ResponseEntity<String> deleteAllUserGames(@PathVariable("id") Integer id) {
		userService.deleteAllGames(id);
		return new ResponseEntity<>("All games deleted successfully", HttpStatus.OK);
	}

	@GetMapping("")
	public ResponseEntity<List<PlayerDTO>> getAllAverageRate() {

		List<PlayerDTO> userDTOList = userService.getUsersListAverageRate();
		return new ResponseEntity<>(userDTOList, HttpStatus.OK);
	}

	@GetMapping("/{id}/games")
	public ResponseEntity<List<GameDTO>> getAllUserGames(@PathVariable("id") Integer id) {
		List<Game> allGames = userService.getUserGames(id);
		List<GameDTO> allGamesDTO = userService.gameListToGameListDTO(allGames);
		return new ResponseEntity<>(allGamesDTO, HttpStatus.OK);
	}

	@GetMapping("/ranking")
	public ResponseEntity<Double> getAllRanking() {
		double ranking = userService.calculateAllAverageRate();
		return new ResponseEntity<>(ranking, HttpStatus.OK);
	}

	@GetMapping("/ranking/loser")
	public ResponseEntity<PlayerDTO> getLoser() {
		PlayerDTO loserUser = userService.getLoser();
		return new ResponseEntity<>(loserUser, HttpStatus.OK);
	}

	@GetMapping("/ranking/winner")
	public ResponseEntity<PlayerDTO> getWinner() {
		PlayerDTO winnerUser = userService.getWinner();
		return new ResponseEntity<>(winnerUser, HttpStatus.OK);
	}
}
