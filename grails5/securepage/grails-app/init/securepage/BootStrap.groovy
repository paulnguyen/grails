package securepage

class BootStrap {

    def init = { servletContext ->
        def adminRole
        Role.withTransaction { rl ->
            adminRole = new Role(authority: 'ROLE_ADMIN').save(flush: true)
        }

        def testUser
        User.withTransaction { us ->
            testUser = new User(username: 'admin', password: 'password', fullname: 'Admin User', address: '1600 Amphitheatre Parkway, Mountain View, California, United States').save(flush: true)
        }

        UserRole.create testUser, adminRole

        UserRole.withTransaction { urole ->
            UserRole.withSession {
                it.flush()
                it.clear()
            }
        }
    }
    def destroy = {
    }
}