package repository;

import domain.User;

public interface RepositoryUserInterface {

    User getUser(String name, String password);

}
