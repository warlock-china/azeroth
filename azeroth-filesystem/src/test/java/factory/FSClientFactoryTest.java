package factory;

import java.io.File;
import java.io.IOException;

import cn.com.warlock.filesystem.FSProvider;
import cn.com.warlock.filesystem.factory.FSClientFactory;

public class FSClientFactoryTest {

    /**
     * @param args
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {
        String url;
        FSProvider provider = FSClientFactory.build("qiniu", "jw56121830");
        url = provider.upload("test", null, new File("/Users/ayg/Desktop/logo.gif"));
        System.out.println(url);

        FSProvider provider2 = FSClientFactory.build("fastDFS", "group1");

        url = provider2.upload("test", null, new File("/Users/ayg/Desktop/logo.gif"));

        System.out.println(url);

        provider2.close();
    }

}
