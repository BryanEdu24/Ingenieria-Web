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
        @NamedQuery(name = "UserExpense.byUser", query = "SELECT ue FROM UserExpense ue "
                + "WHERE ue.user =: user AND ue.paid = False"),
        @NamedQuery(name = "UserExpense.byUserAndExpense", query = "SELECT ue FROM UserExpense ue "
                + "WHERE ue.user.id =: user AND ue.expense.id =: expense"),
        @NamedQuery(name = "UserExpense.checkPaid", query = "SELECT ue FROM UserExpense ue "
                + "WHERE ue.expense.id =: expenseId AND ue.paid = False"),
})
public class UserExpense implements Transferable<UserExpense.Transfer> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gen")
    @SequenceGenerator(name = "gen", sequenceName = "gen")
    private long id;

    private boolean paid;

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
        private Expense.Transfer expenseT;
        private boolean paid;
        private long id;
    }

    @Override
    public Transfer toTransfer() {
        return new Transfer(user.getId(), expense.toTransfer(), paid, id);
    }

    @Override
    public String toString() {
        return toTransfer().toString();
    }
}
