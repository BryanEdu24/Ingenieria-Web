package es.ucm.fdi.iw.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.Arrays;

/**
 * An authorized user of the system.
 */

@Entity
@Data
@NoArgsConstructor
@NamedQueries({
        @NamedQuery(name = "User.byUsername", query = "SELECT u FROM User u "
                + "WHERE u.username = :username AND u.enabled = TRUE"),
        @NamedQuery(name = "User.hasUsername", query = "SELECT COUNT(u) "
                + "FROM User u "
                + "WHERE u.username = :username"),
        @NamedQuery(name = "User.byemail", query = "SELECT u FROM User u "
                + "WHERE u.email = :useremail")
})

@Table(name = "IWUser")
public class User implements Transferable<User.Transfer> {

    public enum Role {
        USER, // normal users
        ADMIN, // admin users
        MANAGER, // manager users
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gen")
    @SequenceGenerator(name = "gen", sequenceName = "gen")
    private long id;

    private boolean enabled;

    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false)
    private String password;

    private String email;

    private String roles; // split by ',' to separate roles

    @ManyToOne
    private House house;

    /**
     * Checks whether this user has a given role.
     *
     * @param role to check
     * @return true iff this user has that role.
     */
    public boolean hasRole(Role role) {
        String roleName = role.name();
        return Arrays.asList(roles.split(",")).contains(roleName);
    }

    @Getter
    @AllArgsConstructor
    public static class Transfer {
        private long id;
        private String username;
        private String email;
        private long house_id;
        private String roles;
    }

    @Override
    public Transfer toTransfer() {
        return new Transfer(id, username, email, house.getId(), roles);
    }

    @Override
    public String toString() {
        return toTransfer().toString();
    }
}
