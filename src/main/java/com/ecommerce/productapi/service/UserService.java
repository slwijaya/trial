package com.ecommerce.productapi.service;

import com.ecommerce.productapi.model.User;
import com.ecommerce.productapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public User validateUser(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    // Metode untuk registrasi pengguna baru
    public User registerUser(String email, String password, String role) {
        if (userRepository.findByEmail(email) != null) {
            return null; // Email sudah digunakan
        }

        // Enkripsi password sebelum disimpan
        String encodedPassword = passwordEncoder.encode(password);
        User user = new User(email, encodedPassword, role);
        return userRepository.save(user);
    }
}
