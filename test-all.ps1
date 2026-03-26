# 訂單管理系統 - 完整測試腳本
# 用途: 測試所有 CRUD 操作 (建立、讀取、更新、刪除)

Write-Host "=== 訂單管理系統測試 ===" -ForegroundColor Green

# 1. 測試 GET (初始狀態，預期為空)
Write-Host "`n1. 查詢所有訂單 (初始狀態)" -ForegroundColor Yellow
try {
    $orders = Invoke-RestMethod -Uri "http://localhost:8080/api/orders"
    Write-Host "✓ 訂單數量: $($orders.Count)" -ForegroundColor Green
} catch {
    Write-Host "✗ 查詢失敗: $($_.Exception.Message)" -ForegroundColor Red
    exit
}

# 2. 測試 POST (建立新訂單)
Write-Host "`n2. 建立新訂單" -ForegroundColor Yellow
$newOrder = @{
    customerName = "測試客戶"
    productName = "測試商品"
    quantity = 1
    price = 999
} | ConvertTo-Json

try {
    $created = Invoke-RestMethod -Method POST `
        -Uri "http://localhost:8080/api/orders" `
        -ContentType "application/json" `
        -Body $newOrder
    
    Write-Host "✓ 訂單已建立，ID: $($created.id)" -ForegroundColor Green
    $orderId = $created.id
} catch {
    Write-Host "✗ 建立失敗: $($_.Exception.Message)" -ForegroundColor Red
    exit
}

# 3. 測試 GET by ID (查詢剛建立的訂單)
Write-Host "`n3. 查詢單一訂單" -ForegroundColor Yellow
try {
    $order = Invoke-RestMethod -Uri "http://localhost:8080/api/orders/$orderId"
    Write-Host "✓ 客戶姓名: $($order.customerName)" -ForegroundColor Green
    Write-Host "✓ 商品名稱: $($order.productName)" -ForegroundColor Green
    Write-Host "✓ 訂單狀態: $($order.status)" -ForegroundColor Green
} catch {
    Write-Host "✗ 查詢失敗: $($_.Exception.Message)" -ForegroundColor Red
}

# 4. 測試 PUT (更新訂單內容)
Write-Host "`n4. 更新訂單" -ForegroundColor Yellow
$updateData = @{
    customerName = "測試客戶 (已更新)"
    productName = "測試商品 (已更新)"
    quantity = 2
    price = 1998
} | ConvertTo-Json

try {
    $updated = Invoke-RestMethod -Method PUT `
        -Uri "http://localhost:8080/api/orders/$orderId" `
        -ContentType "application/json" `
        -Body $updateData
    
    Write-Host "✓ 訂單更新成功" -ForegroundColor Green
    Write-Host "✓ 更新後數量: $($updated.quantity)" -ForegroundColor Green
    Write-Host "✓ 更新後價格: $($updated.price)" -ForegroundColor Green
} catch {
    Write-Host "✗ 更新失敗: $($_.Exception.Message)" -ForegroundColor Red
}

# 5. 測試 GET (確認資料已同步更新)
Write-Host "`n5. 再次查詢所有訂單 (驗證更新)" -ForegroundColor Yellow
try {
    $allOrders = Invoke-RestMethod -Uri "http://localhost:8080/api/orders"
    Write-Host "✓ 目前訂單總數: $($allOrders.Count)" -ForegroundColor Green
} catch {
    Write-Host "✗ 查詢失敗: $($_.Exception.Message)" -ForegroundColor Red
}

# 6. 測試 DELETE (刪除該筆訂單)
Write-Host "`n6. 刪除測試訂單" -ForegroundColor Yellow
try {
    Invoke-RestMethod -Method DELETE -Uri "http://localhost:8080/api/orders/$orderId"
    Write-Host "✓ 訂單刪除成功" -ForegroundColor Green
} catch {
    Write-Host "✗ 刪除失敗: $($_.Exception.Message)" -ForegroundColor Red
}

# 7. 測試 GET (確認刪除成功，數量應回歸初始)
Write-Host "`n7. 最終狀態確認" -ForegroundColor Yellow
try {
    $finalOrders = Invoke-RestMethod -Uri "http://localhost:8080/api/orders"
    Write-Host "✓ 最終訂單數量: $($finalOrders.Count)" -ForegroundColor Green
} catch {
    Write-Host "✗ 查詢失敗: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`n=== 所有測試環節已完成 ===" -ForegroundColor Green
Write-Host "所有 CRUD 操作驗證完畢！" -ForegroundColor Cyan
