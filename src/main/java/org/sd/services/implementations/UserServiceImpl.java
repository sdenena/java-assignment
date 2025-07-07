package org.sd.services.implementations;

import org.hibernate.Session;
import org.sd.configurations.HibernateUtil;
import org.sd.entity.User;
import org.sd.repositories.UserRepository;
import org.sd.services.UserService;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class UserServiceImpl implements UserService {
    private final Scanner scanner = new Scanner(System.in);
    UserRepository userRepository = new UserRepository();

    @Override
    public User createUser() {
        User user = new User();

        System.out.print("Input name: ");
        user.setName(scanner.nextLine());
        System.out.print("Input phone number: ");
        user.setPhoneNumber(scanner.nextLine());
        System.out.print("Input email (optional): ");
        user.setPhoneNumber(scanner.nextLine());

        return userRepository.saveUser(user);
    }

    @Override
    public User updateUser(User user) {
        System.out.print("Input ID: ");
        user.setId(scanner.nextLong());

        if (user.getId() == null || user.getId() == 0L) {
            throw new IllegalArgumentException("User and User ID cannot be null");
        }

        System.out.print("Input name: ");
        user.setPhoneNumber(scanner.nextLine());
        System.out.print("Input phone number: ");
        user.setPhoneNumber(scanner.nextLine());
        System.out.print("Input email (optional): ");
        user.setPhoneNumber(scanner.nextLine());

        // Check if user exists
        Optional<User> existingUser = getUserById(user.getId());
        if (existingUser.isEmpty()) {
            throw new RuntimeException("User not found with ID: " + user.getId());
        }

        // Optional: Merge with existing data (if you want to keep some fields unchanged)
        User userToUpdate = existingUser.get();
        userToUpdate.setName(user.getName());
        userToUpdate.setEmail(user.getEmail());
        userToUpdate.setPhoneNumber(user.getPhoneNumber());

        return userRepository.updateUser(userToUpdate);
    }

    @Override
    public Optional<User> getUserById(Long id) {
         return userRepository.findUserById(id);
    }

    @Override
    public List<User> getUserList(int page, int size) {
        return userRepository.getAllUsers(page, size);
    }
}
