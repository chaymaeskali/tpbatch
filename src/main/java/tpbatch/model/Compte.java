package tpbatch.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class Compte {

    @Id
    private Long idCompte;

    private float solde;

    @OneToMany(mappedBy="Compte")
    private List<Transaction> transactionList;


}
