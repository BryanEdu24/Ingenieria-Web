package es.ucm.fdi.iw.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.Arrays;
import java.util.List;

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
                + "WHERE u.email = :useremail"),
        @NamedQuery(name = "User.byHouse", query = "SELECT u FROM User u "
                + "WHERE u.house = :house")
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

    private String roles;

    private double balance;

    @ManyToOne(optional = true)
    private House house;

    @OneToMany // Gatos que tú creas
    private List<Expense> expenses;

    @OneToMany // lo que debe
    private List<UserExpense> userexpenses;

    @OneToMany
    private List<Notification> notifications;

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
        private Long house_id;
        private String roles;
        private Double balance;
    }

    @Override
    public Transfer toTransfer() {
        if (house == null)
            return new Transfer(id, username, email, null, roles, balance);
        else
            return new Transfer(id, username, email, house.getId(), roles, balance);
    }

    @Override
    public String toString() {
        return toTransfer().toString();
    }
}
