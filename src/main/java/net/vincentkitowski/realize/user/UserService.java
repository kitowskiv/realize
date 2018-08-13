package net.vincentkitowski.realize.user;

import net.vincentkitowski.realize.forms.UserForm;
import net.vincentkitowski.realize.models.User;

public interface UserService {

    public User save(UserForm userForm) throws EmailExistsException;
    public User findByEmail(String email);

}
