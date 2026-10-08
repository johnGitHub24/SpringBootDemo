package com.demo.springbootdemo.advanced.rpc;

// import net.devh.boot.grpc.server.service.GrpcService;
import io.grpc.stub.StreamObserver;

// 假設 proto 定義了 PaymentService
// 此處為示意實作，若無 proto 生成類別則會報編譯錯誤，但在教學案例中作為結構演示
// @GrpcService
/**
 * 【職責】展示 gRPC 支付服務端結構位置（示意骨架）。
 * <p>【技巧】預留 Protocol Buffers 產生類別與 {@link StreamObserver} 回呼寫法；方法本體刻意註解。
 * <p>【概念】gRPC 契約由 proto 產生，手寫適配層只做轉接；缺少 generated 碼時不應強行補註解於產生檔。
 * <p>【邊界】不負責真實支付業務或 proto 產生。
 */
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
