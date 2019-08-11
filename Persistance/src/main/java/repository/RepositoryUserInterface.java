package repository;

import domain.User;

import java.util.Collection;

public interface RepositoryUserInterface {

    User getUser(String name, String password);

    void addUser(User user);

    void removeUser(User user);

    Collection<User> getUsers();

}
