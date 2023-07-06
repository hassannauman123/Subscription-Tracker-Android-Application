package comp3350.srsys.application;


public class Main
{
    private static String dbName="SubscriptionDB";

    public static void main(String[] args)
    {

        System.out.println("All done");
    }

    public static void setDBPathName(final String name) {
        try {
            Class.forName("org.hsqldb.jdbcDriver").newInstance();
        } catch (InstantiationException e) {
           System.out.println("ERROR1");
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            System.out.println("ERROR1");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("ERROR1");
            e.printStackTrace();
        }
        dbName = name;
        System.out.println("Complete " + name);
    }

    public static String getDBPathName() {

        System.out.println("dbName is : " + dbName);
        return dbName;
    }
}

