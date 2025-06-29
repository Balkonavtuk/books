package com.example.book_catalog;

import com.example.book_catalog.repository.UserRepository;
import com.example.book_catalog.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl service;

    private UserDetails sampleUser;

    @BeforeEach
    void setUp() {
        sampleUser = org.springframework.security.core.userdetails.User
                .withUsername("alice")
                .password("secret")
                .roles("USER")
                .build();
    }


    @Test
    void loadUserByUsername_whenNotFound_throwsException() {
        when(userRepository.findByUsername("bob"))
                .thenReturn(java.util.Optional.empty());

        assertThatThrownBy(() -> service.loadUserByUsername("bob"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("User not found: bob");
    }
}