package info.yangguo.perseus.test.util;

import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author:杨果
 * @date:2017/6/22 下午11:00
 *
 * Description:
 *
 */
public class SqliteUtil {
    private static Logger logger = LoggerFactory.getLogger(SqliteUtil.class);

    public static String createNewDatabase(String fileName) {
        String url = "jdbc:sqlite:" + SqliteUtil.class.getClassLoader().getResource("").getPath() + fileName;

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                logger.info("database:{} has been created.", fileName);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            Assert.fail();
        }
        return url;
    }

    public static void executeSql(String url, String sql) {
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            logger.info("sql:{} has executed", sql);
        } catch (SQLException e) {
            logger.error(e.getMessage());
            Assert.fail();
        }
    }

    public static void dropDatabase(String fileName) {
        String filePath = SqliteUtil.class.getClassLoader().getResource("").getPath() + fileName;
        File dbFile = new File(filePath);
        if (dbFile.isFile() && dbFile.exists()) {
            dbFile.delete();
            logger.info("database file:{} has deleted", filePath);
        }
    }
}
