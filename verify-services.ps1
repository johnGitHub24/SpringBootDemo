#!/usr/bin/env pwsh
# 服務驗證腳本

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "服務狀態驗證" -ForegroundColor Cyan
Write-Host "========================================`n" -ForegroundColor Cyan

# 1. 檢查 Docker 容器狀態
Write-Host "1. Docker 容器狀態:" -ForegroundColor Yellow
docker ps --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}"
Write-Host ""

# 2. 測試 Redis
Write-Host "2. Redis 連接測試:" -ForegroundColor Yellow
docker exec redis-demo redis-cli ping
Write-Host ""

# 3. 測試 Kafka
Write-Host "3. Kafka Topics 列表:" -ForegroundColor Yellow
docker exec kafka-demo kafka-topics --list --bootstrap-server localhost:9092
Write-Host ""

# 4. 檢查 Kafka 日誌（最後 10 行）
Write-Host "4. Kafka 日誌（最後 10 行）:" -ForegroundColor Yellow
docker logs kafka-demo --tail 10
Write-Host ""

# 5. Spring Boot 應用程式測試
Write-Host "5. Spring Boot 應用程式測試:" -ForegroundColor Yellow
Write-Host "   嘗試連接 http://localhost:8080..." -ForegroundColor Gray
try {
    $response = Invoke-WebRequest -Uri "http://localhost:8080/api/orders" -Method GET -TimeoutSec 5 -ErrorAction Stop
    Write-Host "   ✅ 應用程式正在運行 (狀態碼: $($response.StatusCode))" -ForegroundColor Green
} catch {
    Write-Host "   ❌ 無法連接到應用程式: $($_.Exception.Message)" -ForegroundColor Red
    Write-Host "   請確認 Spring Boot 應用程式是否已啟動" -ForegroundColor Yellow
}
Write-Host ""

# 6. 顯示所有服務網址
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "服務訪問網址" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Spring Boot API:    http://localhost:8080/api/orders" -ForegroundColor White
Write-Host "H2 Console:         http://localhost:8080/h2-console" -ForegroundColor White
Write-Host "Redis:              localhost:6379" -ForegroundColor White
Write-Host "Kafka:              localhost:29092" -ForegroundColor White
Write-Host "Zookeeper:          localhost:22181" -ForegroundColor White
Write-Host "========================================`n" -ForegroundColor Cyan
