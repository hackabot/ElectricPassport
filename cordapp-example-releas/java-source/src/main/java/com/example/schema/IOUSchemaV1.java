package com.example.schema;

import com.google.common.collect.ImmutableList;
import net.corda.core.schemas.MappedSchema;
import net.corda.core.schemas.PersistentState;
import net.corda.core.schemas.PersistentStateRef;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.UUID;

/**
 * An IOUState schema.
 */
public class IOUSchemaV1 extends MappedSchema {
    public IOUSchemaV1() {
        super(IOUSchema.class, 1, ImmutableList.of(PersistentIOU.class));
    }

    @Entity
    @Table(name = "iou_states")
    public static class PersistentIOU extends PersistentState {
//        @Column(name = "lender") private final String lender;
//        @Column(name = "borrower") private final String borrower;
//        @Column(name = "value") private final int value;
//        @Column(name = "linear_id") private final UUID linearId;

        @Column(name = "Org") private final String Org;
        @Column(name = "Customer") private final String Customer;
        @Column(name = "FirstName") private final String FirstName;
        @Column(name = "LastName") private final String LastName;
        @Column(name = "DOB") private final String DOB;
        @Column(name = "linearId") private final UUID linearId;

        /* @doubt should we keep the Column name all small letters ? */

        public PersistentIOU(String org, String customer, String firstName, String lastName, String DOB, UUID linearId) {
            Org = org;
            Customer = customer;
            FirstName = firstName;
            LastName = lastName;
            this.DOB = DOB;
           // this.docSubmitted = docSubmitted;
            this.linearId = linearId;
        }

        public String getOrg() {
            return Org;
        }

        public String getCustomer() {
            return Customer;
        }

        public String getFirstName() {
            return FirstName;
        }

        public String getLastName() {
            return LastName;
        }

        public String getDOB() {
            return DOB;
        }



        public UUID getLinearId() {
            return linearId;
        }

        //        public PersistentIOU(String lender, String borrower, int value, UUID linearId) {
//            this.lender = lender;
//            this.borrower = borrower;
//            this.value = value;
//            this.linearId = linearId;
//        }
//
//        public String getLender() {
//            return lender;
//        }
//
//        public String getBorrower() {
//            return borrower;
//        }
//
//        public int getValue() {
//            return value;
//        }
//
//        public UUID getId() {
//            return linearId;
//        }
    }
}