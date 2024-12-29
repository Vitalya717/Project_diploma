package netology.web.data;

import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLHelper {
    private static final QueryRunner QUERY_RUNNER = new QueryRunner();

    private SQLHelper() {
    }

    private static Connection getConn() throws SQLException { // метод для установления соединения с базой данных
        return DriverManager.getConnection(System.getProperty("db.url"), "app", "pass");
    }

    @SneakyThrows
    public static void cleanDatabase() { // очистить базу дагнных
        var conn = getConn();
        QUERY_RUNNER.execute(conn, "DELETE FROM credit_request_entity");
        QUERY_RUNNER.execute(conn, "DELETE FROM order_entity");
        QUERY_RUNNER.execute(conn, "DELETE FROM payment_entity");
    }

    @SneakyThrows
    public static String getPurchaseStatus() { // получить статус покупки
        var codeSQL = "SELECT status FROM payment_entity";
        var conn = getConn();
        var code = QUERY_RUNNER.query(conn, codeSQL, new ScalarHandler<String>());
        return code;
    }

    @SneakyThrows
    public static String getPurchaseCreditStatus() { // получить стату покупки в кредит
        var codeSQL = "SELECT status FROM credit_request_entity";
        var conn = getConn();
        var code = QUERY_RUNNER.query(conn, codeSQL, new ScalarHandler<String>());
        return code;
    }

    @SneakyThrows
    public static Long getNumberPurchasesCredit() { // получить количество покупок в кредит
        var codeSQL = "SELECT COUNT(*) FROM credit_request_entity";
        var conn = getConn();
        var code = QUERY_RUNNER.query(conn, codeSQL, new ScalarHandler<Long>());
        return code;
    }
    @SneakyThrows
    public static Long getNumberPurchases() { // получить количество покупок
        var codeSQL = "SELECT COUNT(*) FROM payment_entity";
        var conn = getConn();
        var code = QUERY_RUNNER.query(conn, codeSQL, new ScalarHandler<Long>());
        return code;
    }

    @SneakyThrows
    public static Integer getPurchaseAmount() { // получить сумму покупки
        var codeSQL = "SELECT amount FROM payment_entity";
        var conn = getConn();
        var code = QUERY_RUNNER.query(conn, codeSQL, new ScalarHandler<Integer>());
        return code;
    }

}

