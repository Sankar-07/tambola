import java.util.*;
import java.sql.*;

public class Tambola_Generate {

    static class Node {
        int A[][];

        public Node() {
            this.A = new int[3][9];
        }

        // ... (existing Node class methods)

        public int getRowCount(int r) {
            int count = 0;
            for (int i = 0; i < 9; i++) {
                if (A[r][i] != 0)
                    count++;
            }
            return count;
        }

        public int getColCount(int c) {
            int count = 0;
            for (int i = 0; i < 3; i++) {
                if (A[i][c] != 0)
                    count++;
            }
            return count;
        }

        // gives the row number of the first found empty cell in the given column
        public int getEmptyCellInCol(int c) {
            for (int i = 0; i < 3; i++) {
                if (A[i][c] == 0)
                    return i;
            }
            return -1;
        }

        private void sortColumnWithThreeNumbers(int c) throws Exception {
            int emptyCell = this.getEmptyCellInCol(c);
            if (emptyCell != -1) {
                throw new Exception("Hey! your column has <3 cells filled, invalid function called");
            }
            int tempArr[] = new int[]{this.A[0][c], this.A[1][c], this.A[2][c]};
            Arrays.sort(tempArr);
            for (int r = 0; r < 3; r++) {
                this.A[r][c] = tempArr[r];
            }
        }

        private void sortColumnWithTwoNumbers(int c) throws Exception {
            int emptyCell = this.getEmptyCellInCol(c);
            if (emptyCell == -1) {
                throw new Exception("Hey! your column has 3 cells filled, invalid function called");
            }
            int cell1, cell2;
            if (emptyCell == 0) {
                cell1 = 1;
                cell2 = 2;
            } else if (emptyCell == 1) {
                cell1 = 0;
                cell2 = 2;
            } else { // emptyCell == 2
                cell1 = 0;
                cell2 = 1;
            }
            if (this.A[cell1][c] < this.A[cell2][c]) {
                return;
            } else {
                // swap
                int temp = this.A[cell1][c];
                this.A[cell1][c] = this.A[cell2][c];
                this.A[cell2][c] = temp;
            }
        }

        /*
         * Three possible scenarios: 1) only one number in the column - leave 2) Two
         * numbers in the column & not sorted - swap 3) Three numbers in the column -
         * sort
         */
        private void sortColumn(int c) throws Exception {
            if (this.getColCount(c) == 1) {
                return;
            } else if (this.getColCount(c) == 2) {
                this.sortColumnWithTwoNumbers(c);
            } else {
                this.sortColumnWithThreeNumbers(c);
            }
        }

        public void sortColumns() throws Exception {
            for (int c = 0; c < 9; c++) {
                this.sortColumn(c);
            }
        }
    }

    public static void main(String[] args) {
        try {
            // Load JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // JDBC connection parameters
            String url = "jdbc:mysql://localhost:3306/tambola";
            String username = "root";
            String password = "SaNkAr@11";

            // Establish a connection
            Connection connection = DriverManager.getConnection(url, username, password);

            // Your existing code goes here...

            // Example: Inserting data into a table
            // Assume you have a table named "tambola_tickets"
            // You need to replace the table name and column names accordingly
            Node[] nodes = new Node[6];
            for (int i = 0; i < 6; i++) {
                nodes[i] = new Node();
            }

            for (int i = 0; i < 6; i++) {
                Node currTicket = nodes[i];

                // Prepare SQL statement for insertion
                String insertQuery = "INSERT INTO tambola_sets (ticket_id, row1, row2, row3) VALUES (?, ?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);

                // Set parameters
                preparedStatement.setInt(1, i + 1); // Assuming ticket_id is an auto-incremented primary key
                preparedStatement.setString(2, Arrays.toString(currTicket.A[0])); // Convert row array to string
                preparedStatement.setString(3, Arrays.toString(currTicket.A[1]));
                preparedStatement.setString(4, Arrays.toString(currTicket.A[2]));

                // Execute the insert statement
                preparedStatement.executeUpdate();
            }

            // Close the connection
            //connection.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}
