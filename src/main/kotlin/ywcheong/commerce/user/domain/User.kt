package ywcheong.commerce.user.domain

import jakarta.persistence.AttributeOverride
import jakarta.persistence.Column
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "users")
class User(
    id: UserId,
) {
    @EmbeddedId
    @AttributeOverride(name = "value", column = Column(name = "id"))
    var id: UserId = id
        protected set

    companion object {
        fun create(): User = User(UserId.generate())
    }
}
