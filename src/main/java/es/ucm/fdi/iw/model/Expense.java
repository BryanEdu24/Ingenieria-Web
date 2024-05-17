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
        @NamedQuery(name = "Expense.byHouse", query = "SELECT e FROM Expense e "
                + "WHERE e.house = :house AND e.enabled = TRUE"),
})

public class Expense implements Transferable<Expense.Transfer> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gen")
    @SequenceGenerator(name = "gen", sequenceName = "gen")
    private long id;

    private boolean enabled;

    private Double quantityByUser;

    private Double remainingQuantity;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Double quantity;

    @Column(nullable = false)
    private Date date;

    @ManyToOne(optional = false)
    private User author;

    @ManyToOne
    private House house;

    @Getter
    @AllArgsConstructor
    public static class Transfer {
        private long id;
        private String title;
        private User.Transfer author;
        private Double quantity;
        private Date date;
        private Long house_id;
        private Double quantityByUser;
        private Double remainingQuantity;
    }

    @Override
    public Transfer toTransfer() {
        return new Transfer(id, title, author.toTransfer(), quantity, date, house.getId(), quantityByUser,
                remainingQuantity);
    }

    @Override
    public String toString() {
        return toTransfer().toString();
    }
}
