package es.ucm.fdi.iw.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class UserExpense implements Transferable<UserExpense.Transfer> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gen")
    @SequenceGenerator(name = "gen", sequenceName = "gen")
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "expense_id")
    private Expense expense;

    @Getter
    @AllArgsConstructor
    public static class Transfer {
        private long userId;
        private long expenseId;
    }

    @Override
    public Transfer toTransfer() {
        return new Transfer(user.getId(), expense.getId());
    }

    @Override
    public String toString() {
        return toTransfer().toString();
    }
}
