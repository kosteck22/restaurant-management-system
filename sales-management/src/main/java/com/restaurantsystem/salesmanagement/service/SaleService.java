package com.restaurantsystem.salesmanagement.service;

import com.restaurantsystem.salesmanagement.entity.Sale;
import com.restaurantsystem.salesmanagement.entity.SoldItem;
import com.restaurantsystem.salesmanagement.service.dao.SaleRepository;
import com.restaurantsystem.salesmanagement.web.client.MenuClient;
import com.restaurantsystem.salesmanagement.web.dto.MenuItemDto;
import com.restaurantsystem.salesmanagement.web.dto.SaleRequest;
import com.restaurantsystem.salesmanagement.web.dto.SoldItemRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SaleService implements ISaleService {
    private final SaleRepository saleRepository;
    private final MenuClient menuClient;
    private final ISaleCreatorService saleCreatorService;
    private final KafkaTemplate<String, SaleCreatedEvent> kafkaTemplate;

    public SaleService(SaleRepository saleRepository, MenuClient menuClient, ISaleCreatorService saleCreatorService, KafkaTemplate<String, SaleCreatedEvent> kafkaTemplate) {
        this.saleRepository = saleRepository;
        this.menuClient = menuClient;
        this.saleCreatorService = saleCreatorService;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public String save(SaleRequest saleRequest) {
        List<MenuItemDto> menuItems = menuClient.getMenuItemsByIds(
                saleRequest.items()
                        .stream()
                        .map(SoldItemRequest::articleId)
                        .collect(Collectors.toList())
        ).getBody();

        Sale sale = saleCreatorService.create(saleRequest, menuItems);
        saleRepository.save(sale);

        SaleCreatedEvent event = new SaleCreatedEvent(
                sale.getId(), sale.getDate(), sale.getItems().stream()
                .collect(Collectors.toMap(SoldItem::getArticleId, SoldItem::getQuantity, Integer::sum))
        );

        Message<SaleCreatedEvent> message = MessageBuilder
                .withPayload(event)
                .setHeader(KafkaHeaders.TOPIC, "saleCreated")
                .build();

        kafkaTemplate.send(message);

        return sale.getId();
    }
}
