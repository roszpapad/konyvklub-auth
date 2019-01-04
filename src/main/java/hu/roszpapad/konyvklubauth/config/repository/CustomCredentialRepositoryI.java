package hu.roszpapad.konyvklubauth.config.repository;

import hu.roszpapad.konyvklubauth.config.domain.Credentials;

public interface CustomCredentialRepositoryI {
    Credentials findUserByUsername(String username);
}
