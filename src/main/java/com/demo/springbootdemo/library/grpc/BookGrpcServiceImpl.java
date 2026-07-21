package com.demo.springbootdemo.library.grpc;

import com.demo.springbootdemo.grpc.BookRequest;
import com.demo.springbootdemo.grpc.BookResponse;
import com.demo.springbootdemo.grpc.BookServiceGrpc;
import com.demo.springbootdemo.library.service.BookService;
import com.demo.springbootdemo.library.dto.BookDto;
import io.grpc.stub.StreamObserver;
// import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 【職責】將 Protobuf 圖書請求轉交 {@link BookService}，並以 gRPC 串流回傳。
 * 【技巧】繼承產生的 {@link BookServiceGrpc.BookServiceImplBase}，以 {@link StreamObserver} 寫回回應。
 * 【概念】gRPC 與 REST 可共用同一 Service；協定差異留在適配層，商業規則不必複製。
 * 【邊界】目前 {@code @GrpcService} 為註解狀態，預設未掛載；不取代 Service 內規則。
 */
// @GrpcService
public class BookGrpcServiceImpl extends BookServiceGrpc.BookServiceImplBase {

    private static final Logger log = LoggerFactory.getLogger(BookGrpcServiceImpl.class);

    @Autowired
    private BookService bookService;

    /**
     * 依請求中的圖書 ID 查詢並透過 {@link StreamObserver} 回傳 Protobuf 回應。
     * 查詢失敗時以 gRPC {@code NOT_FOUND} 結束串流，不向上拋出未處理例外。
     *
     * @param request          含圖書 ID 的 Protobuf 請求
     * @param responseObserver 單次回應與完成／錯誤回呼
     */
    @Override
    public void getBook(BookRequest request, StreamObserver<BookResponse> responseObserver) {
        log.info("【gRPC】 收到書籍查詢請求 ID: {}", request.getId());
        try {
            // 呼叫 Spring Service
            BookDto bookDto = bookService.getBookById(request.getId());

            // 利用 Builder 模式建構二進制響應訊息
            BookResponse response = BookResponse.newBuilder()
                    .setId(bookDto.getId())
                    .setTitle(bookDto.getTitle())
                    .setAuthor(bookDto.getAuthor())
                    .setCategory(bookDto.getCategory())
                    .setIsBorrowed(bookDto.getIsBorrowed())
                    .build();

            // 發送回傳並結算
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            log.error("【gRPC】 查詢失敗: {}", e.getMessage());
            // 回傳 gRPC 錯誤狀態碼
            responseObserver.onError(io.grpc.Status.NOT_FOUND
                    .withDescription("查無此書籍: " + e.getMessage())
                    .asRuntimeException());
        }
    }
}
