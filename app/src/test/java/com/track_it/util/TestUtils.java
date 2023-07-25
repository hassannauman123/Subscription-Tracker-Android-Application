package com.track_it.util;

import com.track_it.application.Main;
import java.io.File;
import java.io.IOException;
import com.google.common.io.Files;
import com.track_it.persistence.SubscriptionPersistence;
import com.track_it.persistence.SubscriptionTagPersistence;
import com.track_it.persistence.fakes.FakeSubscriptionPersistenceDB;
import com.track_it.persistence.fakes.FakeSubscriptionTagPersistenceDB;
import com.track_it.persistence.hsqldb.SubscriptionPersistenceHSQLDB;
import com.track_it.application.SetupParameters;
import com.track_it.persistence.hsqldb.SubscriptionTagPersistenceHSQLDB;

public class TestUtils {
    private static final File DB_SRC = new File("src/main/assets/db/Test.script");

    private static boolean useRealDatabase = false; //Should we use real database? - We use this below in the changeDatabase function


    public static File copyDB() throws IOException {

        final File target = File.createTempFile("temp-db", ".script");
        Files.copy(DB_SRC, target);
        Main.setDBPathName(target.getAbsolutePath().replace(".script", ""));
        return target;
    }


    //Change the DataBase - Either to a real SQL database, or a fake database, depending on what useRealDatabase is set to
    public static void changeDatabase() {
        if (useRealDatabase)// Should we use a real SQL database
        {

            try {
                File tempDB;
                tempDB = TestUtils.copyDB();
                final SubscriptionPersistence subPersistence = new SubscriptionPersistenceHSQLDB(tempDB.getAbsolutePath().replace(".script", ""), "false");
                final SubscriptionTagPersistence subTagPersistence = new SubscriptionTagPersistenceHSQLDB(tempDB.getAbsolutePath().replace(".script", ""),"false") {
                };

                SetupParameters.initializeDatabase(subPersistence,subTagPersistence);
            } catch (IOException e) {
                System.out.println(e.getMessage());

            }
        } else //Use fake Database
        {

            SetupParameters.initializeDatabase(new FakeSubscriptionPersistenceDB(), new FakeSubscriptionTagPersistenceDB());
        }
    }


    // Change whether we should use real database
    public static void setUseRealDatabase(boolean input) {
        useRealDatabase = input;
    }

    //Get whether we should use real database, in case someone else wants this information
    public static boolean getUseRealDatabase() {
        return useRealDatabase;
    }


}


