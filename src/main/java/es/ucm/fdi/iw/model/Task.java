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
    @NamedQuery(name = "Task.forHouse", query = "SELECT t "
            + "FROM Task t "
            + "WHERE t.room.house = :house"),
    @NamedQuery(name = "Task.byUser", query = "SELECT t "
            + "FROM Task t "
            + "WHERE t.user = :userId"),
    @NamedQuery(name = "Task.byId", query = "SELECT t "
            + "FROM Task t "
            + "WHERE t.id = :taskId")
})
public class Task implements Transferable<Task.Transfer> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gen")
    @SequenceGenerator(name = "gen", sequenceName = "gen")
    private long id;

    private boolean enabled;

    @Column(nullable = false, unique = true)
    private String title;

    @Column(nullable = false, unique = false)
    private String author;

    @Column(nullable = true, unique = false)
    private Date creationDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @Getter
    @AllArgsConstructor
    public static class Transfer {
        private long id;
        private String title;
        private long userId;
        private long roomId;
    }

    @Override
    public Transfer toTransfer() {
        return new Transfer(id, title, user.getId(), room.getId());
    }

    @Override
    public String toString() {
        return toTransfer().toString();
    }
}