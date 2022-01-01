
## Resources

* https://www.djamware.com/post/5ef41459a3120f3df580ec52/grails-4-and-spring-security-custom-user-details-example
* https://grails-plugins.github.io/grails-spring-security-core/
* https://github.com/grails-plugins/grails-spring-security-core
* https://github.com/didinj/grails-4-spring-security-custom-user-details
		         
    
## Grails Secured Page Example Commands

        grails create-app securepage
        grails create-domain-class User
        grails create-domain-class Role
        grails create-domain-class UserRole
        grails create-service CustomUserDetails
        grails create-controller Dashboard


## File: build.gradle

```
dependencies {

 implementation 'org.grails.plugins:spring-security-core:4.0.3'
 
}
```


## User.groovy (Domain Class)

```
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import grails.compiler.GrailsCompileStatic

@GrailsCompileStatic
@EqualsAndHashCode(includes='username')
@ToString(includes='username', includeNames=true, includePackage=false)
class User implements Serializable {

    private static final long serialVersionUID = 1

    String username
    String password
    boolean enabled = true
    boolean accountExpired
    boolean accountLocked
    boolean passwordExpired
    String fullname
    String address

    Set<Role> getAuthorities() {
        (UserRole.findAllByUser(this) as List<UserRole>)*.role as Set<Role>
    }

    static constraints = {
        password nullable: false, blank: false, password: true
        username nullable: false, blank: false, unique: true
        fullname nullable: false, blank: false
        address nullable: false, blank: false
    }

    static mapping = {
            password column: '`password`'
    }
}
```


## Role.groovy (Domain Class)

```
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import grails.compiler.GrailsCompileStatic

@GrailsCompileStatic
@EqualsAndHashCode(includes='authority')
@ToString(includes='authority', includeNames=true, includePackage=false)
class Role implements Serializable {

        private static final long serialVersionUID = 1

        String authority

        static constraints = {
                authority nullable: false, blank: false, unique: true
        }

        static mapping = {
                cache true
        }
}
```


## UserRole.groovy (Domain Class)

```
import grails.gorm.DetachedCriteria
import groovy.transform.ToString

import org.codehaus.groovy.util.HashCodeHelper
import grails.compiler.GrailsCompileStatic

@GrailsCompileStatic
@ToString(cache=true, includeNames=true, includePackage=false)
class UserRole implements Serializable {

        private static final long serialVersionUID = 1

        User user
        Role role

        @Override
        boolean equals(other) {
                if (other instanceof UserRole) {
                        other.userId == user?.id && other.roleId == role?.id
                }
        }

    @Override
        int hashCode() {
            int hashCode = HashCodeHelper.initHash()
        if (user) {
            hashCode = HashCodeHelper.updateHash(hashCode, user.id)
                }
                if (role) {
                    hashCode = HashCodeHelper.updateHash(hashCode, role.id)
                }
                hashCode
        }

        static UserRole get(long userId, long roleId) {
                criteriaFor(userId, roleId).get()
        }

        static boolean exists(long userId, long roleId) {
                criteriaFor(userId, roleId).count()
        }

        private static DetachedCriteria criteriaFor(long userId, long roleId) {
                UserRole.where {
                        user == User.load(userId) &&
                        role == Role.load(roleId)
                }
        }

        static UserRole create(User user, Role role, boolean flush = false) {
                def instance = new UserRole(user: user, role: role)
                instance.save(flush: flush)
                instance
        }

        static boolean remove(User u, Role r) {
                if (u != null && r != null) {
                        UserRole.where { user == u && role == r }.deleteAll()
                }
        }

        static int removeAll(User u) {
                u == null ? 0 : UserRole.where { user == u }.deleteAll() as int
        }

        static int removeAll(Role r) {
                r == null ? 0 : UserRole.where { role == r }.deleteAll() as int
        }

        static constraints = {
            user nullable: false
                role nullable: false, validator: { Role r, UserRole ur ->
                        if (ur.user?.id) {
                                if (UserRole.exists(ur.user.id, r.id)) {
                                    return ['userRole.exists']
                                }
                        }
                }
        }

        static mapping = {
                id composite: ['user', 'role']
                version false
        }
}
```


## File: grails-app/conf/application.groovy

```
// Added by the Spring Security Core plugin:
grails.plugin.springsecurity.userLookup.userDomainClassName = 'securepage.User'
grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'securepage.UserRole'
grails.plugin.springsecurity.authority.className = 'securepage.Role'
grails.plugin.springsecurity.controllerAnnotations.staticRules = [
        [pattern: '/',               access: ['permitAll']],
        [pattern: '/error',          access: ['permitAll']],
        [pattern: '/index',          access: ['permitAll']],
        [pattern: '/index.gsp',      access: ['permitAll']],
        [pattern: '/shutdown',       access: ['permitAll']],
        [pattern: '/assets/**',      access: ['permitAll']],
        [pattern: '/**/js/**',       access: ['permitAll']],
        [pattern: '/**/css/**',      access: ['permitAll']],
        [pattern: '/**/images/**',   access: ['permitAll']],
        [pattern: '/**/favicon.ico', access: ['permitAll']]
]

grails.plugin.springsecurity.filterChain.chainMap = [
        [pattern: '/assets/**',      filters: 'none'],
        [pattern: '/**/js/**',       filters: 'none'],
        [pattern: '/**/css/**',      filters: 'none'],
        [pattern: '/**/images/**',   filters: 'none'],
        [pattern: '/**/favicon.ico', filters: 'none'],
        [pattern: '/**',             filters: 'JOINED_FILTERS']
]

grails.plugin.springsecurity.logout.postOnly = false
```
     

## File: src/main/groovy/securepage/CustomUserDetails.groovy

```
package securepage

import grails.plugin.springsecurity.userdetails.GrailsUser
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User

class CustomUserDetails extends GrailsUser {

    final String fullname
    final String address

    CustomUserDetails(String username, String password, boolean enabled,
                    boolean accountNonExpired, boolean credentialsNonExpired,
                    boolean accountNonLocked,
                    Collection<GrantedAuthority> authorities,
                    long id, String fullname, String address) {
        super(username, password, enabled, accountNonExpired,
                credentialsNonExpired, accountNonLocked, authorities, id)

        this.fullname = fullname
        this.address= address
    }
}
```


## CustomUserDetailsService.groovy (Grails Service)

```
import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.userdetails.GrailsUserDetailsService
import grails.plugin.springsecurity.userdetails.NoStackUsernameNotFoundException
import grails.gorm.transactions.Transactional
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException

class CustomUserDetailsService implements GrailsUserDetailsService {

    static final List NO_ROLES = [new SimpleGrantedAuthority(SpringSecurityUtils.NO_ROLE)]

    UserDetails loadUserByUsername(String username, boolean loadRoles)
            throws UsernameNotFoundException {
        return loadUserByUsername(username)
    }

    @Transactional(readOnly=true, noRollbackFor=[IllegalArgumentException, UsernameNotFoundException])
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = User.findByUsername(username)
        if (!user) throw new NoStackUsernameNotFoundException()

        def roles = user.authorities

        def authorities = roles.collect {
            new SimpleGrantedAuthority(it.authority)
        }

        return new CustomUserDetails(user.username, user.password, user.enabled,
            !user.accountExpired, !user.passwordExpired,
            !user.accountLocked, authorities ?: NO_ROLES, user.id,
            user.fullname, user.address)
    }
}
```


## File: src/main/groovy/securepage/UserPasswordEncoderListener.groovy

```
package securepage

import grails.plugin.springsecurity.SpringSecurityService
import org.grails.datastore.mapping.engine.event.AbstractPersistenceEvent
import org.grails.datastore.mapping.engine.event.PreInsertEvent
import org.grails.datastore.mapping.engine.event.PreUpdateEvent
import org.springframework.beans.factory.annotation.Autowired
import grails.events.annotation.gorm.Listener
import groovy.transform.CompileStatic

@CompileStatic
class UserPasswordEncoderListener {

    @Autowired
    SpringSecurityService springSecurityService

    @Listener(User)
    void onPreInsertEvent(PreInsertEvent event) {
        encodePasswordForEvent(event)
    }

    @Listener(User)
    void onPreUpdateEvent(PreUpdateEvent event) {
        encodePasswordForEvent(event)
    }

    private void encodePasswordForEvent(AbstractPersistenceEvent event) {
        if (event.entityObject instanceof User) {
            User u = event.entityObject as User
            if (u.password && ((event instanceof  PreInsertEvent) || (event instanceof PreUpdateEvent && u.isDirty('password')))) {
                event.getEntityAccess().setProperty('password', encodePassword(u.password))
            }
        }
    }

    private String encodePassword(String password) {
        springSecurityService?.passwordEncoder ? springSecurityService.encodePassword(password) : password
    }
}
```


## Inject CustomUserDetails Service into File: grails-app/conf/spring/resources.groovy

```
import securepage.UserPasswordEncoderListener
import securepage.CustomUserDetailsService

// Place your Spring DSL code here
beans = {
    userPasswordEncoderListener(UserPasswordEncoderListener)
    userDetailsService(CustomUserDetailsService)
}
```


## DashboardController.groovy (Secured Page Controller)

```
import grails.plugin.springsecurity.annotation.Secured

@Secured('ROLE_ADMIN')
class DashboardController {

    def index() { }
}
```


## dashboard/index.gsp (Secured Page View)

```
<%@ page contentType="text/html;charset=UTF-8" %>

<!doctype html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Secure Dashboard</title>
</head>
<body>
<div id="content" role="main">
    <section class="row colset-2-its">
        <sec:ifLoggedIn>
            <h1>Welcome to the secure dashboard <sec:loggedInUserInfo field='fullname'/>!</h1>
            <p><sec:loggedInUserInfo field='address'/></p>
            <h2><g:link controller="logout">Logout</g:link></h2>
        </sec:ifLoggedIn>
    </section>
</div>
</body>
</html>
```


## File:  grails-app/init/securepage/BootStrap.groovy

```
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
```

