package es.ucm.fdi.iw.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class House implements Transferable<House.Transfer> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gen")
    @SequenceGenerator(name = "gen", sequenceName = "gen")
    private long id;

    @Column(nullable = false, unique = true)
    private String name;
    @Column(nullable = false)
    private String pass;

    private boolean enabled;

    @Getter
    @AllArgsConstructor
    public static class Transfer {
        private long id;
        private String username;
    }

    @Override
    public Transfer toTransfer() {
        return new Transfer(id, name);
    }

    @Override
    public String toString() {
        return toTransfer().toString();
    }
}
