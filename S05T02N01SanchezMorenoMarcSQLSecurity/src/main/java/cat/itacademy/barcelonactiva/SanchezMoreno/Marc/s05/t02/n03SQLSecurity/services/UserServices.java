package cat.itacademy.barcelonactiva.SanchezMoreno.Marc.s05.t02.n03SQLSecurity.services;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserServices {
    UserDetailsService userDetailsService();
}