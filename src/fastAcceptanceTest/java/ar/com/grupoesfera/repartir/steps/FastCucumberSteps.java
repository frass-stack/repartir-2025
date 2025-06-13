package ar.com.grupoesfera.repartir.steps;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

@CucumberContextConfiguration
@SpringBootTest
@ActiveProfiles("fastTest")
// Escanea todo tu paquete base para encontrar @Service, @Repository, @Component, etc.
@ComponentScan("ar.com.grupoesfera.repartir")
public class FastCucumberSteps {
    
    // ... tus @Autowired existentes ...
}
