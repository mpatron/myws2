package org.jobjects.myws2.orm.user;
import java.util.Collection;
import javax.ejb.Local;

@Local
public interface UserRepository {
    void addUser(User user);
    Collection<User> getUsers();
    int getUserCount();
    User getUser(String email);
    User deleteUser(String email);
}