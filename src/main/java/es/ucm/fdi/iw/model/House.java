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

    private boolean enabled;

    @Column(nullable = false)
    private String pass;
    @Column(nullable = false, unique = true)
    private String name;

    @Getter
    @AllArgsConstructor
    public static class Transfer {
        private long id;
        private String name;
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
