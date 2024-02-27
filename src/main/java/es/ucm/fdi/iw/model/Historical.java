package es.ucm.fdi.iw.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class Historical implements Transferable<Historical.Transfer> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gen")
    @SequenceGenerator(name = "gen", sequenceName = "gen")
    private long id;

    private boolean enabled;

    @Column(nullable = false)
    private String message;

    // 0 task, 1 expense
    private boolean type;

    @Getter
    @AllArgsConstructor
    public static class Transfer {
        private long id;
        private boolean type;
        private String message;
    }

    @Override
    public Transfer toTransfer() {
        return new Transfer(id, type, message);
    }

    @Override
    public String toString() {
        return toTransfer().toString();
    }
}
