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
                + "WHERE n.house.id= :houseId AND (n.user.id = :userId OR n.user.id IS NULL) AND n.read = FALSE "),
        @NamedQuery(name = "Notification.unReadCompleted", query = "SELECT n FROM Notification n "
                + "WHERE n.house.id= :houseId AND (n.user.id = :user_id OR n.user.id IS NULL) AND n.read = FALSE ")
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
    private User user; // Usuario que recibe la notificación, null si es para todos

    @ManyToOne
    @JoinColumn(name = "house_id")
    private House house;

    @Getter
    @AllArgsConstructor
    public static class Transfer {
        private long id;
        private String message;
        private Date date;
        private boolean read;
        private long userId;
        private long houseId;
    }

    @Override
    public Transfer toTransfer() {
        if(user != null){
            return new Transfer(id, message, date, read, user.getId(), house.getId());
        } else{ //Se devuleve -1 para las notis que no van dirigidas a nadie
            return new Transfer(id, message, date, read, -1, house.getId());
        }
    }

    @Override
    public String toString() {
        return toTransfer().toString();
    }
}
