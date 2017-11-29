//package com.example.oracles;
//
//import net.corda.core.messaging.CordaRPCOps;
//
//import javax.ws.rs.GET;
//import javax.ws.rs.Path;
//import javax.ws.rs.Produces;
//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.Response;
//
//public class Oracle {
//
//    private final CordaRPCOps rpc;
//
//    public Oracle(CordaRPCOps rpc){
//        this.rpc = rpc;
//    }
//
//    @GET
//    @Path("oracle-service")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response getOraclePrime(){
//        try{
//            rpc.startFlowDynamic();
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//}
