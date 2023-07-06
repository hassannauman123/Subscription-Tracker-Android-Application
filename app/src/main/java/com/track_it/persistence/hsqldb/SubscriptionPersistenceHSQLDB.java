package com.track_it.persistence.hsqldb;


import android.util.Log;

import com.track_it.domainobject.SubscriptionObj;
import com.track_it.logic.exception.DataBaseException;
import com.track_it.logic.exception.DataBaseSubNotFoundException;
import com.track_it.persistence.SubscriptionPersistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class SubscriptionPersistenceHSQLDB implements SubscriptionPersistence {


    private final String dbPath;

    public SubscriptionPersistenceHSQLDB(final String dbPath) {
        this.dbPath = dbPath;
    }


    private Connection connect() throws SQLException {
        return DriverManager.getConnection("jdbc:hsqldb:file:" + dbPath + ";shutdown=true", "SA", "");
    }

    // Create and return a subscriptionObj from the input resultSet
    private SubscriptionObj fromResultSet(final ResultSet resultSet) throws SQLException {

        SubscriptionObj subToReturn = null;
        final String subscriptionName = resultSet.getString("name");
        final int paymentAmount = resultSet.getInt("paymentAmount");
        final String paymentFrequency = resultSet.getString("paymentFrequency");
        subToReturn = new SubscriptionObj(subscriptionName,paymentAmount,paymentFrequency);
        subToReturn.setID( resultSet.getInt("id"));
        return subToReturn;
    }


    // return a list of all subscriptions in the database
    public List<SubscriptionObj> getAllSubscriptions() {
        final List<SubscriptionObj> AllSubscriptions = new ArrayList<>();

        try (final Connection c = connect()) {

            final Statement statement = c.createStatement();

            final ResultSet returnedResults = statement.executeQuery("SELECT * FROM SUBSCRIPTIONS");
            while (returnedResults.next())
            {
               final SubscriptionObj subscription = fromResultSet(returnedResults);
               AllSubscriptions.add(subscription);
            }
            returnedResults.close();
            statement.close();
        }
        catch (final SQLException e)
        {
            Log.e("Connect SQL", e.getMessage() + e.getSQLState());
            e.printStackTrace();
            throw new DataBaseException(e.getMessage());
        }
        return AllSubscriptions;
    }

    public void editSubscriptionByID(int subscriptionIDToEdit, SubscriptionObj newSubscriptionDetails) throws DataBaseException
    {

        //If a subscription with id of subscriptionID is not found in database throw a DataBaseException exception
        if ( !subscriptionInDatabase(subscriptionIDToEdit) )
        {
            throw new DataBaseSubNotFoundException();
        }


        SubscriptionObj subToReturn= null;

        try (Connection connection = connect())
        {
            final PreparedStatement statement = connection.prepareStatement("UPDATE SUBSCRIPTIONS set name = ?, paymentAmount = ?, paymentFrequency = ?  WHERE id = ?");

            statement.setString(1, newSubscriptionDetails.getName());
            statement.setInt(2, newSubscriptionDetails.getTotalPaymentInCents());
            statement.setString(3, newSubscriptionDetails.getPaymentFrequency());
            statement.setInt(4, subscriptionIDToEdit);

           statement.executeUpdate();


        }
        catch (final SQLException e) {
            Log.e("Connect SQL", e.getMessage() + e.getSQLState());
            e.printStackTrace();
            throw new DataBaseException(e.getMessage());
        }

    }

    public void addSubscriptionToDB(SubscriptionObj subscriptionToAdd) throws DataBaseException
    {

        try (Connection connection = connect()) {
            final PreparedStatement statement = connection.prepareStatement("INSERT INTO SUBSCRIPTIONS VALUES(DEFAULT, ?,?, ?)");
            statement.setString(1, subscriptionToAdd.getName());
            statement.setString(2, subscriptionToAdd.getPaymentFrequency());
            statement.setInt(3, subscriptionToAdd.getTotalPaymentInCents());

            statement.executeUpdate(); // execute insertion statement

            // We need to the get the ID of the subscription added to the database so that we can set the ID of the subscription object
           final ResultSet returnedResults =  statement.getGeneratedKeys();

           if ( returnedResults.next()) {
               subscriptionToAdd.setID(returnedResults.getInt(0));
           }
            statement.close();


        } catch (final SQLException e) {
             Log.e("Connect SQL", e.getMessage() + e.getSQLState());
            e.printStackTrace();
            throw new DataBaseException(e.getMessage());

        }

    }

    public void removeSubscriptionByID(int subscriptionIDToRemove) throws DataBaseException
    {
        //If a subscription with id of subscriptionID is not found in database throw a DataBaseException exception
        if ( !subscriptionInDatabase(subscriptionIDToRemove) )
        {
            throw new DataBaseSubNotFoundException();
        }


        // Otherwise remove the subscription from the database
        try (Connection connection = connect()) {
            final PreparedStatement statement = connection.prepareStatement("DELETE FROM SUBSCRIPTIONS WHERE ID = ?");
              statement.setInt(1, subscriptionIDToRemove);

            statement.executeUpdate(); // execute delete statement
            statement.close();


        } catch (final SQLException e) {
            Log.e("Connect SQL", e.getMessage() + e.getSQLState());
            e.printStackTrace();
            throw new DataBaseException(e.getMessage());

        }
    }

    public  SubscriptionObj getSubscriptionByID(int subscriptionIDtoGet) throws DataBaseException
    {
        //If a subscription with id of subscriptionID is not found in database throw a DataBaseException exception
        if ( !subscriptionInDatabase(subscriptionIDtoGet) )
        {
            throw new DataBaseSubNotFoundException();
        }

        // Otherwise get the subscription from the database, and return it
        SubscriptionObj subToReturn = null;
        try (Connection connection = connect())
        {
            final PreparedStatement statement = connection.prepareStatement("SELECT * FROM SUBSCRIPTIONS WHERE id = ?");
            statement.setInt(1, subscriptionIDtoGet);

            final ResultSet resultSet = statement.executeQuery();
            if (resultSet.next())
            {

                subToReturn = fromResultSet(resultSet);
            }
            return subToReturn;

        }

        catch (final SQLException e) {
            Log.e("Connect SQL", e.getMessage() + e.getSQLState());
            e.printStackTrace();
            throw new DataBaseException(e.getMessage());

        }
    }


    //Returns true if a subscription with id of subscriptionID is found in database, else return false
    private boolean subscriptionInDatabase(int subscriptionID)  throws DataBaseException
    {

        boolean found = false;

        try (Connection connection = connect())
        {
            final PreparedStatement statement = connection.prepareStatement("SELECT * FROM SUBSCRIPTIONS WHERE id = ?");
            statement.setInt(1, subscriptionID);

            final ResultSet resultSet = statement.executeQuery();
            if (resultSet.next())
            {

                found = true;
            }
            return found;
        }

        catch (final SQLException e) {
            Log.e("Connect SQL", e.getMessage() + e.getSQLState());
            e.printStackTrace();
            throw new DataBaseException(e.getMessage());
        }
    }


}
