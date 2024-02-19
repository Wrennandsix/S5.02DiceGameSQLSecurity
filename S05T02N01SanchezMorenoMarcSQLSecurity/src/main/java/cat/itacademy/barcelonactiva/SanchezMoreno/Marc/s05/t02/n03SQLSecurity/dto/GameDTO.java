package cat.itacademy.barcelonactiva.SanchezMoreno.Marc.s05.t02.n03SQLSecurity.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GameDTO {
	
	
    private int dice1;
    private int dice2;
    private String result;

    public GameDTO(int dice1, int dice2) {
    	
        this.dice1 = dice1;
        this.dice2 = dice2;
        if(dice1 + dice2 == 7){
            this.result = "You win!!!";
        }
        else {
            this.result = "You Lose!!!";
        }
    }


}