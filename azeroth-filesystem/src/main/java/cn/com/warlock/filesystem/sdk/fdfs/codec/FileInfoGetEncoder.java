package cn.com.warlock.filesystem.sdk.fdfs.codec;

import cn.com.warlock.filesystem.sdk.fdfs.FastdfsConstants;
import cn.com.warlock.filesystem.sdk.fdfs.FileId;

/**
 * @author siuming
 */
public class FileInfoGetEncoder extends FileIdOperationEncoder {

    /**
     * @param fileId
     */
    public FileInfoGetEncoder(FileId fileId) {
        super(fileId);
    }

    @Override
    protected byte cmd() {
        return FastdfsConstants.Commands.FILE_QUERY;
    }
}
