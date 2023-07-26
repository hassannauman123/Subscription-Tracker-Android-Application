package com.track_it.persistence.hsqldb;

import android.util.Log;

import com.track_it.domainobject.SubscriptionObj;
import com.track_it.logic.exceptions.RetrievalException;
import com.track_it.logic.exceptions.RetrievalSubException;
import com.track_it.persistence.SubscriptionPersistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


//This class implements a HSQL database for subscriptions.

public class SubscriptionPersistenceHSQLDB implements SubscriptionPersistence {


    private final String dbPath;
    private final String shutDownString;


    public SubscriptionPersistenceHSQLDB(final String dbPath) {
        this.shutDownString = "true";
        this.dbPath = dbPath;
    }


    // Constructor method, but also lets you Set the shutdown="" option when connecting to a database
    public SubscriptionPersistenceHSQLDB(final String dbPath, final String inputShutDown) {
        this.shutDownString = inputShutDown;
        this.dbPath = dbPath;
    }


    private Connection connect() throws SQLException {
        return DriverManager.getConnection("jdbc:hsqldb:file:" + dbPath + ";shutdown=" + shutDownString, "SA", "");
    }


    // Create and return a subscriptionObj from the input resultSet
    private SubscriptionObj fromResultSet(final ResultSet resultSet) throws SQLException {

        SubscriptionObj subToReturn = null;
        final String subscriptionName = resultSet.getString("name");
        final int paymentAmount = resultSet.getInt("paymentAmount");
        final String paymentFrequency = resultSet.getString("paymentFrequency");
        subToReturn = new SubscriptionObj(subscriptionName, paymentAmount, paymentFrequency);
        subToReturn.setID(resultSet.getInt("id"));
        return subToReturn;
    }


    @Override
    // return a list of all subscriptions in the database
    public List<SubscriptionObj> getAllSubscriptions() throws RetrievalException {
        final List<SubscriptionObj> AllSubscriptions = new ArrayList<>();

        try (final Connection c = connect()) {

            final Statement statement = c.createStatement();

            final ResultSet returnedResults = statement.executeQuery("SELECT * FROM SUBSCRIPTIONS");
            while (returnedResults.next()) {
                final SubscriptionObj subscription = fromResultSet(returnedResults);
                AllSubscriptions.add(subscription);
            }
            returnedResults.close();
            statement.close();
            c.close();

        } catch (final SQLException e) {
            Log.e("Connect SQL", e.getMessage() + e.getSQLState());
            e.printStackTrace();
            throw new RetrievalException("Unable to get all subscriptions");
        }


        return AllSubscriptions;
    }


    @Override
    // Edit the details of subscription object in the database.
    public void editSubscriptionByID(int subscriptionIDToEdit, final SubscriptionObj newSubscriptionDetails) throws RetrievalException {

        //If a subscription with id of subscriptionID is not found in database throw a DataBaseException exception
        if (!subscriptionInDatabase(subscriptionIDToEdit)) {
            throw new RetrievalSubException();
        }


        SubscriptionObj subToReturn = null;

        try (Connection connection = connect()) {
            final PreparedStatement statement = connection.prepareStatement("UPDATE SUBSCRIPTIONS set name = ?, paymentAmount = ?, paymentFrequency = ?  WHERE id = ?");

            statement.setString(1, newSubscriptionDetails.getName());
            statement.setInt(2, newSubscriptionDetails.getTotalPaymentInCents());
            statement.setString(3, newSubscriptionDetails.getPaymentFrequency());
            statement.setInt(4, subscriptionIDToEdit);

            statement.executeUpdate();
            connection.close();


        } catch (final SQLException e) {
            Log.e("Connect SQL", e.getMessage() + e.getSQLState());
            e.printStackTrace();
            throw new RetrievalException("Unable to edit subscription");
        }

    }

    @Override
    public void addSubscriptionToDB(SubscriptionObj subscriptionToAdd) throws RetrievalException {

        try (Connection connection = connect()) {
            final PreparedStatement statement = connection.prepareStatement("INSERT INTO SUBSCRIPTIONS VALUES(DEFAULT, ?,?, ?)", Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, subscriptionToAdd.getName());
            statement.setString(2, subscriptionToAdd.getPaymentFrequency());
            statement.setInt(3, subscriptionToAdd.getTotalPaymentInCents());

            statement.executeUpdate(); // execute insertion statement

            // We need to the get the ID of the subscription added to the database so that we can set the ID of the subscription object
            final ResultSet returnedResults = statement.getGeneratedKeys();

            if (returnedResults.next()) {
                subscriptionToAdd.setID(returnedResults.getInt(1));
            }

            statement.close();
            connection.close();

        } catch (final SQLException e) {
            Log.e("Connect SQL", e.getMessage() + e.getSQLState());
            e.printStackTrace();
            throw new RetrievalException("unable to save subscription ");

        }

    }


    @Override
    public void removeSubscriptionByID(int subscriptionIDToRemove) throws RetrievalException {

        //If a subscription with id of subscriptionID is not found in database throw an exception
        if (!subscriptionInDatabase(subscriptionIDToRemove)) {
            throw new RetrievalSubException();
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
            throw new RetrievalException("Unable to remove subscription");
        }
    }

    @Override
    public SubscriptionObj getSubscriptionByID(int subscriptionIDtoGet) throws RetrievalException {
        //If a subscription with id of subscriptionID is not found in database throw a Database exception
        if (!subscriptionInDatabase(subscriptionIDtoGet)) {
            throw new RetrievalSubException();
        }

        // Otherwise get the subscription from the database, and return it
        SubscriptionObj subToReturn = null;


        try (Connection connection = connect()) {
            final PreparedStatement statement = connection.prepareStatement("SELECT * FROM SUBSCRIPTIONS WHERE id = ?");
            statement.setInt(1, subscriptionIDtoGet);

            final ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {

                subToReturn = fromResultSet(resultSet);
            }
            return subToReturn;

        } catch (final SQLException e) {
            Log.e("Connect SQL", e.getMessage() + e.getSQLState());
            e.printStackTrace();
            throw new RetrievalException("Unable to retrieve subscription");

        }
    }


    //Returns true if a subscription with id of subscriptionID is found in database, else returns false
    private boolean subscriptionInDatabase(int subscriptionID) throws RetrievalException {

        boolean found = false;

        try (Connection connection = connect()) {
            final PreparedStatement statement = connection.prepareStatement("SELECT * FROM SUBSCRIPTIONS WHERE id = ?");
            statement.setInt(1, subscriptionID);

            final ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {

                found = true;
            }
            connection.close();
            return found;
        } catch (final SQLException e) {
            Log.e("Connect SQL", e.getMessage() + e.getSQLState());
            e.printStackTrace();
            throw new RetrievalException("Error with checking if subscription exists");
        }
    }


}
