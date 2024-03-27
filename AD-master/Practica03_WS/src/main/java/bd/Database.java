package bd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Function;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import ad.practica03.Utils;



public class Database {

    /**
     * Executes SQL operations on the db ensuring a correct usage of the
     * connection lifetime and exception handling
     *
     * @param operation executed function
     * @param input operation arguments
     * @return operation return or null
     */
    protected Map<String, Object> execute(Function<Map<String, Object>, Map<String, Object>> operation, Map<String, Object> input) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:derby://localhost:1527/pr2;user=pr2;password=pr2");
            input = (input == null) ? new HashMap<>() : input;
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

    protected List<Map<String, Object>> dump_results(ResultSet rs) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        int column_count = rsmd.getColumnCount();
        String[] column_names = new String[column_count];
        for (int i = 0; i < column_count; ++i) {
            column_names[i] = rsmd.getColumnName(i + 1);
        }
        List<Map<String, Object>> data = new ArrayList<>();
        while (rs.next()) {
            Map<String, Object> row = new HashMap<>();
            for (String col : column_names) {
                row.put(col, rs.getObject(col));
            }
            data.add(row);
        }
        return data;
    }

    protected Map<String, Object> sql_create_account(Map<String, Object> input) {
        Map<String, Object> output = new HashMap<>();

        Connection c = (Connection) input.get("connection");
        String user = (String) input.get("user");
        String pass = (String) input.get("pass");
        Boolean success = false;
        try {
            String query = "INSERT INTO usuarios VALUES (?, ?)";
            PreparedStatement statement = c.prepareStatement(query);
            statement.setString(1, user);
            statement.setString(2, pass);
            statement.executeUpdate(); // if user exists, throws
            success = true;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        output.put("result", success);
        return output;
    }

    public boolean create_account(String user, String pass) {
        Map<String, Object> args = new HashMap<>();
        args.put("user", user);
        args.put("pass", pass);
        Map<String, Object> out = execute(this::sql_create_account, args);
        Boolean result = (Boolean) out.get("result");
        return result;
    }

    protected Map<String, Object> sql_is_correct_account(Map<String, Object> input) {
        Map<String, Object> output = new HashMap<>();

        Connection c = (Connection) input.get("connection");
        String user = (String) input.get("user");
        String pass = (String) input.get("pass");
        Boolean success = false;

        try {
            String query = "SELECT * FROM usuarios WHERE id_usuario = ? AND password = ?";
            PreparedStatement statement = c.prepareStatement(query);
            statement.setString(1, user);
            statement.setString(2, pass);
            ResultSet rs = statement.executeQuery();
            success = rs.next(); // its a primary key so either empty or 1 result    
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        output.put("result", success);
        return output;
    }

    public boolean is_correct_account(String user, String pass) {
        Map<String, Object> args = new HashMap<>();
        args.put("user", user);
        args.put("pass", pass);
        Map<String, Object> out = execute(this::sql_is_correct_account, args);
        Boolean result = (Boolean) out.get("result");
        return result;
    }

    protected Map<String, Object> sql_insert_post(Map<String, Object> input) {
        Map<String, Object> output = new HashMap<>();
        Connection c = (Connection) input.get("connection");
        String title = (String) input.get("title");
        String desc = (String) input.get("desc");
        String kw = (String) input.get("kw");
        String author = (String) input.get("author");
        String user = (String) input.get("user");
        String cdate = (String) input.get("cdate");
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String fn = (String) input.get("fn");

        Boolean success = false;
        try {
            // no user check required. Transaction would abort if incorrect creator,
            // since its a foreign key.
            String query = "INSERT INTO IMAGE (TITLE, DESCRIPTION, KEYWORDS, AUTHOR, CREATOR, CAPTURE_DATE, STORAGE_DATE, FILENAME) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = c.prepareStatement(query);
            statement.setString(1, title);
            statement.setString(2, desc);
            statement.setString(3, kw);
            statement.setString(4, author);
            statement.setString(5, user);
            statement.setString(6, cdate);
            statement.setString(7, date);
            statement.setString(8, fn);
            statement.executeUpdate();
            success = true;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        output.put("result", success);
        return output;
    }

    public boolean insert_post(String title, String description, String keywords,
            String author, String user, String cdate, String filename) {
        Map<String, Object> args = new HashMap<>();
        args.put("title", title);
        args.put("desc", description);
        keywords = Utils.keyword_process(keywords);
        args.put("kw", keywords);
        args.put("author", author);
        args.put("user", user);
        args.put("cdate", cdate);
        args.put("fn", filename);
        Map<String, Object> out = execute(this::sql_insert_post, args);
        Boolean result = (Boolean) out.get("result");
        return result;
    }

    protected Map<String, Object> sql_dump_all_posts(Map<String, Object> input) {
        Connection c = (Connection) input.get("connection");
        Map<String, Object> output = new HashMap<>();
        output.put("result", null);
        try {
            String query = "SELECT * FROM image";
            PreparedStatement statement = c.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            output.put("result", dump_results(rs));
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return output;
    }

    public List<Map<String, Object>> dump_all_posts() {
        Map<String, Object> out = execute(this::sql_dump_all_posts, null);
        List<Map<String, Object>> data = (List<Map<String, Object>>) out.get("result"); // works if null
        return data;
    }
    
    protected Map<String, Object> sql_search_posts(Map<String, Object> input) {
        Connection c = (Connection) input.get("connection");
        String title_substr = (String) input.get("title");
        String with_kws = (String) input.get("kw");
        Map<String, Object> output = new HashMap<>();
        output.put("result", null);
        try {
            // this works since keywords are stored sorted and correctly preprocessed
            String query = "SELECT * FROM image WHERE title LIKE ? AND keywords LIKE ?";
            PreparedStatement statement = c.prepareStatement(query);
            String title_expr = "%"+title_substr+"%";
            statement.setString(1, title_expr);
            String kws_expr = "%"+ with_kws.replace(",","%") + "%";
            statement.setString(2, kws_expr);
            ResultSet rs = statement.executeQuery();
            output.put("result", dump_results(rs));
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return output;
    }
    
    public List<Map<String, Object>> search_posts(String title_substring, String with_keywords) {
        Map<String, Object> args = new HashMap<>();
        args.put("title", title_substring);
        with_keywords = Utils.keyword_process(with_keywords);
        args.put("kw", with_keywords);
        Map<String, Object> out = execute(this::sql_search_posts, args);
        List<Map<String, Object>> data = (List<Map<String, Object>>) out.get("result"); // works if null
        return data;
    }
    
    protected Map<String, Object> sql_search_by_id(Map<String, Object> input) {
        Connection c = (Connection) input.get("connection");
        int id = (int) input.get("id");
        Map<String, Object> output = new HashMap<>();
        output.put("result", null);
        try {
            // this works since keywords are stored sorted and correctly preprocessed
            String query = "SELECT * FROM image WHERE id = ?";
            PreparedStatement statement = c.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            output.put("result", dump_results(rs));
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return output;
    }
    
    public List<Map<String, Object>> search_by_id(int id) {
        Map<String, Object> args = new HashMap<>();
        args.put("id", id);
        Map<String, Object> out = execute(this::sql_search_by_id, args);
        List<Map<String, Object>> data = (List<Map<String, Object>>) out.get("result"); // works if null
        return data;
    }
    
    protected Map<String, Object> sql_search_by_kw(Map<String, Object> input) {
        Connection c = (Connection) input.get("connection");
        String with_kws = (String) input.get("kw");
        Map<String, Object> output = new HashMap<>();
        output.put("result", null);
        try {
            // this works since keywords are stored sorted and correctly preprocessed
            String query = "SELECT * FROM image WHERE keywords LIKE ?";
            PreparedStatement statement = c.prepareStatement(query);
            String kws_expr = "%"+ with_kws.replace(",","%") + "%";
            statement.setString(1, kws_expr);
            ResultSet rs = statement.executeQuery();
            output.put("result", dump_results(rs));
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return output;
    }
    
    public List<Map<String, Object>> search_by_kw(String with_keywords) {
        Map<String, Object> args = new HashMap<>();
        with_keywords = Utils.keyword_process(with_keywords);
        args.put("kw", with_keywords);
        Map<String, Object> out = execute(this::sql_search_by_kw, args);
        List<Map<String, Object>> data = (List<Map<String, Object>>) out.get("result"); // works if null
        return data;
    }
    
    protected Map<String, Object> sql_search_by_storedate(Map<String, Object> input) {
        Connection c = (Connection) input.get("connection");
        String day = (String) input.get("day");
        String month = (String) input.get("month");
        String year = (String) input.get("year");
        Map<String, Object> output = new HashMap<>();
        output.put("result", null);
        try {
            // this works since keywords are stored sorted and correctly preprocessed
            String query = "SELECT * FROM image WHERE storage_date LIKE ?";
            PreparedStatement statement = c.prepareStatement(query);
            day = day != null ? "%"+day : "%";
            String date = "%" + year + "-%" + month + "-" + day;
            statement.setString(1, date);
            ResultSet rs = statement.executeQuery();
            output.put("result", dump_results(rs));
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return output;
    }
    
    public List<Map<String, Object>> search_by_storedate(String date) {
        Map<String, Object> args = new HashMap<>();
        String[] d = date.split("/");
        args.put("day", d.length == 3 ? d[0] : null);
        args.put("month", d.length == 3 ? d[1] : d[0]);
        args.put("year", d.length == 3 ? d[2] : d[1]);
        Map<String, Object> out = execute(this::sql_search_by_storedate, args);
        List<Map<String, Object>> data = (List<Map<String, Object>>) out.get("result"); // works if null
        return data;
    }
    
     protected Map<String, Object> sql_search_by_title(Map<String, Object> input) {
        Connection c = (Connection) input.get("connection");
        String title_substr = (String) input.get("title");
        Map<String, Object> output = new HashMap<>();
        output.put("result", null);
        try {
            // this works since keywords are stored sorted and correctly preprocessed
            String query = "SELECT * FROM image WHERE title LIKE ?";
            PreparedStatement statement = c.prepareStatement(query);
            String title_expr = "%"+title_substr+"%";
            statement.setString(1, title_expr);
            ResultSet rs = statement.executeQuery();
            output.put("result", dump_results(rs));
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return output;
    }
    
    public List<Map<String, Object>> search_by_title(String title_substring) {
        Map<String, Object> args = new HashMap<>();
        args.put("title", title_substring);
        Map<String, Object> out = execute(this::sql_search_by_title, args);
        List<Map<String, Object>> data = (List<Map<String, Object>>) out.get("result"); // works if null
        return data;
    }
    
    protected Map<String, Object> sql_search_by_author(Map<String, Object> input) {
        Connection c = (Connection) input.get("connection");
        String author_substr = (String) input.get("author");
        Map<String, Object> output = new HashMap<>();
        output.put("result", null);
        try {
            // this works since keywords are stored sorted and correctly preprocessed
            String query = "SELECT * FROM image WHERE author LIKE ?";
            PreparedStatement statement = c.prepareStatement(query);
            String author_expr = "%"+author_substr+"%";
            statement.setString(1, author_expr);
            ResultSet rs = statement.executeQuery();
            output.put("result", dump_results(rs));
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return output;
    }
    
    public List<Map<String, Object>> search_by_author(String author_substring) {
        Map<String, Object> args = new HashMap<>();
        args.put("author", author_substring);
        Map<String, Object> out = execute(this::sql_search_by_author, args);
        List<Map<String, Object>> data = (List<Map<String, Object>>) out.get("result"); // works if null
        return data;
    }
    
    protected Map<String, Object> sql_delete_post(Map<String, Object> input) {
        Connection c = (Connection) input.get("connection");
        int id = (int) input.get("id");
        Map<String, Object> output = new HashMap<>();
        output.put("result", null);
        try {
            String query = "SELECT * FROM image WHERE id = ?";
            PreparedStatement statement = c.prepareStatement(query,
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                output.put("result", rs.getString("filename"));
                rs.deleteRow();
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return output;
    }

    public String delete_post(int id) {
    Map<String, Object> args = new HashMap<>();
        args.put("id", id);
        Map<String, Object> out = execute(this::sql_delete_post, args);
        return (String) out.get("result");
    }

    protected Map<String, Object> sql_update_post(Map<String, Object> input) {
        Map<String, Object> output = new HashMap<>();
        Connection c = (Connection) input.get("connection");
        int id = (int) input.get("id");
        String title = (String) input.get("title");
        String desc = (String) input.get("desc");
        String kw = (String) input.get("kw");
        String author = (String) input.get("author");
        String cdate = (String) input.get("cdate");
        String fn = (String) input.get("fn");

        output.put("result", false);
        try {
            String query = "UPDATE IMAGE SET title = COALESCE(?, title), "
                    + "description = COALESCE(?, description), "
                    + "keywords = COALESCE(?, keywords), "
                    + "author = COALESCE(?, author), "
                    + "capture_date = COALESCE(?, capture_date), "
                    + "filename = COALESCE(?, filename) "
                    + "WHERE id = ?";
            PreparedStatement statement = c.prepareStatement(query);
            statement.setString(1, title);
            statement.setString(2, desc);
            statement.setString(3, kw);
            statement.setString(4, author);
            statement.setString(5, cdate);
            statement.setString(6, fn);
            statement.setInt(7, id);
            int rows_affected = statement.executeUpdate();
            output.put("result", rows_affected > 0);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return output;
    }

    public boolean update_post(int id, String title, String description,
        String keywords, String author, String cdate, String filename) {
        Map<String, Object> args = new HashMap<>();
        args.put("id", id);
        args.put("title", title);
        args.put("desc", description);
        keywords = Utils.keyword_process(keywords);
        args.put("kw", keywords);
        args.put("author", author);
        args.put("cdate", cdate);
        args.put("fn", filename);
        Map<String, Object> out = execute(this::sql_update_post, args);
        Boolean result = (Boolean) out.get("result");
        return result;
    }
    
}
