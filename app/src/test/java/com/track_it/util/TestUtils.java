package com.track_it.util;

import com.track_it.application.Services;

import java.io.File;
import java.io.IOException;

import com.google.common.io.Files;
import com.track_it.logic.SubscriptionHandler;
import com.track_it.persistence.SubscriptionPersistence;
import com.track_it.persistence.fakes.FakeSubscriptionPersistenceDatabase;
import com.track_it.persistence.hsqldb.SubscriptionPersistenceHSQLDB;
import com.track_it.presentation.util.SetupParameters;

public class TestUtils {
    private static final File DB_SRC = new File("src/main/assets/db/Test.script");

    private static boolean useRealDatabase = false; //Should we use real database?




    public static File copyDB() throws IOException {
        final File target = File.createTempFile("temp-db", ".script");
        Files.copy(DB_SRC, target);
        comp3350.srsys.application.Main.setDBPathName(target.getAbsolutePath().replace(".script", ""));
        return target;
    }



    public static void changeDatabase()
    {
        if (TestUtils.getUseRealDatabase())
        {

            try {
                File tempDB;
                tempDB = TestUtils.copyDB();
                final SubscriptionPersistence persistence = new SubscriptionPersistenceHSQLDB(tempDB.getAbsolutePath().replace(".script", ""),"false");
                SetupParameters.initializeDatabase(persistence);
            }
            catch (IOException e)
            {
                System.out.println(e.getMessage());
                assert(false);

            }
        }
        else
        {
            SetupParameters.initializeDatabase(new FakeSubscriptionPersistenceDatabase());
        }
    }


    public static void setUseRealDatabase(boolean input) {
        useRealDatabase = input;
    }

    public static boolean getUseRealDatabase()
    {
        return useRealDatabase;
    }


    public static void setUpTestDataBase()
    {

        if ( useRealDatabase) //Use the a real SQL database.
        {

            try {
                //USE REAL DATABASE

                System.out.println("using the database for reals LLLLLLLLLLLL");
                File tempDB = TestUtils.copyDB();
                final SubscriptionPersistence persistence = new SubscriptionPersistenceHSQLDB( tempDB.getAbsolutePath().replace(".script", ""));
                SetupParameters.initializeDatabase(persistence);

            }
            catch(Exception e)
            {
                System.out.println("ERROR WITH SETTING UP TEST SQL DATABASE \n" + e.getMessage());
            }

        }
        else  //USE FAKE DATABASE
        {
            System.out.println("using the database for reals LLLLLLLLLLLL");

            SetupParameters.initializeDatabase(new FakeSubscriptionPersistenceDatabase());
        }

    }
}


