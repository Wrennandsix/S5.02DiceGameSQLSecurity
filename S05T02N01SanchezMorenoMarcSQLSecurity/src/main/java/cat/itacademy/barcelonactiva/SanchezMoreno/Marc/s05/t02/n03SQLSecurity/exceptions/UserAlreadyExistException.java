package cat.itacademy.barcelonactiva.SanchezMoreno.Marc.s05.t02.n03SQLSecurity.exceptions;


public class UserAlreadyExistException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public UserAlreadyExistException(String msg) {
        super(msg);
    }
}