package com.neusoft.cas.jdbc;

import org.jasig.cas.adaptors.jdbc.AbstractJdbcUsernamePasswordAuthenticationHandler;
import org.jasig.cas.authentication.PreventedException;
import org.jasig.cas.authentication.principal.Principal;
import org.jasig.cas.authentication.principal.SimplePrincipal;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;

import javax.security.auth.login.AccountNotFoundException;
import javax.security.auth.login.FailedLoginException;
import java.security.GeneralSecurityException;
import java.util.Map;

/**
 * Created by neusoft on 15-5-26.
 */
public class DatabaseAuthenticationHandler extends AbstractJdbcUsernamePasswordAuthenticationHandler {
    private String sql;

    public void setSql(String sql) {
        this.sql = sql;
    }

    @Override
    protected Principal authenticateUsernamePasswordInternal(String username, String password) throws GeneralSecurityException, PreventedException {
        try {
            Map<String, Object> queryMap = getJdbcTemplate().queryForMap(this.sql, username);
            String dbPassword = (String) queryMap.get("password");
            String salt = (String) queryMap.get("salt");

            UCPasswordEncoder encoder = new UCPasswordEncoder();
            encoder.setSalt(salt);
            final String encryptedPassword = encoder.encode(password);
            if (!dbPassword.equals(encryptedPassword)) {
                throw new FailedLoginException("Password does not match value on record.");
            }
        } catch (final IncorrectResultSizeDataAccessException e) {
            if (e.getActualSize() == 0) {
                throw new AccountNotFoundException(username + " not found with SQL query");
            } else {
                throw new FailedLoginException("Multiple records found for " + username);
            }
        } catch (final DataAccessException e) {
            throw new PreventedException("SQL exception while executing query for " + username, e);
        }
        return new SimplePrincipal(username);
    }
}
