package cn.com.warlock.filesystem.sdk.fdfs.codec;

import cn.com.warlock.filesystem.sdk.fdfs.FastdfsUtils;
import cn.com.warlock.filesystem.sdk.fdfs.FileInfo;
import cn.com.warlock.filesystem.sdk.fdfs.exchange.Replier;
import io.netty.buffer.ByteBuf;

/**
 * @author siuming
 */
public enum FileInfoDecoder implements Replier.Decoder<FileInfo> {

                                                                  INSTANCE;

    @Override
    public FileInfo decode(ByteBuf buf) {
        long fileSize = buf.readLong();
        long createTime = buf.readLong();
        long crc32 = buf.readLong();
        String address = FastdfsUtils.readString(buf, 16);
        return FileInfo.newBuilder().fileSize(fileSize).createTime(createTime).crc32(crc32)
            .address(address).build();
    }
}
