package tpbatch.batch;

import java.text.SimpleDateFormat;

import org.springframework.batch.item.ItemProcessor;
import tpbatch.model.Transaction;
import tpbatch.model.TransactionDto;

public class Processor implements ItemProcessor<TransactionDto,TransactionDto> {
    @Override
    public TransactionDto process(TransactionDto transactionDto) throws Exception {
        //System.out.println("Inserting Employee info: " + transaction);

        transactionDto.setDateTransaction(dateFormat.parse(transactionDto.getDateTransaction()));
        transactionDto.setIdTransaction(transactionDto.getIdTransaction());
        transactionDto.setIdCompte(transactionDto.getIdCompte());
        transactionDto.setMontant(transactionDto.getMontant());

        return transactionDto;

    }
}
