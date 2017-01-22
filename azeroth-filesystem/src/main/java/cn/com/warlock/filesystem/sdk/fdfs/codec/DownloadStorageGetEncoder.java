/**
 *
 */
package cn.com.warlock.filesystem.sdk.fdfs.codec;

import cn.com.warlock.filesystem.sdk.fdfs.FileId;
import static cn.com.warlock.filesystem.sdk.fdfs.FastdfsConstants.Commands.SERVICE_QUERY_FETCH_ONE;

public class DownloadStorageGetEncoder extends FileIdOperationEncoder {

    public DownloadStorageGetEncoder(FileId fileId) {
        super(fileId);
    }

    @Override
    protected byte cmd() {
        return SERVICE_QUERY_FETCH_ONE;
    }

}
