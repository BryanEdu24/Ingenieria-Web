package es.ucm.fdi.iw.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@NamedQueries({
        @NamedQuery(name = "Notification.unRead", query = "SELECT COUNT(*) FROM Notification n "
                + "WHERE n.user.id = :userId AND n.enabled = true AND n.read = false"),
        @NamedQuery(name = "Notification.userNotifications", query = "SELECT n FROM Notification n "
                + "WHERE n.user.id = :userId AND n.enabled = true AND n.read = false")
})

public class Notification implements Transferable<Notification.Transfer> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gen")
    @SequenceGenerator(name = "gen", sequenceName = "gen")
    private long id;

    private boolean enabled;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private Date date;

    private boolean read;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Getter
    @AllArgsConstructor
    public static class Transfer {
        private long id;
        private String message;
        private Date date;
        private boolean read;
        private long userId;
    }

    @Override
    public Transfer toTransfer() {
        return new Transfer(id, message, date, read, user.getId());
    }

    @Override
    public String toString() {
        return toTransfer().toString();
    }
}
