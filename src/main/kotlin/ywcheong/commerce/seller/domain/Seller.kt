package ywcheong.commerce.seller.domain

import jakarta.persistence.AttributeOverride
import jakarta.persistence.Column
import jakarta.persistence.Embedded
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.Table
import ywcheong.commerce.identity.domain.IdentityId

@Entity
@Table(name = "sellers")
class Seller(
    id: SellerId,
    identityId: IdentityId,
) {
    @EmbeddedId
    @AttributeOverride(name = "value", column = Column(name = "id"))
    var id: SellerId = id
        protected set

    @Embedded
    @AttributeOverride(name = "value", column = Column(name = "identity_id"))
    var identityId: IdentityId = identityId
        protected set
}
