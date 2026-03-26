package com.demo.springbootdemo.advanced.rpc;

import net.devh.boot.grpc.server.service.GrpcService;
import io.grpc.stub.StreamObserver;

// 假設 proto 定義了 PaymentService
// 此處為示意實作，若無 proto 生成類別則會報編譯錯誤，但在教學案例中作為結構演示
@GrpcService
public class PaymentGrpcService {
    
    /* 
    // 範例方法：
    @Override
    public void processPayment(PaymentRequest request, StreamObserver<PaymentResponse> responseObserver) {
        // 處理邏輯
        PaymentResponse response = PaymentResponse.newBuilder()
            .setSuccess(true)
            .setMessage("Payment processed via gRPC")
            .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
    */
}
