package com.track_it.persistence.hsqldb;

import android.util.Log;

import com.track_it.domainobject.SubscriptionObj;
import com.track_it.domainobject.SubscriptionTag;
import com.track_it.logic.exceptions.RetrievalException;
import com.track_it.persistence.SubscriptionTagPersistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SubscriptionTagPersistenceHSQLDB implements SubscriptionTagPersistence {

    private final String dbPath;
    private final String shutDownString;


    public SubscriptionTagPersistenceHSQLDB(final String dbPath) {
        this.shutDownString = "true";
        this.dbPath = dbPath;
    }


    // Constructor method, but also lets you Set the shutdown="" option when connecting to a database
    public SubscriptionTagPersistenceHSQLDB(final String dbPath, final String inputShutDown) {
        this.shutDownString = inputShutDown;
        this.dbPath = dbPath;
    }


    private Connection connect() throws SQLException {
        return DriverManager.getConnection("jdbc:hsqldb:file:" + dbPath + ";shutdown=" + shutDownString, "SA", "");
    }


    // Create and return a SubscriptionTag from the input resultSet
    private SubscriptionTag fromResultSet(final ResultSet resultSet) throws SQLException {

        SubscriptionTag tagToReturn = new SubscriptionTag(resultSet.getString("name"));
        tagToReturn.setID(resultSet.getInt("id"));
        return tagToReturn;
    }

    @Override
    public void changeSubscriptionTags(SubscriptionObj inputSub) {
        try (Connection connection = connect()) {

            final PreparedStatement statement = connection.prepareStatement("DELETE from SUBSCRIPTIONS_TAGS where SUBSCRIPTIONS_TAGS.subscription_id = ?");
            statement.setInt(1, inputSub.getID());
            statement.executeUpdate();

            for (SubscriptionTag insertTag : inputSub.getTagList()) {
                addTagToPersistence(insertTag);
                associateTagWithSubscription(inputSub, insertTag);
            }

        } catch (final SQLException e) {
            Log.e("Connect SQL", e.getMessage() + e.getSQLState());
            e.printStackTrace();
            throw new RetrievalException("Unable to change tags of subscription");
        }

    }

    @Override
    public List<SubscriptionTag> getAllTags() {

        List<SubscriptionTag> listAllTags = new ArrayList<SubscriptionTag>();

        try (Connection connection = connect()) {

            final PreparedStatement statement = connection.prepareStatement("SELECT * from TAGS");

            final ResultSet returnedResults = statement.executeQuery();

            while (returnedResults.next()) {
                final SubscriptionTag newTag = fromResultSet(returnedResults);
                listAllTags.add(newTag);
            }


        } catch (final SQLException e) {
            Log.e("Connect SQL", e.getMessage() + e.getSQLState());
            e.printStackTrace();
            throw new RetrievalException("Unable to get all tags");
        }

        return listAllTags;


    }


    @Override
    public void removeUnusedTags() {
        try (Connection connection = connect()) {

            final PreparedStatement statement = connection.prepareStatement("DELETE from TAGS where id NOT IN (SELECT tag_id from SUBSCRIPTIONS_TAGS)");
            statement.executeUpdate();


        } catch (final SQLException e) {
            Log.e("Connect SQL", e.getMessage() + e.getSQLState());
            e.printStackTrace();
            throw new RetrievalException("Unable to remove unused tags");

        }
    }


    @Override
    public List<SubscriptionTag> getTagsForSubscription(SubscriptionObj inputSubscription) throws RetrievalException {
        List<SubscriptionTag> listOfTagsForSub = new ArrayList<SubscriptionTag>();

        try (Connection connection = connect()) {

            final PreparedStatement statement = connection.prepareStatement("SELECT * from TAGS join SUBSCRIPTIONS_TAGS on TAGS.id = SUBSCRIPTIONS_TAGS.tag_id where SUBSCRIPTIONS_TAGS.subscription_id = ?");
            statement.setInt(1, inputSubscription.getID());


            final ResultSet returnedResults = statement.executeQuery();

            while (returnedResults.next()) {
                final SubscriptionTag newTag = fromResultSet(returnedResults);
                listOfTagsForSub.add(newTag);
            }


        } catch (final SQLException e) {
            Log.e("Connect SQL", e.getMessage() + e.getSQLState());
            e.printStackTrace();
            throw new RetrievalException("Unable to get tags for a subscription");
        }

        return listOfTagsForSub;

    }


    @Override
    public void removeAllTagsBySubID(int inputSub) throws RetrievalException {
        try (Connection connection = connect()) {

            final PreparedStatement statement = connection.prepareStatement("DELETE from SUBSCRIPTIONS_TAGS where SUBSCRIPTIONS_TAGS.subscription_id = ?");
            statement.setInt(1, inputSub);
            statement.executeUpdate();

        } catch (final SQLException e) {
            Log.e("Connect SQL", e.getMessage() + e.getSQLState());
            e.printStackTrace();
            throw new RetrievalException("Unable to remove a subscriptions tags");
        }

    }


    @Override
    public void addTagToPersistence(SubscriptionTag insertTag) throws RetrievalException {
        if (!isTagInDatabase(insertTag.getName())) // If the tag is not already present in database
        {

            try (Connection connection = connect()) {

                final PreparedStatement statement = connection.prepareStatement("INSERT INTO TAGS VALUES(DEFAULT, ?)", Statement.RETURN_GENERATED_KEYS);
                statement.setString(1, insertTag.getName());


                // We need to the get the ID of the subscription added to the database so that we can set the ID of the subscription object

                statement.executeUpdate();

                final ResultSet returnedResults = statement.getGeneratedKeys();

                if (returnedResults.next()) {

                    insertTag.setID(returnedResults.getInt(1));
                }

                statement.close();
                connection.close();

            } catch (final SQLException e) {
                Log.e("Connect SQL", e.getMessage() + e.getSQLState());
                e.printStackTrace();
                throw new RetrievalException("Unable save a tag");
            }
        } else // Else set it's id by the value in database
        {
            setTagID(insertTag);

        }
    }

    private void associateTagWithSubscription(SubscriptionObj inputSubscription, SubscriptionTag insertTag) throws RetrievalException {
        try (Connection connection = connect()) {
            final PreparedStatement statement = connection.prepareStatement("INSERT INTO SUBSCRIPTIONS_TAGS VALUES(?, ?)");
            statement.setInt(1, inputSubscription.getID());
            statement.setInt(2, insertTag.getID());

            statement.executeUpdate(); // execute insertion statement
            statement.close();
            connection.close();

        } catch (final SQLException e) {
            Log.e("Connect SQL", e.getMessage() + e.getSQLState());
            e.printStackTrace();
            throw new RetrievalException("Unable to change a subscriptions tags");
        }
    }


    //Set a tag ID if it is already present in database
    private void setTagID(SubscriptionTag setTag) throws RetrievalException {

        try (Connection connection = connect()) {
            final PreparedStatement statement = connection.prepareStatement("SELECT * FROM TAGS WHERE TAGS.name = ?");
            statement.setString(1, setTag.getName());


            // We need to the get the ID of the subscription added to the database so that we can set the ID of the subscription object
            final ResultSet returnedResults = statement.executeQuery();

            if (returnedResults.next()) {
                setTag.setID(returnedResults.getInt("id"));
            }

            statement.close();
            connection.close();


        } catch (final SQLException e) {
            Log.e("Connect SQL", e.getMessage() + e.getSQLState());
            e.printStackTrace();
            throw new RetrievalException(e.getMessage());
        }


    }

    private boolean isTagInDatabase(String searchTag) throws RetrievalException {

        boolean found = false;

        try (Connection connection = connect()) {
            final PreparedStatement statement = connection.prepareStatement("SELECT * FROM TAGS WHERE TAGS.name = ?");
            statement.setString(1, searchTag);


            final ResultSet returnedResults = statement.executeQuery();

            if (returnedResults.next()) {
                found = true;

            }
            statement.close();
            connection.close();


        } catch (final SQLException e) {
            Log.e("Connect SQL", e.getMessage() + e.getSQLState());
            e.printStackTrace();
            throw new RetrievalException(e.getMessage());
        }


        return found;
    }


}
