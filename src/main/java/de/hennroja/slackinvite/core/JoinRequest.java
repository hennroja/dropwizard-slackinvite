package de.hennroja.slackinvite.core;

import javax.persistence.*;

/**
 * Created by hennroja on 20/03/16.
 */
@Entity
@Table(name = "joinrequest")
@NamedQueries({
        @NamedQuery(
                name = "JoinRequest.findAll",
                query = "SELECT p FROM JoinRequest p"
        ),
        @NamedQuery(
                name = "de.hennroja.slackinvite.JoinRequest.findByMail",
                query = "SELECT p FROM JoinRequest p WHERE p.email = :email"
        )
})
public class JoinRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "firstname", nullable = false)
    private String firstname;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "activation_hash", nullable = false)
    private String hash;

    @Column(name = "active", nullable = false)
    private boolean active;

    public JoinRequest() {

    }

    public JoinRequest(String firstname, String email) {
        this.firstname = firstname;
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JoinRequest that = (JoinRequest) o;

        if (id != that.id) return false;
        if (firstname != null ? !firstname.equals(that.firstname) : that.firstname != null) return false;
        return email.equals(that.email);

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (firstname != null ? firstname.hashCode() : 0);
        result = 31 * result + email.hashCode();
        return result;
    }
}
