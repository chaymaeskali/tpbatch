package tpbatch.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import tpbatch.model.Compte;

import java.util.Date;
@Entity @Data @NoArgsConstructor @AllArgsConstructor @ToString
public class Transaction {
    @Id
    private Long idTransaction;
    private float montant;
    private Date dateTransaction;
    private Date dateDebit;


    @ManyToOne
    @JoinColumn(name="id_compte")
    private Compte compte;



}
