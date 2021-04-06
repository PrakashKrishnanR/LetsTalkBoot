package com.app.expd.service;

import com.app.expd.constants.MessageConstants;
import com.app.expd.dto.*;
import com.app.expd.exceptions.LetsTalkException;
import com.app.expd.models.NotificationEmail;
import com.app.expd.models.Roles;
import com.app.expd.models.User;
import com.app.expd.models.VerificationToken;
import com.app.expd.repository.UserRepository;
import com.app.expd.repository.VerificationtokenRepository;
import com.app.expd.security.JwtProvider;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationtokenRepository verificationtokenRepository;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RefreshTokenServie refreshTokenServie;
    private final MessageConstants messageConstants;

    @Transactional
    public void signup(RegisterRequest registerRequest){
        Optional<User> checkUserExist = userRepository.findByUsernameIgnoreCase(registerRequest.getEmail());
        if (checkUserExist.isPresent()) {
            throw new LetsTalkException("User Already Signed In with the Email !!");
        }
        User user = new User();
        user.setUsername(registerRequest.getEmail().toUpperCase());
        user.setPassword(
                this.passwordEncoder.encode(registerRequest.getPassword()));
        user.setName(registerRequest.getEmail().split("@")[0].toLowerCase());
        user.setCreatedDate(Instant.now());
        user.setRole(Roles.USER);
        user.setEnabled(false);

        userRepository.save(user);

        //* Verify user using mailID --- currently unavailable
       String token =  generateUserVerificationToken(user);
       mailService.sendNotification(new NotificationEmail(messageConstants.getMailAlert(),
               user.getUsername(), messageConstants.getMessageBody()+messageConstants.getHostName()+messageConstants.getAuthServiceUrl()+token));
    }

    @Transactional
    public User getCurrentUser(){
        String username;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            username  = ((UserDetails)principal).getUsername();
        } else {
             username = principal.toString();
        }
        return userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User name not found - " + username));
    }

    private String generateUserVerificationToken(User user) {

        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setCreatedDste(Instant.now());
        verificationToken.setToken(token);
        verificationToken.setUser(user);

        verificationtokenRepository.save(verificationToken);
        return token;
    }
    @Transactional
    public String verifyToken(String token) {

        VerificationToken verificationToken = verificationtokenRepository.findByToken(token).
                orElseThrow( () -> new LetsTalkException("Invalid Token"));
        fetchAndEnableUser(verificationToken);
        verificationtokenRepository.delete(verificationToken);
        return token;
    }


    public void fetchAndEnableUser(VerificationToken verificationToken) {

        User user = verificationToken.getUser();
        if( user == null) {
            throw new LetsTalkException("No valid Registered User found");
        }
        user.setEnabled(true);
    }

    @Transactional
    public void resetUserPassoword(String username) {

        User user = this.userRepository.findByUsernameIgnoreCase(username).orElseThrow(
                () -> new LetsTalkException("Invalid EmailId !!")
        );

        String newPassword = UUID.randomUUID().toString().substring(1,9);

        user.setPassword(this.passwordEncoder.encode(newPassword));
        this.userRepository.save(user);

        mailService.resetPasswordNotification(new NotificationEmail(messageConstants.getResetSubject(),
                user.getUsername(), messageConstants.getResetPassword()+ newPassword));

    }
    public AuthenticationResponse login(LoginRequest loginRequest) {

       Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername().toUpperCase(),
                loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String authToken = jwtProvider.generateToken(authentication);
        return AuthenticationResponse.builder()
                .authenticationToken(authToken)
                .username(loginRequest.getUsername().toUpperCase())
                .loggedInUser(loginRequest.getUsername().split("@")[0].toLowerCase())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getTokenExpirationTime()))
                .refreshToken(refreshTokenServie.generateRefreshToken().getRefreshToken())
                .build();
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest){
        refreshTokenServie.validateRefreshToken(refreshTokenRequest.getRefreshToken());
        String token = jwtProvider.generateTokenWithUsername(refreshTokenRequest.getUsername());

        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .username(refreshTokenRequest.getUsername().toUpperCase())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getTokenExpirationTime()))
                .refreshToken(refreshTokenRequest.getRefreshToken())
                .build();
    }

    public void changePassword(ResetPasswordRequest resetPasswordRequest) {

        User user = this.getCurrentUser();

        if(resetPasswordRequest.getNewPassword().equals(resetPasswordRequest.getOldPassword())) {
            throw new LetsTalkException("Please choose different password.");
        } else if ( ! this.bcryptPasswordMatch(resetPasswordRequest.getOldPassword(),
                user.getPassword())) {
            throw new LetsTalkException("Invalid Old password");
        }
        user.setPassword(
                this.passwordEncoder.encode(resetPasswordRequest.getNewPassword())
        );
        this.userRepository.save(user);
    }

    public boolean isLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
    }

    public boolean bcryptPasswordMatch(String oldpassword, String encodedPassword) {
       return BCrypt.checkpw(oldpassword, encodedPassword);
    }
}
