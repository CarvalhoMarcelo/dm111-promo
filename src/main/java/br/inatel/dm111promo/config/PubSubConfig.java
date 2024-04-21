package br.inatel.dm111promo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.pubsub.v1.ProjectSubscriptionName;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class PubSubConfig {

    @Value("${dm111.project-id}")
    private String projectId;

    @Value("${dm111.pubsub.supermarketlist.subscription-name}")
    private String subscriptionName;

    // Need to set the GOOGLE_APPLICATION_CREDENTIALS pointing out
    // to the path of the credentials file

    @Bean
    public ProjectSubscriptionName subscriptionName() {
        return ProjectSubscriptionName.of(projectId, subscriptionName);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
