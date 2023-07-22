package com.track_it.util;


import android.content.Context;
import android.content.res.AssetManager;

import androidx.test.platform.app.InstrumentationRegistry;

import com.google.common.io.Files;
import com.track_it.application.Main;
import com.track_it.application.SetupParameters;
import com.track_it.persistence.SubscriptionPersistence;
import com.track_it.persistence.SubscriptionTagPersistence;
import com.track_it.persistence.fakes.FakeSubscriptionPersistenceDatabase;
import com.track_it.persistence.fakes.FakeSubscriptionTagPersistenceDatabase;
import com.track_it.persistence.hsqldb.SubscriptionPersistenceHSQLDB;
import com.track_it.persistence.hsqldb.SubscriptionTagPersistenceHSQLDB;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class TestUtils {
    private static File DB_SRC;

    private static boolean useRealDatabase = false; //Should we use real database? - Default false means use fakeDataBase




    public static File copyDB() throws IOException {

       // Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        //AssetManager am = context.getAssets();

       // InputStream DB_SRC = am.open("Test.script");
       // writeBytesToFile(DB_SRC);

        //final File target = File.createTempFile("temp-db", "");
        //writeBytesToFile(DB_SRC, DB_SRC);
       // Main.setDBPathName("Test");
       return null;
    }




    public static void writeBytesToFile(InputStream is, File file) throws IOException{
        FileOutputStream fos = null;
        try {
            byte[] data = new byte[2048];
            int nbread = 0;
            fos = new FileOutputStream(file);
            while((nbread=is.read(data))>-1){
                fos.write(data,0,nbread);
            }
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        finally{
            if (fos!=null){
                fos.close();
            }
        }
    }

    //Change the DataBase - Either to a real SQL database, or a fake database, depending on what useRealDatabase is set to
    public static void changeDatabase() {
        if (TestUtils.getUseRealDatabase())//Use real SQL database
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
                assert (false);

            }
        } else //Use fake Database
        {

            SetupParameters.initializeDatabase(new FakeSubscriptionPersistenceDatabase(), new FakeSubscriptionTagPersistenceDatabase());
        }
    }


    // Change whether we should use real database
    public static void setUseRealDatabase(boolean input) {
        useRealDatabase = input;
    }

    //Get whether we should use real database
    public static boolean getUseRealDatabase() {
        return useRealDatabase;
    }


}


