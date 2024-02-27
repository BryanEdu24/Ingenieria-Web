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
public class Expense implements Transferable<Expense.Transfer> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gen")
    @SequenceGenerator(name = "gen", sequenceName = "gen")
    private long id;

    private boolean enabled;

    private String title;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private Float quantity;

    @Column(nullable = false)
    private Date date;

    @Getter
    @AllArgsConstructor
    public static class Transfer {
        private long id;
        private String author;
        private Float quantity;
        private Date date;
    }

    @Override
    public Transfer toTransfer() {
        return new Transfer(id, author, quantity, date);
    }

    @Override
    public String toString() {
        return toTransfer().toString();
    }
}
