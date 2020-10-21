package ru.iliketobreathe.restaurantvote.web;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.iliketobreathe.restaurantvote.AuthorizedUser;
import ru.iliketobreathe.restaurantvote.model.User;
import ru.iliketobreathe.restaurantvote.repository.CrudUserRepository;

@Service("userService")
public class MyUserDetailsService implements UserDetailsService {

    private final CrudUserRepository repository;

    public MyUserDetailsService(CrudUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public AuthorizedUser loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = repository.getByEmail(email.toLowerCase());
        if (user == null) {
            throw new UsernameNotFoundException("User " + email + " is not found");
        }
        return new AuthorizedUser(user);
    }
}
