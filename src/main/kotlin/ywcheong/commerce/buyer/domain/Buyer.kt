package ywcheong.commerce.buyer.domain

import jakarta.persistence.AttributeOverride
import jakarta.persistence.Column
import jakarta.persistence.Embedded
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.Table
import ywcheong.commerce.identity.domain.IdentityId

@Entity
@Table(name = "buyers")
class Buyer(
    id: BuyerId,
    identityId: IdentityId,
) {
    @EmbeddedId
    @AttributeOverride(name = "value", column = Column(name = "id"))
    var id: BuyerId = id
        protected set

    @Embedded
    @AttributeOverride(name = "value", column = Column(name = "identity_id"))
    var identityId: IdentityId = identityId
        protected set

    companion object {
        fun register(identityId: IdentityId): Buyer = Buyer(BuyerId.generate(), identityId)
    }
}
