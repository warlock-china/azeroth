/**
 * 
 */
package cn.com.warlock.filesystem.provider;

import cn.com.warlock.filesystem.FSProvider;
import cn.com.warlock.filesystem.utils.HttpDownloader;

public abstract class AbstractProvider implements FSProvider{

	protected static final String DIR_SPLITER = "/";
	
	protected String urlprefix;

	protected String bucketName;
	
	@Override
	public String getPath(String fileName) {
		try {
			String url = getFullPath(fileName);
			if (HttpDownloader.read(url) == null) {
				throw new FSOperErrorException(name(), "文件不存在");
			}
			return url;
		} catch (Exception e) {
			throw new FSOperErrorException(name(), e);
		}
	}

	protected String getFullPath(String key) {
		return urlprefix + key;
	}
	
	public abstract String name();
}
