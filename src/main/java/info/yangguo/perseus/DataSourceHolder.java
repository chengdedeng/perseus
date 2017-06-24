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


    public static void setMaster() {
        setDataSource(MASTER);
    }

    public static void setSlave() {
        setDataSource(SLAVE);
    }

    public static boolean isMaster() {
        return MASTER.equals(getDataSource());
    }

    public static boolean isSlave() {
        return SLAVE.equals(getDataSource());
    }

    public static void clearDataSource() {
        dataSources.remove();
    }
}
