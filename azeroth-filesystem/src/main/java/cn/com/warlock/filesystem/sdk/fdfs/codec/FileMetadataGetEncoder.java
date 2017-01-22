/**
 *
 */
package cn.com.warlock.filesystem.sdk.fdfs.codec;

import cn.com.warlock.filesystem.sdk.fdfs.FastdfsConstants;
import cn.com.warlock.filesystem.sdk.fdfs.FileId;

/**
 * 获取文件属性请求
 *
 * @author liulongbiao
 */
public class FileMetadataGetEncoder extends FileIdOperationEncoder {

    public FileMetadataGetEncoder(FileId fileId) {
        super(fileId);
    }

    @Override
    public byte cmd() {
        return FastdfsConstants.Commands.METADATA_GET;
    }

}