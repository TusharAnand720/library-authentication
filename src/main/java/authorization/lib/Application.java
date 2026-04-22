package authorization.lib;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);


		/*
		project structure:

		auth-lib/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── authorization/
│   │   │       └── lib/
│   │   │           ├── config/
│   │   │           │   ├── Algorithm.java
│   │   │           │   ├── AuthProperties.java
│   │   │           │   └── AuthAutoConfiguration.java
│   │   │           ├── exception/
│   │   │           │   ├── AuthException.java
│   │   │           │   ├── TokenExpiredException.java
│   │   │           │   ├── InvalidTokenException.java
│   │   │           │   └── SigningException.java
│   │   │           ├── model/
│   │   │           │   ├── JwtClaims.java
│   │   │           │   └── AuthToken.java
│   │   │           └── service/
│   │   │               ├── AuthService.java         ← the one interface
│   │   │               └── AuthServiceImpl.java     ← the one implementation
│   │   └── resources/
│   │       └── META-INF/
│   │           └── spring/
│   │               └── org.springframework.boot.autoconfigure.AutoConfiguration.imports
│   └── test/
│       └── java/
│           └── authorization/
│               └── lib/
│                   ├── service/
│                   │   └── AuthServiceImplTest.java
│                   └── config/
│                       └── AuthPropertiesTest.java
└── pom.xml
		 */
    }

}
