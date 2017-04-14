package cn.com.warlock.confcenter.utils;

public class ConfigZkPathUtils {

    private static final String ZK_ROOT = "/confcenter/";

    public static String getConfigFilePath(String env, String app, String version,
                                           String fileName) {
        String path = ZK_ROOT + env + "/" + app + "/" + version + "/" + fileName;
        return path;
    }
}
