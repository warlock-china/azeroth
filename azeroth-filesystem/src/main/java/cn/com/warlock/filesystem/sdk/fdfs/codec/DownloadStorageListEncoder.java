/**
 *
 */
package cn.com.warlock.filesystem.sdk.fdfs.codec;

import cn.com.warlock.filesystem.sdk.fdfs.FileId;
import static cn.com.warlock.filesystem.sdk.fdfs.FastdfsConstants.Commands.SERVICE_QUERY_FETCH_ALL;

/**
 * 获取可下载的存储服务器列表
 *
 * @author liulongbiao
 */
public class DownloadStorageListEncoder extends FileIdOperationEncoder {

    public DownloadStorageListEncoder(FileId fileId) {
        super(fileId);
    }

    @Override
    protected byte cmd() {
        return SERVICE_QUERY_FETCH_ALL;
    }

}
