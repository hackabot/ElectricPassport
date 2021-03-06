package com.example.state;

import com.example.schema.IOUSchemaV1;
import com.google.common.collect.ImmutableList;
import net.corda.core.contracts.LinearState;
import net.corda.core.contracts.UniqueIdentifier;
import net.corda.core.identity.AbstractParty;
import net.corda.core.identity.Party;
import net.corda.core.schemas.MappedSchema;
import net.corda.core.schemas.PersistentState;
import net.corda.core.schemas.QueryableState;

import java.util.Arrays;
import java.util.List;

/**
 * The state object recording IOU agreements between two parties.
 *
 * A state must implement [ContractState] or one of its descendants.
 */
public class IOUState implements LinearState, QueryableState {
//    private final Integer value;
//    private final Party lender;
//    private final Party borrower;
//    private final UniqueIdentifier linearId;

    private final String FirstName;
    private final Party Customer;
    private final Party Org;
    private final String LastName;
    private final String DOB;
    private final UniqueIdentifier linearId;

    public IOUState(String firstName,  String lastName, String DOB,  Party org, Party customer) {
        FirstName = firstName;
        Customer = customer;
        Org = org;
        LastName = lastName;
        this.DOB = DOB;
        this.linearId = new UniqueIdentifier();

    }

    /**
     * @param value the value of the IOU.
     * @param lender the party issuing the IOU.
     * @param borrower the party receiving and approving the IOU.
     */
//    public IOUState(Integer value,
//                    Party lender,
//                    Party borrower)
//    {
//        this.value = value;
//        this.lender = lender;
//        this.borrower = borrower;
//        this.linearId = new UniqueIdentifier();
//    }

//    public Integer getValue() { return value; }
//    public Party getLender() { return lender; }
//    public Party getBorrower() { return borrower; }

    public String getFirstName() {
        return FirstName;
    }public Party getCustomer() {
        return Customer;
    }public Party getOrg() {
        return Org;
    }public String getLastName() {
        return LastName;
    }public String getDOB() {
        return DOB;
    }


    @Override public UniqueIdentifier getLinearId() { return linearId; }
//    @Override public List<AbstractParty> getParticipants() {
//        return Arrays.asList(lender, borrower);
//    }
    @Override public List<AbstractParty> getParticipants() {
        return Arrays.asList(Org, Customer);
    }

//    @Override public PersistentState generateMappedObject(MappedSchema schema) {
//        if (schema instanceof IOUSchemaV1) {
//            return new IOUSchemaV1.PersistentIOU(
//                    this.lender.getName().toString(),
//                    this.borrower.getName().toString(),
//                    this.value,
//                    this.linearId.getId());
//        } else {
//            throw new IllegalArgumentException("Unrecognised schema $schema");
//        }
//    }
 @Override public PersistentState generateMappedObject(MappedSchema schema) {
        if (schema instanceof IOUSchemaV1) {
            return new IOUSchemaV1.PersistentIOU(
                    this.Org.getName().toString(),
                    this.Customer.getName().toString(),
                    this.FirstName,
                    this.LastName,
                    this.DOB,
                    this.linearId.getId());
        } else {
            throw new IllegalArgumentException("Unrecognised schema $schema");
        }
    }

    @Override public Iterable<MappedSchema> supportedSchemas() {
        return ImmutableList.of(new IOUSchemaV1());
    }

    @Override
    public String toString() {
        return String.format("%s(iou=%s, lender=%s, borrower=%s, linearId=%s)", getClass().getSimpleName(), FirstName, LastName, DOB, Org, Customer, linearId);
    }
}