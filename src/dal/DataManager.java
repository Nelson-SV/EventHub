package dal;

import exceptions.EventException;

import java.sql.Connection;
import java.sql.SQLException;

public class DataManager implements IData{
    @Override
    public void test() throws EventException, SQLException {
        Connection conn = new ConnectionManager().getConnection();
        System.out.println(conn.getSchema());
    }
}
