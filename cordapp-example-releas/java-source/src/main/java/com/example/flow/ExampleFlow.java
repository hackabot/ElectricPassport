package com.example.flow;

import co.paralleluniverse.fibers.Suspendable;
import com.example.contract.IOUContract;
import com.example.state.IOUState;
import com.google.common.collect.Sets;
import com.sun.org.apache.xpath.internal.operations.Bool;
import net.corda.core.contracts.Command;
import net.corda.core.contracts.ContractState;
import net.corda.core.contracts.StateAndContract;
import net.corda.core.contracts.StateAndRef;
import net.corda.core.flows.*;
import net.corda.core.identity.AbstractParty;
import net.corda.core.identity.Party;
import net.corda.core.node.services.Vault;
import net.corda.core.transactions.SignedTransaction;
import net.corda.core.transactions.TransactionBuilder;
import net.corda.core.utilities.ProgressTracker;
import net.corda.core.utilities.ProgressTracker.Step;
import net.corda.core.utilities.UntrustworthyData;

import java.sql.ResultSet;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.contract.IOUContract.IOU_CONTRACT_ID;
import static java.lang.System.out;
import static net.corda.core.contracts.ContractsDSL.requireThat;

/**
 * This flow allows two parties (the [Initiator] and the [Acceptor]) to come to an agreement about the IOU encapsulated
 * within an [IOUState].
 * <p>
 * In our simple example, the [Acceptor] always accepts a valid IOU.
 * <p>
 * These flows have deliberately been implemented by using only the call() method for ease of understanding. In
 * practice we would recommend splitting up the various stages of the flow into sub-routines.
 * <p>
 * All methods called within the [FlowLogic] sub-class need to be annotated with the @Suspendable annotation.
 */
public class ExampleFlow {
    @InitiatingFlow
    @StartableByRPC
    public static class Initiator extends FlowLogic<SignedTransaction> {

//        private final int iouValue;
//        private final Party otherParty;

        private final String DOB;
        private final String FirstName;
        private final String LastName;
        private final Party Customer;

        public Initiator( String firstName, String lastName, String DOB, Party customer) {
            this.DOB = DOB;
            FirstName = firstName;
            LastName = lastName;
            Customer = customer;
        }

        private final Step GENERATING_TRANSACTION = new Step("Generating transaction based on new IOU.");
        private final Step VERIFYING_TRANSACTION = new Step("Verifying contract constraints.");
        private final Step SIGNING_TRANSACTION = new Step("Signing transaction with our private key.");
        private final Step GATHERING_SIGS = new Step("Gathering the counterparty's signature.") {
            @Override public ProgressTracker childProgressTracker() {
                return CollectSignaturesFlow.Companion.tracker();
            }
        };
        private final Step FINALISING_TRANSACTION = new Step("Obtaining notary signature and recording transaction.") {
            @Override public ProgressTracker childProgressTracker() {
                return FinalityFlow.Companion.tracker();
            }
        };
        
        // The progress tracker checkpoints each stage of the flow and outputs the specified messages when each
        // checkpoint is reached in the code. See the 'progressTracker.currentStep' expressions within the call()
        // function.
        private final ProgressTracker progressTracker = new ProgressTracker(
                GENERATING_TRANSACTION,
                VERIFYING_TRANSACTION,
                SIGNING_TRANSACTION,
                GATHERING_SIGS,
                FINALISING_TRANSACTION
        );
//
//        public Initiator(int iouValue, Party otherParty) {
//            this.iouValue = iouValue;
//            this.otherParty = otherParty;
//        }

        @Override
        public ProgressTracker getProgressTracker() {
            return progressTracker;
        }

        /**
         * The flow logic is encapsulated within the call() method.
         */
        @Suspendable
        @Override
        public SignedTransaction call() throws FlowException {
            // Obtain a reference to the notary we want to use.
            final Party notary = getServiceHub().getNetworkMapCache().getNotaryIdentities().get(0);


            //Adding the input state of the customer :
            //Get input state from the customer
//            FlowSession getPreviousStateSession = initiateFlow(Customer);
            FlowSession otherPartySession = initiateFlow(Customer);

            UntrustworthyData<IOUState> ppp = new UntrustworthyData<>(null);
            Class c = ppp.getClass();
//            UntrustworthyData<? extends IOUState> prevState1 = otherPartySession.receive(ppp.getClass());

            UntrustworthyData<IOUState> prevState1 = otherPartySession.receive(IOUState.class);

//            subFlow(new PrevStateFlow(), Sets.newHashSet(getPreviousStateSession));
//            Boolean kyc_done_before = subFlow(new PrevStateFlow(getPreviousStateSession));
            List<StateAndRef<IOUState>> prevState = subFlow(new PrevStateFlow(otherPartySession));
           // StateAndRef<IOUState> prevState = null;

            // Perform checking on the object received.
            // T O D O: Check the received object.
            // Return the object.
//            List<IOUState> prevState = null;
//            try{
//                prevState1 = prevState1.unwrap(data -> {
////                    data.getFirstName();
////                    data.getLastName();
////                    data.getLinearId();
////                    data.getDOB();
//
//                    return (List<IOUState>) data;
//                  //  return data;
//                } , prevState);
//            }
//
//            catch (Exception e){
//                e.printStackTrace();
//            }

            // Stage 1.
            progressTracker.setCurrentStep(GENERATING_TRANSACTION);
            // Generate an unsigned transaction.
            IOUState iouState = new IOUState(FirstName, LastName, DOB, getServiceHub().getMyInfo().getLegalIdentities().get(0), Customer);
            final Command<IOUContract.Commands.Create> txCommand = new Command<>(new IOUContract.Commands.Create(),
                    iouState.getParticipants().stream().map(AbstractParty::getOwningKey).collect(Collectors.toList()));
            final TransactionBuilder txBuilder = new TransactionBuilder(notary).withItems(new StateAndContract(iouState, IOU_CONTRACT_ID), txCommand);
            if(prevState.size() != 0)
                txBuilder.addInputState(prevState.get(0));



            // Stage 2.
            progressTracker.setCurrentStep(VERIFYING_TRANSACTION);
            // Verify that the transaction is valid.
            txBuilder.verify(getServiceHub());

            // Stage 3.
            progressTracker.setCurrentStep(SIGNING_TRANSACTION);
            // Sign the transaction.
            final SignedTransaction partSignedTx = getServiceHub().signInitialTransaction(txBuilder);


//            FlowSession otherPartySession = initiateFlow(Customer);

            // Stage 4.
            progressTracker.setCurrentStep(GATHERING_SIGS);
            // Send the state to the counterparty, and receive it back with their signature.
            final SignedTransaction fullySignedTx = subFlow(
                    new CollectSignaturesFlow(partSignedTx, Sets.newHashSet(otherPartySession), CollectSignaturesFlow.Companion.tracker()));

            // Stage 5.
            progressTracker.setCurrentStep(FINALISING_TRANSACTION);
            // Notarise and record the transaction in both parties' vaults.
            return subFlow(new FinalityFlow(fullySignedTx));
        }
    }

    @InitiatingFlow
    @InitiatedBy(Initiator.class)
    public static class PrevStateFlow extends FlowLogic<List<StateAndRef<IOUState>>> {

        private final FlowSession otherPartyFlow;
        public PrevStateFlow(FlowSession otpf){
            otherPartyFlow = otpf;
        }
        @Override
        @Suspendable
        public List<StateAndRef<IOUState>> call() throws FlowException {

            Vault.Page<IOUState> results = getServiceHub().getVaultService().queryBy(IOUState.class);
//            return results.getStates().get(0);

//            FlowSession getPreviousStateSession = initiateFlow(Org);

            if(results == null){
                out.println("HAKKA : Results Null");
            }
            else{
                out.println("HAKKA : " +  results);
            }

            if(results.getStates() == null){
                out.println("Hakka : getstates null");
            }
            else{
                out.println("HAKKA : " + results.getStates());
            }

//            if(results.getStates().get(0) ==  null){
//                out.println("HAKKA : Get 0 null");
//            }
//            else {
//                out.println("HAKKA : " + results.getStates().get(0));
//            }

//            otherPartyFlow.send(results.getStates());

            return results.getStates();
        }
    }

    @InitiatedBy(Initiator.class)
    public static class Acceptor extends FlowLogic<SignedTransaction> {

        private final FlowSession otherPartyFlow;

        public Acceptor(FlowSession otherPartyFlow) {
            this.otherPartyFlow = otherPartyFlow;
        }

        @Suspendable
        @Override
        public SignedTransaction call() throws FlowException {
            class SignTxFlow extends SignTransactionFlow {
                private SignTxFlow(FlowSession otherPartyFlow, ProgressTracker progressTracker) {
                    super(otherPartyFlow, progressTracker);
                }

                @Override
                protected void checkTransaction(SignedTransaction stx) {
                    requireThat(require -> {
                        ContractState output = stx.getTx().getOutputs().get(0).getData();
                        require.using("This must be an IOU transaction.", output instanceof IOUState);
                        IOUState iou = (IOUState) output;
                       // require.using("I won't accept IOUs with a value over 100.", Integer.parseInt(iou.getDOB()) <= 100);
                        return null;
                    });
                }
            }

            return subFlow(new SignTxFlow(otherPartyFlow, SignTransactionFlow.Companion.tracker()));
        }
    }
}
