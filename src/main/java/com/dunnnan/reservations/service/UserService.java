package com.dunnnan.reservations.service;

import com.dunnnan.reservations.model.AppUser;
import com.dunnnan.reservations.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.dunnnan.reservations.model.dto.AppUserDto;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    //Login
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AppUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("AppUser with such email was not found: " + email));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(user.getUserType().toString())
                .build();
    }

    //Registration
    public boolean emailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public void registerUser(AppUserDto appUserDto) {
        AppUser newUser = new AppUser(
                appUserDto.getName(),
                appUserDto.getSurname(),
                appUserDto.getEmail(),
                appUserDto.getPhoneNumber(),
                appUserDto.getPassword()
        );
        userRepository.save(newUser);
    }

}
