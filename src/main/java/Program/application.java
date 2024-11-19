package Program;

import db.DB;

import java.sql.Connection;

public class application {
    public static void main(String[] args) {
        Connection connection = DB.getConnection();
        DB.closeConnection();

        System.out.println(connection);
    }
}
