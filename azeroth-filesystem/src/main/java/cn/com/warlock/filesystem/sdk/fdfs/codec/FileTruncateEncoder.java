/**
 *
 */
package cn.com.warlock.filesystem.sdk.fdfs.codec;

import cn.com.warlock.filesystem.sdk.fdfs.FastdfsConstants;
import cn.com.warlock.filesystem.sdk.fdfs.FileId;
import cn.com.warlock.filesystem.sdk.fdfs.exchange.Requestor;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.util.Collections;
import java.util.List;

import static cn.com.warlock.filesystem.sdk.fdfs.FastdfsConstants.*;
import static io.netty.util.CharsetUtil.UTF_8;

/**
 * 截取请求
 *
 * @author liulongbiao
 */
public class FileTruncateEncoder implements Requestor.Encoder {

    private final FileId fileId;
    private final long truncatedSize;

    public FileTruncateEncoder(FileId fileId, long truncatedSize) {
        this.fileId = fileId;
        this.truncatedSize = truncatedSize;
    }

    @Override
    public List<Object> encode(ByteBufAllocator alloc) {
        byte[] pathBytes = fileId.path().getBytes(UTF_8);
        int length = 2 * FDFS_PROTO_PKG_LEN_SIZE + pathBytes.length;
        byte cmd = FastdfsConstants.Commands.FILE_TRUNCATE;

        ByteBuf buf = alloc.buffer(length + FDFS_HEAD_LEN);
        buf.writeLong(length);
        buf.writeByte(cmd);
        buf.writeByte(ERRNO_OK);
        buf.writeLong(pathBytes.length);
        buf.writeLong(truncatedSize);
        buf.writeBytes(pathBytes);
        return Collections.singletonList(buf);
    }

}
