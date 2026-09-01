# API 測試腳本
$baseUrl = "http://localhost:8080/api/orders"

Write-Host "=== 測試 GET 所有訂單 ===" -ForegroundColor Green
try {
    $response = Invoke-RestMethod -Method GET -Uri $baseUrl
    Write-Host "成功! 訂單數量: $($response.Count)" -ForegroundColor Green
    $response | ConvertTo-Json
} catch {
    Write-Host "失敗: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`n=== 測試 POST 建立訂單 ===" -ForegroundColor Green
$orderData = @{
    customerName = "測試客戶"
    productName = "iPhone 15"
    quantity = 1
    price = 29900
} | ConvertTo-Json

try {
    $response = Invoke-RestMethod -Method POST -Uri $baseUrl -ContentType "application/json" -Body $orderData
    Write-Host "成功! 訂單 ID: $($response.id)" -ForegroundColor Green
    $response | ConvertTo-Json
} catch {
    Write-Host "失敗: $($_.Exception.Message)" -ForegroundColor Red
    Write-Host "詳細錯誤:" -ForegroundColor Yellow
    $_.Exception | Format-List -Force
}
