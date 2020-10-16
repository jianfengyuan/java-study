package dao;

import domain.User;

import java.util.List;

public interface IUserDao {
    List<User> findAll();

    List<User> findUserAccount();

    List<User> findUserRole();
}
