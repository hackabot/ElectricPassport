//package com.example.api;
//
//
//// OOOOOOOOOOOOOOOOLA AL LA LA AL AA LA
//
//import com.example.contract.IOUContract;
//import com.example.flow.ExampleFlow;
//import com.example.state.IOUState;
//import com.google.common.collect.ImmutableList;
//import com.google.common.collect.ImmutableMap;
//import net.corda.core.contracts.LinearState;
//import net.corda.core.contracts.StateAndRef;
//import net.corda.core.contracts.StateRef;
//import net.corda.core.contracts.UniqueIdentifier;
//import net.corda.core.crypto.SecureHash;
//import net.corda.core.identity.CordaX500Name;
//import net.corda.core.identity.Party;
//import net.corda.core.messaging.CordaRPCOps;
//import net.corda.core.messaging.FlowProgressHandle;
//import net.corda.core.node.NodeInfo;
//import net.corda.core.node.services.Vault;
//import net.corda.core.transactions.SignedTransaction;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import javax.ws.rs.*;
//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.Response;
//import java.io.IOException;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.ExecutionException;
//
//import static java.util.stream.Collectors.toList;
//import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
//import static javax.ws.rs.core.Response.Status.CREATED;
//
//// This API is accessible from /api/example. All paths specified below are relative to it.
//@Path("example")
//public class ExampleApi {
//    private final CordaRPCOps rpcOps;
//    private final CordaX500Name myLegalName;
//
//
//    private final List<String> serviceNames = ImmutableList.of("Controller", "Network Map Service");
//
//  //  private final List<String> vaultVals = ImmutableList.of("Controller","");
//
//    static private final Logger logger = LoggerFactory.getLogger(ExampleApi.class);
//    private Party party;
//
//    public ExampleApi(CordaRPCOps rpcOps) {
//        this.rpcOps = rpcOps;
//        this.myLegalName = rpcOps.nodeInfo().getLegalIdentities().get(0).getName();
//
//    }
//
//    /**
//     * Returns the node's name.
//     */
//    @GET
//    @Path("me")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Map<String, CordaX500Name> whoami() {
//        return ImmutableMap.of("me", myLegalName);
//    }
//
//    /**
//     * Returns all parties registered with the [NetworkMapService]. These names can be used to look up identities
//     * using the [IdentityService].
//     */
//    @GET
//    @Path("peers")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Map<String, List<CordaX500Name>> getPeers() {
//        List<NodeInfo> nodeInfoSnapshot = rpcOps.networkMapSnapshot();
//        return ImmutableMap.of("peers", nodeInfoSnapshot
//                .stream()
//                .map(node -> node.getLegalIdentities().get(0).getName())
//                .filter(name -> !name.equals(myLegalName) && !serviceNames.contains(name.getOrganisation()))
//                .collect(toList()));
//    }
//
//    /**
//     * Display state of Vaults in distributed ledger using StateMetadata
//     */
//
////    @GET
////    @Path("vaultTransact")
////    @Produces(MediaType.APPLICATION_JSON)
////    public List<StateAndRef<IOUState>> getVaultSupport(){
////
////        return rpcOps.getVaultTransactionNotes().getClass();
////        //rpcOps.nodeInfo().getLegalIdentities().getClass();
////        //party = rpcOps.nodeInfo().getLegalIdentities().get(0);
////        //return rpcOps.getVaultTransactionNotes(); //find securehash ID
////    }
////
////    /**
////     * Return current node time
////     */
////    @GET
////    @Path("nodeTime")
////    @
//
//    /**
//     * Returns vault state metadata
//     */
//    @GET
//    @Path("states")
//    @Produces(MediaType.APPLICATION_JSON)
//    public List<Vault.StateMetadata> getMetaData(){
//        return rpcOps.vaultQuery(IOUState.class).getStatesMetadata();
//        //return rpcOps.vaultQuery(query,true);
//    }
//
//    /**
//     * Returns Vault Track
//     *
//     */
//
//    @GET
//    @Path("vaultTrack")
//    @Produces(MediaType.APPLICATION_JSON)
//    public List<StateAndRef<IOUState>> getTrack() {
//        return rpcOps.vaultTrack(IOUState.class).getSnapshot().getStates();
//    }
//
//    /**
//     * Displays all KYC details that exist in the node's vault.
//     */
//    @GET
//    @Path("ious")
//    @Produces(MediaType.APPLICATION_JSON)
//    public List<StateAndRef<IOUState>> getIOUs() {
//        return rpcOps.vaultQuery(IOUState.class).getStates();
//    }
//
//    /**
//     * Display Parties participating in the txn. --- PartiesFromName
//     */
////    @GET
////    @Path("partyNames")
////    @Produces(MediaType.APPLICATION_JSON)
////    public List<StateAndRef> getPartyNames(){
////        return rpcOps.partiesFromName();
////    }
//
//
//    /**
//     * Initiates a flow to agree an IOU between two parties.
//     *
//     * Once the flow finishes it will have written the IOU to ledger. Both the lender and the borrower will be able to
//     * see it when calling /api/example/ious on their respective nodes.
//     *
//     * This end-point takes a Party name parameter as part of the path. If the serving node can't find the other party
//     * in its network map cache, it will return an HTTP bad request.
//     *
//     * The flow is invoked asynchronously. It returns a future when the flow's call() method returns.
//     */
//
//    /* ElectricPassport
//        Changing the Query params to accomodate our needs
//
//     */
//
//
//
//    @PUT
//    @Path("create-iou")
//    public Response createIOU(
//
//            @QueryParam("fisrt_name") String firstName,
//            @QueryParam("last_name") String lastName,
//            @QueryParam("dob") String dob,
//            @QueryParam("customer") CordaX500Name customer)
//            throws InterruptedException, ExecutionException {
//
////        if (iouValue <= 0) {
////            return Response.status(BAD_REQUEST).entity("Query parameter 'iouValue' must be non-negative.\n").build();
////        }
//        if (customer == null) {
//            return Response.status(BAD_REQUEST).entity("Query parameter 'partyName' missing or has wrong format.\n").build();
//        }
//
//        final Party otherParty = rpcOps.wellKnownPartyFromX500Name(customer);
//        if (otherParty == null) {
//            return Response.status(BAD_REQUEST).entity("Party named " + customer + "cannot be found.\n").build();
//        }
//
//        try {
//            FlowProgressHandle<SignedTransaction> flowHandle = rpcOps
//                    .startTrackedFlowDynamic(ExampleFlow.Initiator.class, firstName, lastName, dob, otherParty);
//            flowHandle.getProgress().subscribe(evt -> System.out.printf(">> %s\n", evt));
//
//            // The line below blocks and waits for the flow to return.
//            final SignedTransaction result = flowHandle
//                    .getReturnValue()
//                    .get();
//
//            final String msg = String.format("Transaction id %s committed to ledger.\n", result.getId());
//            return Response.status(CREATED).entity(msg).build();
//
//        } catch (Throwable ex) {
//            final String msg = ex.getMessage();
//            logger.error(ex.getMessage(), ex);
//            return Response.status(BAD_REQUEST).entity(msg).build();
//        }
//    }
//}

package com.example.api;



import com.example.flow.ExampleFlow;
import com.example.flow.VerifyPrevFlow;
import com.example.state.IOUState;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.corda.core.contracts.StateAndRef;
import net.corda.core.identity.CordaX500Name;
import net.corda.core.identity.Party;
import net.corda.core.messaging.CordaRPCOps;
import net.corda.core.messaging.FlowProgressHandle;
import net.corda.core.node.NodeInfo;
import net.corda.core.node.ServiceHub;
import net.corda.core.transactions.SignedTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import net.corda.core.node.services.Vault;
import static java.util.stream.Collectors.toList;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.CREATED;

// This API is accessible from /api/example. All paths specified below are relative to it.
@Path("example")
public class ExampleApi {
    private final CordaRPCOps rpcOps;
    private final CordaX500Name myLegalName;

    private final List<String> serviceNames = ImmutableList.of("Controller", "Network Map Service");

    static private final Logger logger = LoggerFactory.getLogger(ExampleApi.class);

    public ExampleApi(CordaRPCOps rpcOps) {
        this.rpcOps = rpcOps;
        this.myLegalName = rpcOps.nodeInfo().getLegalIdentities().get(0).getName();
    }

    /**
     * Returns the node's name.
     */
    @GET
    @Path("me")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, CordaX500Name> whoami() {
        return ImmutableMap.of("me", myLegalName);
    }

    /**
     * Returns all parties registered with the [NetworkMapService]. These names can be used to look up identities
     * using the [IdentityService].
     */
    @GET
    @Path("peers")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, List<CordaX500Name>> getPeers() {
        List<NodeInfo> nodeInfoSnapshot = rpcOps.networkMapSnapshot();
        return ImmutableMap.of("peers", nodeInfoSnapshot
                .stream()
                .map(node -> node.getLegalIdentities().get(0).getName())
                .filter(name -> !name.equals(myLegalName) && !serviceNames.contains(name.getOrganisation()))
                .collect(toList()));
    }

    /**
     * Displays all IOU states that exist in the node's vault.
     */
    @GET
    @Path("ious")
    @Produces(MediaType.APPLICATION_JSON)
    public List<StateAndRef<IOUState>> getIOUs() {
        return rpcOps.vaultQuery(IOUState.class).getStates();
    }

    /**
     //     * Returns vault state metadata
     //     */
    @GET
    @Path("states")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Vault.StateMetadata> getMetaData(){
        return rpcOps.vaultQuery(IOUState.class).getStatesMetadata();
        //return rpcOps.vaultQuery(query,true);
    }

    /**
     * Returns Vault Track
     *
     */

    @GET
    @Path("vaultTrack")
    @Produces(MediaType.APPLICATION_JSON)
    public List<StateAndRef<IOUState>> getTrack() {
        return rpcOps.vaultTrack(IOUState.class).getSnapshot().getStates();
    }


    /**
     * Initiates a flow to agree an IOU between two parties.
     *
     * Once the flow finishes it will have written the IOU to ledger. Both the lender and the borrower will be able to
     * see it when calling /api/example/ious on their respective nodes.
     *
     * This end-point takes a Party name parameter as part of the path. If the serving node can't find the other party
     * in its network map cache, it will return an HTTP bad request.
     *
     * The flow is invoked asynchronously. It returns a future when the flow's call() method returns.
     */

    /* ElectricPassport
        Changing the Query params to accomodate our needs

     */

    @PUT
    @Path("create-iou")
    public Response createIOU(

            @QueryParam("fisrt_name") String firstName,
            @QueryParam("last_name") String lastName,
            @QueryParam("dob") String dob,
            @QueryParam("customer") CordaX500Name customer)
            throws InterruptedException, ExecutionException {

//        if (iouValue <= 0) {
//            return Response.status(BAD_REQUEST).entity("Query parameter 'iouValue' must be non-negative.\n").build();
//        }
        if (customer == null) {
            return Response.status(BAD_REQUEST).entity("Query parameter 'partyName' missing or has wrong format.\n").build();
        }

        final Party otherParty = rpcOps.wellKnownPartyFromX500Name(customer);
        if (otherParty == null) {
            return Response.status(BAD_REQUEST).entity("Party named " + customer + "cannot be found.\n").build();
        }

        try {
            FlowProgressHandle<SignedTransaction> flowHandle = rpcOps
                    .startTrackedFlowDynamic(ExampleFlow.Initiator.class, firstName, lastName, dob, otherParty);
            flowHandle.getProgress().subscribe(evt -> System.out.printf(">> %s\n", evt));

            // The line below blocks and waits for the flow to return.
            final SignedTransaction result = flowHandle
                    .getReturnValue()
                    .get();

            final String msg = String.format("Transaction id %s committed to ledger.\n", result.getId());
            return Response.status(CREATED).entity(msg).build();

        } catch (Throwable ex) {
            final String msg = ex.getMessage();
            logger.error(ex.getMessage(), ex);
            return Response.status(BAD_REQUEST).entity(msg).build();
        }
    }

    @GET
    @Path("verifyPrevious")
    public Response verifyPrevious(

//            @QueryParam("fisrt_name") String firstName,
//            @QueryParam("last_name") String lastName,
//            @QueryParam("dob") String dob,
            @QueryParam("customer") CordaX500Name customer)
            throws InterruptedException, ExecutionException {

//        if (iouValue <= 0) {
//            return Response.status(BAD_REQUEST).entity("Query parameter 'iouValue' must be non-negative.\n").build();
//        }
        if (customer == null) {
            return Response.status(BAD_REQUEST).entity("Query parameter 'partyName' missing or has wrong format.\n").build();
        }

        final Party otherParty = rpcOps.wellKnownPartyFromX500Name(customer);
        if (otherParty == null) {
            return Response.status(BAD_REQUEST).entity("Party named " + customer + "cannot be found.\n").build();
        }

        try {
            FlowProgressHandle<List<StateAndRef<IOUState>>> flowHandle = rpcOps
                    .startTrackedFlowDynamic(VerifyPrevFlow.PrevStateFlow.class);//, otherParty);
            flowHandle.getProgress().subscribe(evt -> System.out.printf(">> %s\n", evt));

//            rpcOps.startFlowDynamic()

            // The line below blocks and waits for the flow to return.
            final StateAndRef<IOUState> result = flowHandle.getReturnValue().get().get(0);



//                    .getReturnValue()
//                    .get();
//
//            result.getInputs().get(0);
//
//
//            final  result = flowHandle
//                    .getReturnValue()
//                    .get();



//            final String msg = String.format("Transaction verified.\n", result.getState().getData().toString());
            final String msg = "Transaction verified.\n" + result.getState().getData().toString();
            return Response.status(CREATED).entity(msg).build();

        } catch (Throwable ex) {
            final String msg = ex.getMessage();
            logger.error(ex.getMessage(), ex);
            return Response.status(BAD_REQUEST).entity(msg).build();
        }
    }
}