package es.ucm.fdi.iw.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class Historical implements Transferable<Historical.Transfer> {

    public enum Type {
        TASK,
        EXPENSE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gen")
    @SequenceGenerator(name = "gen", sequenceName = "gen")
    private long id;

    @Column(nullable = false)
    private String message;

    private String type;

    @ManyToOne(optional = true)
    private House house;

    /**
     * Checks whether this historical has a given type.
     *
     * @param ty to check
     * @return true iff this historical has that type.
     */
    public boolean hasType(Type ty) {
        String typeName = ty.name();
        return Arrays.asList(type.split(",")).contains(typeName);
    }

    @Getter
    @AllArgsConstructor
    public static class Transfer {
        private long id;
        private String type;
        private String message;
        private Long house_id;
    }

    @Override
    public Transfer toTransfer() {
        return new Transfer(id, type, message, house.getId());
    }

    @Override
    public String toString() {
        return toTransfer().toString();
    }
}
