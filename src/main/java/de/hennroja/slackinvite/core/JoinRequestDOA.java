package de.hennroja.slackinvite.core;

import com.google.common.base.Optional;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

/**
 * Created by hennroja on 20/03/16.
 */
public class JoinRequestDOA extends AbstractDAO<JoinRequest> {

    public JoinRequestDOA(SessionFactory factory) {
        super(factory);
    }

    public Optional<JoinRequest> findById(Long id) {
        return Optional.fromNullable(get(id));
    }

    public JoinRequest create(JoinRequest joinRequest) {
        return persist(joinRequest);
    }

    public JoinRequest findByMail(String email) {
        return list(namedQuery("de.hennroja.slackinvite.JoinRequest.findByMail").setString("email", email)).get(0);
    }
}
