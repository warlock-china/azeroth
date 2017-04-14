package cn.com.warlock.emitter.zk;

import java.io.IOException;
import java.time.Duration;
import java.util.function.Supplier;

import cn.com.warlock.emitter.GeneratorException;
import cn.com.warlock.emitter.GeneratorIdentityHolder;
import cn.com.warlock.emitter.bytes.Blueprint;
import cn.com.warlock.emitter.zk.connection.ZooKeeperConnection;

public class SynchronizedGeneratorIdentity implements GeneratorIdentityHolder {
    final int                 clusterId;
    final Supplier<Duration>  claimDurationSupplier;
    final String              zNode;
    final ZooKeeperConnection zooKeeperConnection;

    ResourceClaim             resourceClaim = null;

    public SynchronizedGeneratorIdentity(ZooKeeperConnection zooKeeperConnection, String zNode,
                                         int clusterId, Supplier<Duration> claimDurationSupplier) {
        this.zooKeeperConnection = zooKeeperConnection;
        this.zNode = zNode;
        this.clusterId = clusterId;
        this.claimDurationSupplier = claimDurationSupplier;
    }

    public static SynchronizedGeneratorIdentity basedOn(String quorum, String znode,
                                                        Supplier<Duration> claimDurationSupplier) throws IOException {
        ZooKeeperConnection zooKeeperConnection = new ZooKeeperConnection(quorum);
        int clusterId = ClusterID.get(zooKeeperConnection.get(), znode);

        return new SynchronizedGeneratorIdentity(zooKeeperConnection, znode, clusterId,
            claimDurationSupplier);
    }

    public static SynchronizedGeneratorIdentity basedOn(String quorum, String znode,
                                                        Long claimDuration) throws IOException {
        ZooKeeperConnection zooKeeperConnection = new ZooKeeperConnection(quorum);
        int clusterId = ClusterID.get(zooKeeperConnection.get(), znode);
        Supplier<Duration> durationSupplier = () -> Duration.ofMillis(claimDuration);

        return new SynchronizedGeneratorIdentity(zooKeeperConnection, znode, clusterId,
            durationSupplier);
    }

    @Override
    public int getClusterId() throws GeneratorException {
        return clusterId;
    }

    @Override
    public int getGeneratorId() throws GeneratorException {
        if (resourceClaim == null) {
            resourceClaim = acquireResourceClaim();
        }

        try {
            return resourceClaim.get();
        } catch (IllegalStateException e) {
            resourceClaim.close();
            resourceClaim = acquireResourceClaim();
            return resourceClaim.get();
        }
    }

    public String getZNode() {
        return zNode;
    }

    public void relinquishGeneratorIdClaim() {
        resourceClaim.close();
        resourceClaim = null;
    }

    private ResourceClaim acquireResourceClaim() throws GeneratorException {
        Long claimDuration = getDurationInMillis(claimDurationSupplier);
        try {
            return ExpiringResourceClaim.claimExpiring(zooKeeperConnection,
                Blueprint.MAX_GENERATOR_ID + 1, zNode, claimDuration);
        } catch (IOException e) {
            throw new GeneratorException(e);
        }
    }

    @Override
    public void close() throws IOException {
        if (resourceClaim != null) {
            resourceClaim.close();
        }
    }

    static Long getDurationInMillis(Supplier<Duration> durationSupplier) {
        if (durationSupplier == null)
            return null;
        Duration duration = durationSupplier.get();
        if (duration == null)
            return null;
        return duration.toMillis();
    }
}
