package com.example.flow;

import co.paralleluniverse.fibers.Suspendable;
import com.example.state.IOUState;
import net.corda.core.contracts.StateAndRef;
import net.corda.core.flows.*;
import net.corda.core.node.services.Vault;
import net.corda.core.transactions.SignedTransaction;

import java.util.List;

import static java.lang.System.out;

public class VerifyPrevFlow {

    @InitiatingFlow
    @StartableByRPC
    public static class PrevStateFlow extends FlowLogic<List<StateAndRef<IOUState>>> {

//        private final FlowSession otherPartyFlow;
//        public PrevStateFlow(FlowSession otpf){
//            otherPartyFlow = otpf;
//        }
        @Override
        @Suspendable
        public List<StateAndRef<IOUState>> call() throws FlowException {

            Vault.Page<IOUState> results = getServiceHub().getVaultService().queryBy(IOUState.class);

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

            return results.getStates();
        }
    }
}
