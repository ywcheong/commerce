package ywcheong.commerce.identity.domain

import jakarta.persistence.AttributeOverride
import jakarta.persistence.Column
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "identities")
class Identity(
    id: IdentityId,
) {
    @EmbeddedId
    @AttributeOverride(name = "value", column = Column(name = "id"))
    var id: IdentityId = id
        protected set

    companion object {
        fun create(): Identity = Identity(IdentityId.generate())
    }
}
