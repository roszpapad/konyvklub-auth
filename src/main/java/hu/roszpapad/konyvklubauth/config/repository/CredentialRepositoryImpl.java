package hu.roszpapad.konyvklubauth.config.repository;

import hu.roszpapad.konyvklubauth.config.domain.Authority;
import hu.roszpapad.konyvklubauth.config.domain.Credentials;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class CredentialRepositoryImpl implements CustomCredentialRepositoryI {

    @PersistenceContext
    EntityManager entityManager;

    private static final String query1 = "select id, username, password, email, first_name, last_name from konyvklub_schema.user where username = ? and enabled=true";

    private static final String query2 = "select * from konyvklub_schema.authority where id in (select authorities_id from user_authorities where user_id = ?);";

    public static String getQuery1() {
        return query1;
    }

    private String getQuery2() {
        return query2;
    }

    @Override
    public Credentials findUserByUsername(String username) {


        Query query1 = entityManager.createNativeQuery(getQuery1());
        query1.setParameter(1, username);
        List query1ResultList =  query1.getResultList();
        List<Credentials> credentialsList = new ArrayList<>();
        Iterator it = query1ResultList.iterator();
        while (it.hasNext()){
            Object[] line = (Object[]) it.next();
            Credentials credentials = new Credentials();
            BigInteger integer = (BigInteger) line[0];
            credentials.setId(integer.longValue());
            credentials.setUsername((String) line[1]);
            credentials.setPassword((String) line[2]);
            credentials.setEmail((String) line[3]);
            credentials.setFirstName((String) line[4]);
            credentials.setLastName((String) line[5]);

            Query query2 = entityManager.createNativeQuery(getQuery2());
            query2.setParameter(1, credentials.getId());
            List query2ResultList = query2.getResultList();
            List<Authority> authorities = new ArrayList<>();
            Iterator it2 = query2ResultList.iterator();
            while (it2.hasNext()) {
                Object[] line2 = (Object[]) it2.next();
                Authority authority = new Authority();
                BigInteger integer2 = (BigInteger) line2[0];
                authority.setId(integer2.longValue());
                authority.setAuthority((String) line2[1]);
                authorities.add(authority);
            }
            credentials.setAuthorities(authorities);
            credentialsList.add(credentials);
        }

        return credentialsList.get(0);

    }
}
