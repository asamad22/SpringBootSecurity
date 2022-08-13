package bd.com.dipti.testspringsecurity.service;
import bd.com.dipti.testspringsecurity.model.AppUser;
import bd.com.dipti.testspringsecurity.repository.AppUserRepository;
import bd.com.dipti.testspringsecurity.token.ConfirmationToken;
import bd.com.dipti.testspringsecurity.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.omg.PortableInterceptor.USER_EXCEPTION;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {
    @Autowired
    private AppUserRepository appUserRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private ConfirmationTokenService confirmationTokenService;
    private static String USER_NOT_FOUND_MSG="user with email %s not found";

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return appUserRepository.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG,email)));
    }

    public String SignUpUser(AppUser appUser){
        boolean existingUser=appUserRepository
                .findByEmail(appUser.getEmail())
                .isPresent();

        if(existingUser){
            throw  new IllegalStateException("Email already taken");
        }

        String encodePassword=bCryptPasswordEncoder
                .encode(appUser
                        .getPassword());
        appUser.setPassword(encodePassword);

        appUserRepository.save(appUser);


        String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                appUser
        );

        confirmationTokenService.saveConfirmationToken(
                confirmationToken);

//        TODO: SEND EMAIL

        return token;
    }

    public int enableAppUser(String email) {
        return appUserRepository.enableAppUser(email);
    }
}