/**
 *
 */
package cn.com.warlock.filesystem.sdk.fdfs.codec;

import cn.com.warlock.filesystem.sdk.fdfs.FileId;
import static cn.com.warlock.filesystem.sdk.fdfs.FastdfsConstants.Commands.SERVICE_QUERY_UPDATE;

/**
 * 获取可更新的存储服务器
 *
 * @author liulongbiao
 */
public class UpdateStorageGetEncoder extends FileIdOperationEncoder {

    public UpdateStorageGetEncoder(FileId fileId) {
        super(fileId);
    }

    @Override
    protected byte cmd() {
        return SERVICE_QUERY_UPDATE;
    }

}
