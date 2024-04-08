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
            + "WHERE t.room.house = :house"
            + " order by t.creationDate"),
    @NamedQuery(name = "Task.byUser", query = "SELECT t "
            + "FROM Task t "
            + "WHERE t.user = :userId"),
    @NamedQuery(name = "Task.byId", query = "SELECT t "
            + "FROM Task t "
            + "WHERE t.id = :taskId"),
    @NamedQuery(name = "Task.byRoom", query = "SELECT t "
            + "FROM Task t "
            + "WHERE t.room.id = :roomId")
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
        private String author;
        private Boolean enabled;
        private long userId;
        private long roomId;
        private Date creationDate;
    }

    @Override
    public Transfer toTransfer() {
        return new Transfer(id, title, author, enabled, user.getId(), room.getId(), creationDate);
    }

    @Override
    public String toString() {
        return toTransfer().toString();
    }
}