package com.dunnnan.reservations.service;

import com.dunnnan.reservations.model.AppUser;
import com.dunnnan.reservations.model.dto.AppUserDto;
import com.dunnnan.reservations.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    //Login
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("AppUser with such email was not found: " + email));
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

    //Data
    public AppUser findUserId(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new RuntimeException("User with id: " + id + " was not found")
        );
    }

    public Long getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUser user = (AppUser) authentication.getPrincipal();
        return user.getId();
    }

}
