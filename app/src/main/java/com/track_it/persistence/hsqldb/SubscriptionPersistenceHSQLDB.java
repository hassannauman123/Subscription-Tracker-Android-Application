package com.track_it.persistence.hsqldb;


import android.util.Log;

import com.track_it.domainobject.SubscriptionObj;
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

    private SubscriptionObj fromResultSet(final ResultSet rs) throws SQLException {

        SubscriptionObj subToReturn = null;
        final String subscriptionName = rs.getString("name");
        final int paymentAmount = rs.getInt("paymentAmount");
        final String paymentFrequency = rs.getString("paymentFrequency");
        subToReturn = new SubscriptionObj(subscriptionName,paymentAmount,paymentFrequency);
        subToReturn.setID( rs.getInt("id"));

        return subToReturn;
    }


    public List<SubscriptionObj> getAllSubscriptions() {
        final List<SubscriptionObj> AllSubscriptions = new ArrayList<>();

        try (final Connection c = connect()) {

            final Statement st = c.createStatement();

            final ResultSet rs = st.executeQuery("SELECT * FROM SUBSCRIPTIONS");
            while (rs.next())
            {
               final SubscriptionObj subscription = fromResultSet(rs);
               AllSubscriptions.add(subscription);
            }
            rs.close();
            st.close();


        }
        catch (final SQLException e)
        {
            Log.e("Connect SQL", e.getMessage() + e.getSQLState());
            e.printStackTrace();
        }

        return AllSubscriptions;

    }


    public void editSubscriptionByID(int subscriptionID, SubscriptionObj newSubscriptionDetails)
    {
        SubscriptionObj subToReturn= null;

        try (Connection connection = connect())
        {
            final PreparedStatement statement = connection.prepareStatement("UPDATE SUBSCRIPTIONS set name = ?, paymentAmount = ?, paymentFrequency = ?  WHERE id = ?");

            statement.setString(1, newSubscriptionDetails.getName());
            statement.setInt(2, newSubscriptionDetails.getTotalPaymentInCents());
            statement.setString(3, newSubscriptionDetails.getPaymentFrequency());
            statement.setInt(4, subscriptionID);

           statement.executeUpdate();


        }
        catch (final SQLException e) {
            Log.e("Connect SQL", e.getMessage() + e.getSQLState());
            e.printStackTrace();
            System.out.println("ERROR ERROR ERROR " +e.getMessage() );
        }

    }

    public void addSubscriptionDataBase(SubscriptionObj subscriptionToAdd)
    {

    }

    public void removeSubscriptionByID(int subscriptionIDToRemove)
    {

    }

    public  SubscriptionObj getSubscriptionByID(int subscriptionID)
    {

        SubscriptionObj subToReturn= null;
        try (Connection connection = connect())
        {
            final PreparedStatement statement = connection.prepareStatement("SELECT * FROM SUBSCRIPTIONS WHERE id = ?");
            statement.setInt(1, subscriptionID);

            final ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {

                subToReturn = fromResultSet(resultSet);
                }
            }
        catch (final SQLException e) {
            Log.e("Connect SQL", e.getMessage() + e.getSQLState());
            e.printStackTrace();
        }

        return subToReturn;
    }



}
