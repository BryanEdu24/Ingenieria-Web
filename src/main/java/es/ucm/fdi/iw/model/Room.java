package es.ucm.fdi.iw.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@NamedQueries({
        @NamedQuery(name = "Room.byroomname", query = "SELECT r FROM Room r "
                + "WHERE r.name = :roomName AND r.enabled = TRUE"),
        @NamedQuery(name = "Room.byHouse", query = "SELECT r FROM Room r "
                + "WHERE r.house.id = :houseId AND r.enabled = TRUE")
})

public class Room implements Transferable<Room.Transfer> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gen")
    @SequenceGenerator(name = "gen", sequenceName = "gen")
    private long id;

    private boolean enabled;

    private String img;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    private House house;

    @Getter
    @AllArgsConstructor
    public static class Transfer {
        private long id;
        private String name;
        private long house_id;
        private String img;
    }

    @Override
    public Transfer toTransfer() {
        return new Transfer(id, name, house.getId(), img);
    }

    @Override
    public String toString() {
        return toTransfer().toString();
    }
}
