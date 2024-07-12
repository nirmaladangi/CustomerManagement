package swing;

import java.sql.ResultSet;

import javax.swing.table.TableModel;
import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

		@SuppressWarnings("unused")
		public class DbUtils {

		    public static DefaultTableModel resultSetToTableModel(ResultSet rs) throws SQLException {
		        ResultSetMetaData metaData = rs.getMetaData();
		        int columnCount = metaData.getColumnCount();

		        // Create column names array
		        String[] columnNames = new String[columnCount];
		        for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
		            columnNames[columnIndex - 1] = metaData.getColumnLabel(columnIndex);
		        }

		        // Create data array
		        Object[][] data = new Object[0][columnCount];
		        while (rs.next()) {
		            Object[] newRow = new Object[columnCount];
		            for (int i = 1; i <= columnCount; i++) {
		                newRow[i - 1] = rs.getObject(i);
		            }
		            Object[][] temp = new Object[data.length + 1][columnCount];
		            System.arraycopy(data, 0, temp, 0, data.length);
		            temp[data.length] = newRow;
		            data = temp;
		        }

		        return new DefaultTableModel(data, columnNames);

		    }

}


