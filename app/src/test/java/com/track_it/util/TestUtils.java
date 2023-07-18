package com.track_it.util;


import com.track_it.application.Main;
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

    private static boolean useRealDatabase = false; //Should we use real database? - Default false means use fakeDataBase


    public static File copyDB() throws IOException {
        final File target = File.createTempFile("temp-db", ".script");
        Files.copy(DB_SRC, target);
        Main.setDBPathName(target.getAbsolutePath().replace(".script", ""));
        return target;
    }


    //Change the DataBase - Either to a real SQL database, or a fake database, depending on what useRealDatabase is set to
    public static void changeDatabase()
    {
        if (TestUtils.getUseRealDatabase())//Use real SQL database
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
        else //Use fake Database
        {

            SetupParameters.initializeDatabase(new FakeSubscriptionPersistenceDatabase());
        }
    }


    // Change whether we should use real database
    public static void setUseRealDatabase(boolean input) {
        useRealDatabase = input;
    }

    //Get whether we should use real database
    public static boolean getUseRealDatabase()
    {
        return useRealDatabase;
    }



}


