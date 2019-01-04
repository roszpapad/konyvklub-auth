package hu.roszpapad.konyvklubauth.config;

import hu.roszpapad.konyvklubauth.config.domain.Credentials;
import hu.roszpapad.konyvklubauth.config.repository.CredentialRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class JdbcUserDetails implements UserDetailsService {

    @Autowired
    private CredentialRepositoryImpl credentialRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Credentials credentials = credentialRepository.findUserByUsername(username);



        if(credentials == null){

            throw new UsernameNotFoundException("User "+username+" can not be found");
        }

        User user = new User(credentials.getUsername(),credentials.getPassword(),true,true,true,true,credentials.getAuthorities());

        return  user;


    }
}
