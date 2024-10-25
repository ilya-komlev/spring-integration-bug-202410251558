package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.file.FileHeaders;
import org.springframework.integration.file.dsl.Files;

import java.io.File;

@EnableIntegration
@SpringBootApplication
public class Main {

    static String dirPath;

    public static void main(String[] args) {
        // To demonstrate the bug the path must have some combination of '[', ']', '(', ')'.
        dirPath = args.length > 0 ? args[0] : "C:/[dir]";
        SpringApplication.run(Main.class, args);
    }

    @Bean
    IntegrationFlow flow() {
        File dir = new File(dirPath);
        return IntegrationFlow.from(Files.inboundAdapter(dir))
                // Expecting relative path, getting absolute path.
                .handle(msg -> System.out.println(msg.getHeaders().get(FileHeaders.RELATIVE_PATH)))
                .get();
    }
}