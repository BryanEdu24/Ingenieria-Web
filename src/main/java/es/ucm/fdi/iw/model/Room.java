package es.ucm.fdi.iw.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class Room implements Transferable<Room.Transfer> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gen")
    @SequenceGenerator(name = "gen", sequenceName = "gen")
    private long id;

    private boolean enabled;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToOne
    @JoinColumn(name = "house_id")
    private House house;

    @Getter
    @AllArgsConstructor
    public static class Transfer {
        private long id;
        private String name;
        private long houseId;
    }

    @Override
    public Transfer toTransfer() {
        return new Transfer(id, name, house.getId());
    }

    @Override
    public String toString() {
        return toTransfer().toString();
    }
}
