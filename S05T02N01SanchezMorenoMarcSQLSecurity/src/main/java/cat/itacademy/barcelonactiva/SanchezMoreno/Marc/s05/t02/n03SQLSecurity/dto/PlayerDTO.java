package cat.itacademy.barcelonactiva.SanchezMoreno.Marc.s05.t02.n03SQLSecurity.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
@Data
@Setter
@Getter 
public class PlayerDTO {
	
	private String name;

	private Double averageRate;

	public PlayerDTO(String name, Double averageRate) {

	    this.name = name;
		this.averageRate = averageRate;
	}

	public PlayerDTO(String name) {
		this.name = name;
	
	}

}