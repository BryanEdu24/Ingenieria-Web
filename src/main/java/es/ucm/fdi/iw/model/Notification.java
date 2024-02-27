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
    private House user;

    @Getter
    @AllArgsConstructor
    public static class Transfer {
        private long id;
        private String message;
        private Date date;
        private boolean read;
    }

    @Override
    public Transfer toTransfer() {
        return new Transfer(id, message, date, read);
    }

    @Override
    public String toString() {
        return toTransfer().toString();
    }
}
