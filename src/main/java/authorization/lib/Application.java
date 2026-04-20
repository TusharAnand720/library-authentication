package authorization.lib;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);


		/*
		project stricture:

		auth-lib/
├── src/main/java/com/yourorg/auth/
│   ├── config/
│   │   ├── JwtConfig.java           # POJO — secret, algo, expiry, issuer
│   │   └── AuthAutoConfiguration.java  # Spring Boot @AutoConfiguration
│   ├── model/
│   │   ├── JwtClaims.java           # subject, roles, iat, exp
│   │   └── AuthToken.java           # wraps raw JWT string
│   ├── service/
│   │   ├── TokenService.java        # interface: generate()
│   │   ├── TokenValidator.java      # interface: validate(), isExpired()
│   │   └── impl/
│   │       └── JwtTokenServiceImpl.java  # JJWT-backed impl
│   ├── filter/
│   │   └── JwtAuthFilter.java       # OncePerRequestFilter
│   ├── context/
│   │   └── AuthContext.java         # ThreadLocal holder
│   ├── annotation/
│   │   └── AuthenticatedUser.java   # @AuthenticatedUser param annotation
│   ├── store/
│   │   ├── TokenStore.java          # V2 interface (revocation)
│   │   └── NoOpTokenStore.java      # V1 default impl
│   └── exception/
│       ├── AuthException.java
│       ├── TokenExpiredException.java
│       ├── InvalidTokenException.java
│       └── SigningException.java
├── src/main/resources/
│   └── META-INF/spring/
│       └── org.springframework.boot.autoconfigure.AutoConfiguration.imports
├── src/test/java/...
└── pom.xml  (or build.gradle)
		 */
	}

}
