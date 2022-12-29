package tpbatch.batch;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tpbatch.model.Compte;
import tpbatch.model.Transaction;
import tpbatch.model.TransactionDto;
import tpbatch.repository.CompteRepository;
import tpbatch.repository.TransactionRepository;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
public class DbWriter implements ItemWriter<Transaction> {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private CompteRepository compteRepository;
    @Override
    public void write(List<? extends TransactionDto> transactions) throws Exception {
       /* System.out.println("Data Saved for Transactions: " + transactions);
        transactionRepository.saveAll(transactions);*/
        for(int i=0;i<transactions.size();i++)
        {  Transaction transaction = new Transaction();
            //stocker date transition dans calender
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(transactions.get(i).getDateTransaction());

            //get the day and check it
            if(calendar.get(Calendar.DAY_OF_MONTH)<25)
            {
                //add days to move to the first day of next month
                calendar.add(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH)-calendar.get(Calendar.DAY_OF_MONTH)+1);

            }else
            {    //add days to move to the first day of next month
                calendar.add(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH)-calendar.get(Calendar.DAY_OF_MONTH)+1);
                //then add one other month
                calendar.add(Calendar.MONTH, 1);
            }
            //set the time of dateDebit to 08:30
            Date dateDebit = calendar.getTime();
            dateDebit.setHours(8);
            dateDebit.setMinutes(30);

            transaction.setDateDebit(dateDebit);
            transaction.setIdTransaction(transactions.get(i).getIdTransaction());
            transaction.setDateTransaction(transactions.get(i).getDateTransaction());
            transaction.setMontant(transactions.get(i).getMontant());

            transactionRepository.save(transaction);

            //je dois avoir des comptes dans ma bd pour faire cette partie
            Compte compte = compteRepository.getById(transactions.get(i).getIdCompte());
            compte.setSolde(compte.getSolde()-transactions.get(i).getMontant());

            compteRepository.save(compte);


        }

    }
}
