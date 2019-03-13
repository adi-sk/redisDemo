package com.zycus.redis.dao;

import com.zycus.redis.Util;
import com.zycus.redis.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {

    private static final Logger LOG = LoggerFactory.getLogger(UserDao.class);

    public void addUser(User user) throws SQLException {
        Connection connection = Util.getdbConnection();
        String query = "INSERT INTO USER_DEMO (USER_ID,NAME,FOLLOWERS,EMAIL_ID) VALUES (?,?,?,?)";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,user.getUserId());
            preparedStatement.setString(2,user.getName());
            preparedStatement.setLong(3,user.getFollowers());
            preparedStatement.setString(4,user.getEmailId());

            int records = preparedStatement.executeUpdate();

            LOG.info(records + " inserted");

            if(!connection.getAutoCommit()){
                connection.commit();
            }

        }
        catch (SQLException e){
            LOG.error("Somwthing Went Wrong while insert");
            e.printStackTrace();
            connection.rollback();
        }
        finally {
            connection.close();
        }
    }

    public void updateUserName(String userId ,String userName) throws SQLException {
        Connection connection = Util.getdbConnection();
        String query = "UPDATE USER_DEMO SET NAME = ? WHERE USER_ID = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,userName);
            preparedStatement.setString(2,userId);

            int records = preparedStatement.executeUpdate();

            LOG.info(records + " updated");

            if(!connection.getAutoCommit()){
                connection.commit();
            }

        }
        catch (SQLException e){
            LOG.error("Somwthing Went Wrong while update");
            e.printStackTrace();
            connection.rollback();
        }
        finally {
            connection.close();
        }
    }

    public User getUser(String userId) throws SQLException{
        Connection connection = Util.getdbConnection();
        LOG.info("Inside user get dao !");

        String query = "SELECT USER_ID,NAME,FOLLOWERS,EMAIL_ID FROM USER_DEMO WHERE USER_ID = ?";
        User user = new User();
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,userId);

            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){

                user.setUserId(resultSet.getString("USER_ID"));
                user.setName(resultSet.getString("NAME"));
                user.setFollowers(resultSet.getLong("FOLLOWERS"));
                user.setEmailId(resultSet.getString("EMAIL_ID"));
            }

            if(!connection.getAutoCommit()){
                connection.commit();
            }
        }
        catch (SQLException e){
            LOG.error("Somwthing Went Wrong while update");
            e.printStackTrace();
        }
        finally {
            connection.close();
        }
        return user;
    }

    public void deleteUser(String userId) throws SQLException {
        Connection connection = Util.getdbConnection();
        String query = "DELETE FROM USER_DEMO WHERE USER_ID=?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,userId);

            int records = preparedStatement.executeUpdate();

            LOG.info(records + "Deleted !");

            if(!connection.getAutoCommit()){
                connection.commit();
            }
        }
        catch (SQLException e){
            LOG.error("Somwthing Went Wrong while Deleting User");
            e.printStackTrace();
        }
        finally {
            connection.close();
        }
    }

}
