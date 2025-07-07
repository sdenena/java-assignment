package org.sd.services;

import org.sd.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    public User createUser();
    public User updateUser();
    public boolean deleteUser();
    public Optional<User> getUserById(Long id);
    public List<User> getUserList(int page, int size);
    public List<User> getAllUsers();
}
