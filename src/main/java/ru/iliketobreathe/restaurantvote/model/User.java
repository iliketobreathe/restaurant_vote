package ru.iliketobreathe.restaurantvote.model;

import java.util.Date;
import java.util.List;
import java.util.Set;

public class User extends AbstractNamedEntity {

    private String email;

    private String password;

    private Set<Role> roles;

    private Date registered = new Date();

    private List<Vote> votes;

    public User() {
    }

    public User(Integer id, String name, String email, String password, Set<Role> roles, Date registered) {
        super(id, name);
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.registered = registered;
    }
}
