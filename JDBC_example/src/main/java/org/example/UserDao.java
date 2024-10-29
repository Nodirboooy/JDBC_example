package org.example;

import java.sql.*;

public class UserDao {

public  UserDao(){ }

    static String signUp(String name, String email, String password, String username, String phoneNumber) {
        String result = "";
        String sql = "{ ? = call sign_up(?, ?, ?, ?, ?) }";

        try (CallableStatement stmt = DatabaseManager.getConnection().prepareCall(sql)) {
            stmt.registerOutParameter(1, java.sql.Types.VARCHAR);
            stmt.setString(2, name);
            stmt.setString(3, email);
            stmt.setString(4, password);
            stmt.setString(5, username);
            stmt.setString(6, phoneNumber);

            stmt.execute();
            result = stmt.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }



    public String signIn(String email, String password) {
        String result = "";
        try (Connection conn = DatabaseManager.getConnection();
             CallableStatement stmt = conn.prepareCall("{? = call sign_in(?, ?)}")) {
            stmt.registerOutParameter(1, Types.VARCHAR);
            stmt.setString(2, email);
            stmt.setString(3, password);
            stmt.execute();
            result = stmt.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public Long getUserIdByEmail(String email) {
        String query = "SELECT id FROM users_test WHERE email = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getLong("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String addCard(int userId, String bankName, java.sql.Date expiryDate) {
        String result = "";
        try (Connection conn = DatabaseManager.getConnection();
             CallableStatement stmt = conn.prepareCall("{? = call add_card(?, ?, ?)}")) {
            stmt.registerOutParameter(1, Types.VARCHAR);
            stmt.setInt(2, userId);
            stmt.setString(3, bankName);
            stmt.setDate(4, expiryDate);
            stmt.execute();
            result = stmt.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void getMyCards(Integer cardId) {
        String sql = "{call get_my_cards(?)}";

        try (Connection conn = DatabaseManager.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {


            if (cardId == null) {
                stmt.setNull(1, Types.INTEGER);
            } else {
                stmt.setInt(1, cardId);
            }


            boolean hasResult = stmt.execute();

            if (hasResult) {
                try (ResultSet rs = stmt.getResultSet()) {
                    while (rs.next()) {
                        int retrievedCardId = rs.getInt("card_id");
                        int retrievedUserId = rs.getInt("user_id");
                        String bankName = rs.getString("bank_name");
                        Date expiryDate = rs.getDate("expiry_date");

                        System.out.println("Card ID: " + retrievedCardId);
                        System.out.println("User ID: " + retrievedUserId);
                        System.out.println("Bank Name: " + bankName);
                        System.out.println("Expiry Date: " + expiryDate);
                        System.out.println("-----------------------------");
                    }
                }
            } else {
                System.out.println("Hech qanday karta topilmadi.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void deleteCard(int cardId) {
        try (Connection conn = DatabaseManager.getConnection();
             CallableStatement stmt = conn.prepareCall("{call delete_card(?)}")) {
            stmt.setInt(1, cardId);
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

