package info.yangguo.perseus;

/**
 * 线程上下文的保存,主要是为了后面的数据源选择.
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
