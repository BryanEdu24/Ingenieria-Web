package es.ucm.fdi.iw.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@NamedQueries({
                @NamedQuery(name = "Task.forHouse", query = "SELECT t "
                                + "FROM Task t "
                                + "WHERE t.room.house = :house AND t.enabled= true"
                                + " order by t.creationDate"),
                @NamedQuery(name = "Task.byUser", query = "SELECT t "
                                + "FROM Task t "
                                + "WHERE t.user = :user AND t.enabled= true"),
                @NamedQuery(name = "Task.byId", query = "SELECT t "
                                + "FROM Task t "
                                + "WHERE t.id = :taskId"),
                @NamedQuery(name = "Task.byRoom", query = "SELECT t "
                                + "FROM Task t "
                                + "WHERE t.room.id = :roomId AND t.enabled= true"),
                @NamedQuery(name = "Task.nTasksbyUser", query = "SELECT COUNT(*) "
                                + "FROM Task t "
                                + "WHERE t.user = :user AND t.enabled= true"),
})
public class Task implements Transferable<Task.Transfer> {
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gen")
        @SequenceGenerator(name = "gen", sequenceName = "gen")
        private long id;

        private boolean enabled;

        private boolean done;

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

        @OneToMany
        @JoinColumn(name = "task_id")
        private List<Note> notes;

        @Getter
        @AllArgsConstructor
        public static class Transfer {
                private long id;
                private String title;
                private String author;
                private Boolean enabled;
                private User.Transfer userT;
                private Room.Transfer room;
                private Date creationDate;
                private List<Note.Transfer> notes;
                private boolean done;
        }

        @Override
        public Transfer toTransfer() {
                List<Note.Transfer> aux = new ArrayList<Note.Transfer>();
                if (notes != null) {
                        for (Note i : notes) {
                                aux.add(i.toTransfer());
                        }
                }
                return new Transfer(id, title, author, enabled, user.toTransfer(), room.toTransfer(), creationDate, aux, done);
        }

        @Override
        public String toString() {
                return toTransfer().toString();
        }
}