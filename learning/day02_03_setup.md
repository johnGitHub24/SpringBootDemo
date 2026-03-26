# Day 2 & 3 - 開發環境安裝 (Windows & Mac)

由於您目前的系統環境已經能夠運行 Spring Boot 專案（從我們正在操作的 `SpringBootDemo` 即可得知），這代表您的環境已經具備以下核心組件：

## 1. Java Development Kit (JDK)
*   **要求**：Spring Boot 3.x 至少需要 **JDK 17**。
*   **確認方式**：在 Terminal 輸入 `java -version`。
*   **範例步驟 (Windows)**：
    1. 前往 [Oracle JDK](https://www.oracle.com/java/technologies/downloads/) 或 [Adoptium](https://adoptium.net/) 下載運作檔。
    2. 設定環境變數 `JAVA_HOME`。

## 2. Integrated Development Environment (IDE)
*   **推薦**：**IntelliJ IDEA** (目前您正在使用的 IDE)。
*   **外掛**：確認已安裝 `Spring Boot Helper` 或內建的 Spring 支援。

## 3. 建置工具 (Maven 或 Gradle)
*   **說明**：Spring Boot 專案通常使用 Maven (`pom.xml`) 或 Gradle (`build.gradle`)。
*   **範例**：本專案使用 **Gradle**。您可以查看根目錄的 `build.gradle`。

## 4. 資料庫 (MySQL/PostgreSQL/H2)
*   **說明**：入門時常用內嵌式資料庫 **H2**，不需安裝即可使用。
*   **範例配置** (application.properties):
    ```properties
    spring.datasource.url=jdbc:h2:mem:testdb
    spring.datasource.driverClassName=org.h2.Driver
    spring.datasource.username=sa
    spring.datasource.password=
    spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
    ```

---
[環境確認完畢後，請前往 Day 4 - 第一個 Spring Boot 程式]
