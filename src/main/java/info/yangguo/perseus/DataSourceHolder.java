package info.yangguo.perseus;

/**
 * The selected data source will be saved in the class.
 */
public class DataSourceHolder {
    private static final String MASTER = "master";
    private static final String SLAVE = "slave";
    private static final ThreadLocal<String> dataSources = new ThreadLocal<>();

    private static void setDataSource(String dataSourceKey) {
        dataSources.set(dataSourceKey);
    }

    private static String getDataSource() {
        return dataSources.get();
    }


    protected static void setMaster() {
        setDataSource(MASTER);
    }

    protected static void setSlave() {
        setDataSource(SLAVE);
    }

    protected static boolean isMaster() {
        return MASTER.equals(getDataSource());
    }

    protected static boolean isSlave() {
        return SLAVE.equals(getDataSource());
    }

    protected static boolean haveValue() {
        if (dataSources.get() != null) {
            return true;
        } else {
            return false;
        }
    }

    protected static void clearDataSource() {
        dataSources.remove();
    }
}
