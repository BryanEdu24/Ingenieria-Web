package es.ucm.fdi.iw.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class Note implements Transferable<Note.Transfer> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gen")
    @SequenceGenerator(name = "gen", sequenceName = "gen")
    private long id;

    private boolean enabled;

    private String author;

    @Column(nullable = false)
    private String message;

    @ManyToOne
    private Task task;

    @Getter
    @AllArgsConstructor
    public static class Transfer {
        private long id;
        private boolean enabled;
        private String author;
        private String message;
        private long taskId;
    }

    @Override
    public Transfer toTransfer() {
        return new Transfer(id, enabled, author, message, task.getId());
    }

    @Override
    public String toString() {
        return toTransfer().toString();
    }
}