package com.demo.springbootdemo.library.exception;

/**
 * 【職責】表示目標圖書不存在，或借還流程拒絕操作。
 * 【技巧】以非受檢例外承載訊息，由 {@link GlobalExceptionHandler} 對應為 HTTP 404。
 * 【概念】專用領域例外讓服務可用「失敗即拋」表達契約，不必到處回 Optional 到 Controller。
 * 【邊界】不承載其他錯誤類型，也不組裝 HTTP 回應。
 */
public class BookNotFoundException extends RuntimeException {

    /**
     * 【職責】以自訂訊息建立例外（例如已借出說明）。
     * 【技巧】直接委派 {@link RuntimeException} 建構子。
     * 【概念】同一例外型別可表達多種「資源不可用」語意，由訊息區分細節。
     */
    public BookNotFoundException(String message) {
        super(message);
    }

    /**
     * 【職責】依主鍵組出「找不到書籍」訊息。
     * 【技巧】在訊息中嵌入 id，方便除錯與日誌對照。
     * 【概念】工廠式建構子讓呼叫端不必重複拼字串。
     */
    public BookNotFoundException(Integer id) {
        super("找不到書籍，ID: " + id);
    }
}
