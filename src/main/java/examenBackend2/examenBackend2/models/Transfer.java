package examenBackend2.examenBackend2.models;

import java.util.UUID;

import lombok.Data;

@Data
public class Transfer {
    private UUID id; 
    private UUID originAccount;
    private UUID destinationAccount;
    private Double amount;


    public Transfer(Double amount) {
        this.amount = amount;
        this.destinationAccount = UUID.randomUUID();
        this.originAccount = UUID.randomUUID();
        this.id = UUID.randomUUID();
    }

}
