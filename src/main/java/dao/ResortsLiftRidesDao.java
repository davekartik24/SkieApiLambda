package dao;

import model.ResortsLiftRides;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ResortsLiftRidesDao {

    public int getTotalVertical(int resortID, String seasonId, String dayId, int skierId) throws SQLException, ClassNotFoundException {
        StringBuilder sb = new StringBuilder();
        sb.append(resortID).append(seasonId).append(dayId).append(skierId);
        String aggregateQueryStatement = "SELECT sum(vertical) as total_vertical FROM resorts_lift_rides WHERE filter_id = ?";
        Connection conn = DBCPDataSource.getConnection();
        try(PreparedStatement preparedStatement = conn.prepareStatement(aggregateQueryStatement);) {
            preparedStatement.setString(1, sb.toString());

            ResultSet result = preparedStatement.executeQuery();

            int totalVertical = 0;
            if(result.next()){
                totalVertical = result.getInt("total_vertical");

                if (totalVertical == 0) {
                    conn.close();
                    throw new SQLException("Data Not Found");
                }
            }
            conn.close();
            return totalVertical;
        } catch (SQLException ex) {
            conn.close();
            throw new SQLException(ex);
        }
    }

    public void createLiftRide(ResortsLiftRides newLiftRide) throws ClassNotFoundException, SQLException {
        String insertQueryStatement = "INSERT INTO resorts_lift_rides(id, filter_id, vertical) " +
                "VALUES (?, ?, ?)";

        StringBuilder sb = new StringBuilder();
        sb.append(newLiftRide.getResortId()).append(newLiftRide.getSeasonId()).append(newLiftRide.getDayId()).append(newLiftRide.getSkierId());
        try(Connection conn = DBCPDataSource.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(insertQueryStatement)){
            preparedStatement.setString(1, newLiftRide.getId());
            preparedStatement.setString(2, sb.toString());
            preparedStatement.setInt(3, newLiftRide.getVertical());

            preparedStatement.executeUpdate();
        } catch (SQLException se) {
            throw new SQLException(se);
        }
    }
}