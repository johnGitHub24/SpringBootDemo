package com.demo.springbootdemo.library.grpc;

import com.demo.springbootdemo.grpc.BookRequest;
import com.demo.springbootdemo.grpc.BookResponse;
import com.demo.springbootdemo.grpc.BookServiceGrpc;
import com.demo.springbootdemo.library.service.BookService;
import com.demo.springbootdemo.library.dto.BookDto;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * gRPC 伺服器端實作案例 (Remote Procedure Call)
 *
 * 技術原理：
 * 1. 定義：在 .proto 檔案中定義 Service 與 Message。
 * 2. 生成：編譯時自動生成 BookServiceGrpc.BookServiceImplBase。
 * 3. 實作：繼承生成類別並覆寫方法。
 *
 * 優點：
 * - 跨語言支援。
 * - 使用 HTTP/2 雙向串流，性能優於傳統 REST。
 * - Protobuf 二進制序列化，體積小、速度快。
 */
@GrpcService
public class BookGrpcServiceImpl extends BookServiceGrpc.BookServiceImplBase {

    private static final Logger log = LoggerFactory.getLogger(BookGrpcServiceImpl.class);

    @Autowired
    private BookService bookService;

    /**
     * 技術方法：getBook
     * 接收 Protobuf 請求並透過 StreamObserver 回傳
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
