package cat.itacademy.barcelonactiva.SanchezMoreno.Marc.s05.t02.n03SQLSecurity.domain;



import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;

import cat.itacademy.barcelonactiva.SanchezMoreno.Marc.s05.t02.n03SQLSecurity.domain.utils.ResultCalculator;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "games")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Game {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "user_id", nullable = false)
	private int userId;
	
	@Column(name = "dice1", nullable = false)
	private int dice1;

	@Column(name = "dice2", nullable = false)
	private int dice2;

	@Column(name = "result", nullable = false)
	private String result;
	
	@ManyToOne
    private Player player;
	
	@CreationTimestamp
	@Column(nullable = false, updatable = false)
	private Timestamp gameDate;

	public Game(int usuario_id) {
		
		ResultCalculator calculateResult = (d1, d2) -> (d1 + d2 == 7) ? "Victory!" : "you lose";

		this.dice1 = (int) (Math.random() * 6 + 1);
		this.dice2 = (int) (Math.random() * 6 + 1);
		this.result = calculateResult.calculate(dice1, dice2);
		this.userId = usuario_id;
	}
	
	public Game(Player player,int dice1,int dice2) {
		
		ResultCalculator calculateResult = (d1, d2) -> (d1 + d2 == 7) ? "Victory!" : "you lose";
		this.dice1 = dice1;
		this.dice2 = dice2;
		this.result = calculateResult.calculate(dice1, dice2);
		this.player = player;
		this.userId = player.getId();
	}



}
