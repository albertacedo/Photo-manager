/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.function.Function;
import java.util.Map;
import java.util.HashMap;
import bd.account;

/**
 *
 * @author alumne
 */
public class Database {
    private Map<String, Object> execute(Function<Map<String, Object>, Map<String, Object>> operation, Map<String, Object> input) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:derby://localhost:1527/pr2;user=pr2;password=pr2");
            input.put("connection", connection);
            return operation.apply(input);
        } catch (SQLException e) {
            // connection failed OR SQL failed 
            System.err.println(e.getMessage());
            return null;
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }
    }
    
    private Map<String, Object> sql_create_user(Map<String, Object> input) {
        Map<String, Object> output = new HashMap<>();
        output.put("test", "nice");
        return output;
    }
    
    public Map<String, Object> create_user(int a, double b) {
        Map<String, Object> args = new HashMap<>();
        args.put("usuari", a);
        args.put("pass", b);
        return execute(this::sql_create_user, args);
    }
    /*private void build_statements() throws SQLException {
        String query = "insert into usuarios values(?,?)";
        account_creation = connection.prepareStatement(query);
    }
    
    public boolean creasdfgsdfte_account(account data) {
        try {
            account_creation.setString(1, data.username);
            account_creation.setString(2, data.password);
            account_creation.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
    }*/
    
    
}

