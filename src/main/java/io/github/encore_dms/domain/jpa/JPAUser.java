package io.github.encore_dms.domain.jpa;

import javax.persistence.Entity;

@Entity(name = "User")
class JPAUser extends AbstractJPAEntity implements io.github.encore_dms.domain.User {

}
