package es.ucm.fdi.iw.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@NamedQueries({
        @NamedQuery(name = "House.byHousename", query = "SELECT h FROM House h "
                + "WHERE h.name = :name AND h.enabled = TRUE"),
        @NamedQuery(name = "House.allHouses", query = "SELECT h FROM House h "
                + "WHERE h.enabled = :status")

})
public class House implements Transferable<House.Transfer> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gen")
    @SequenceGenerator(name = "gen", sequenceName = "gen")
    private long id;

    private boolean enabled;

    @Column(nullable = false)
    private String pass;
    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany
    @JoinColumn(name = "house_id")
    private List<User> users;

    @OneToMany
    @JoinColumn(name = "house_id")
    private List<Room> rooms;

    @Getter
    @AllArgsConstructor
    public static class Transfer {
        private long id;
        private String name;
        private List<User> users;
        private List<Room> rooms;
    }

    @Override
    public Transfer toTransfer() {
        return new Transfer(id, name, users, rooms);
    }

    @Override
    public String toString() {
        return toTransfer().toString();
    }
}
