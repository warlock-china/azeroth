package cn.com.warlock.emitter.zk;

import java.util.function.Supplier;

public class ResourceClaimHolder {
    private ResourceClaim claim;
    private final Supplier<ResourceClaim> freshClaimSupplier;
    private final int clusterId;

    public ResourceClaimHolder(Supplier<ResourceClaim> freshClaimSupplier, int clusterId) {
        this.freshClaimSupplier = freshClaimSupplier;
        this.clusterId = clusterId;
    }

    public int getResourceId() {
        try {
            return claim.get();
        } catch (IllegalStateException e) {
            claim.close();
            claim = freshClaimSupplier.get();
            return claim.get();
        }
    }

    public int getClusterId() {
        return clusterId;
    }
}
