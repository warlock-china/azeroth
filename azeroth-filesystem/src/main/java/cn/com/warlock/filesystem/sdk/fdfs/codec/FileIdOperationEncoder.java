package cn.com.warlock.filesystem.sdk.fdfs.codec;

import cn.com.warlock.filesystem.sdk.fdfs.FileId;
import cn.com.warlock.filesystem.sdk.fdfs.exchange.Requestor;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;

import java.util.Collections;
import java.util.List;

import static cn.com.warlock.filesystem.sdk.fdfs.FastdfsConstants.*;
import static cn.com.warlock.filesystem.sdk.fdfs.FastdfsUtils.writeFixLength;

/**
 * @author siuming
 */
public abstract class FileIdOperationEncoder implements Requestor.Encoder {

    private final FileId fileId;

    /**
     * @param fileId
     */
    protected FileIdOperationEncoder(FileId fileId) {
        this.fileId = fileId;
    }

    @Override
    public List<Object> encode(ByteBufAllocator alloc) {
        byte cmd = cmd();
        int length = FDFS_GROUP_LEN + fileId.pathBytes().length;
        ByteBuf buf = alloc.buffer(length + FDFS_HEAD_LEN);
        buf.writeLong(length);
        buf.writeByte(cmd);
        buf.writeByte(ERRNO_OK);
        writeFixLength(buf, fileId.group(), FDFS_GROUP_LEN);
        ByteBufUtil.writeUtf8(buf, fileId.path());
        return Collections.singletonList(buf);
    }

    /**
     * @return
     */
    protected abstract byte cmd();
}
