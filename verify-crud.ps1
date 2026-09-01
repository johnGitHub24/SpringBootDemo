# CRUD 功能功能指標測試腳本 (REST API)

$baseUrl = "http://localhost:8080/api/orders"

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "開始 CRUD REST API 驗證" -ForegroundColor Cyan
Write-Host "========================================`n" -ForegroundColor Cyan

try {
    # 1. CREATE - 建立訂單
    Write-Host "1. [CREATE] 正在建立測試訂單..." -ForegroundColor Yellow
    $orderData = @{
        customerName = "驗證測試員"
        productName = "測試商品"
        price = 99.9
        quantity = 2
    } | ConvertTo-Json

    $newOrder = Invoke-RestMethod -Uri $baseUrl -Method Post -Body $orderData -ContentType "application/json"
    $orderId = $newOrder.id
    Write-Host "   ✅ 成功建立訂單! ID: $orderId" -ForegroundColor Green

    # 2. READ - 取得訂單
    Write-Host "`n2. [READ] 驗證訂單是否存在..." -ForegroundColor Yellow
    $order = Invoke-RestMethod -Uri "$baseUrl/$orderId" -Method Get
    Write-Host "   ✅ 取得成功! 客戶: $($order.customerName), 狀態: $($order.status)" -ForegroundColor Green

    # 3. UPDATE - 更新狀態
    Write-Host "`n3. [UPDATE] 將訂單狀態更新為 COMPLETED..." -ForegroundColor Yellow
    $updateData = @{
        status = "COMPLETED"
    } | ConvertTo-Json
    $updatedOrder = Invoke-RestMethod -Uri "$baseUrl/$orderId" -Method Put -Body $updateData -ContentType "application/json"
    Write-Host "   ✅ 更新成功! 目前狀態: $($updatedOrder.status)" -ForegroundColor Green

    # 4. DELETE - 刪除訂單
    Write-Host "`n4. [DELETE] 刪除該測試訂單..." -ForegroundColor Yellow
    $deleteResult = Invoke-WebRequest -Uri "$baseUrl/$orderId" -Method Delete
    if ($deleteResult.StatusCode -eq 200) {
        Write-Host "   ✅ 刪除成功!" -ForegroundColor Green
    }

    # 5. FINAL READ - 確認已刪除
    Write-Host "`n5. [VERIFY] 確認訂單已不再存在..." -ForegroundColor Yellow
    try {
        Invoke-RestMethod -Uri "$baseUrl/$orderId" -Method Get -ErrorAction Stop
        Write-Host "   ❌ 錯誤: 訂單依然存在!" -ForegroundColor Red
    } catch {
        Write-Host "   ✅ 驗證成功: 訂單已消失 (404 Not Found)" -ForegroundColor Green
    }

} catch {
    Write-Host "`n❌ 驗證過程中出錯: $($_.Exception.Message)" -ForegroundColor Red
    Write-Host "請確認 Spring Boot 應用程式是否已啟動並運行於 localhost:8080" -ForegroundColor Cyan
}

Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "驗證完成" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
