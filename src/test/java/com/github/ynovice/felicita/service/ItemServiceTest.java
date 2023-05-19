package com.github.ynovice.felicita.service;

import com.github.ynovice.felicita.Application;
import com.github.ynovice.felicita.model.dto.request.CreateItemRequestDto;
import com.github.ynovice.felicita.model.dto.request.CreateSizeQuantityRequestDto;
import com.github.ynovice.felicita.model.entity.Item;
import com.github.ynovice.felicita.model.entity.SizeQuantity;
import com.github.ynovice.felicita.repository.ItemRepository;
import com.github.ynovice.felicita.validator.ItemValidator;
import lombok.NonNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = Application.class
)
@TestPropertySource("/application-test.yml")
@AutoConfigureMockMvc
public class ItemServiceTest {

    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemValidator itemValidator;

    @Test
    public void createValidItemTest() {

        CreateItemRequestDto requestDto = validCreateItemRequestDto();
        Item item = itemService.createItem(requestDto);

        // id
        assertNotNull(item.getId());

        // name
        assertNotNull(item.getName());
        assertEquals(requestDto.getName(), item.getName());
        // description
        assertNotNull(item.getDescription());
        assertEquals(requestDto.getDescription(), item.getDescription());
        // price
        assertNotNull(item.getPrice());
        assertEquals(requestDto.getPrice(), item.getPrice());

        // price
        assertNotNull(item.getImages());
        assertEquals(requestDto.getImagesIds().size(), item.getImages().size());

        // categories
        assertNotNull(item.getCategories());
        assertEquals(requestDto.getCategoriesIds().size(), item.getCategories().size());

        // materials
        assertNotNull(item.getMaterials());
        assertEquals(requestDto.getMaterialsIds().size(), item.getMaterials().size());

        // colors
        assertNotNull(item.getColors());
        assertEquals(requestDto.getColorsIds().size(), item.getColors().size());

        // hasPrint
        assertEquals(requestDto.getHasPrint(), item.getHasPrint());

        // createdAt
        assertNotNull(item.getCreatedAt());

        // active
        assertEquals(shouldBeActive(requestDto), item.getActive());

        // sizesQuantities
        assertNotNull(item.getSizesQuantities());
        assertEquals(requestDto.getSizesQuantities().size(), item.getSizesQuantities().size());
        assertTrue(conform(requestDto.getSizesQuantities(), item.getSizesQuantities()));

        // cartEntries
        assertNotNull(item.getCartEntries());
        assertTrue(item.getCartEntries().isEmpty());

        // testing persistence
        Optional<Item> repositoryItemOptional = itemRepository.findById(item.getId());
        assertTrue(repositoryItemOptional.isPresent());
        assertEquals(item, repositoryItemOptional.get());

        // testing validation
        assertFalse(validate(item).hasErrors());
    }

    private boolean shouldBeActive(@NonNull CreateItemRequestDto createItemRequestDto) {

        List<CreateSizeQuantityRequestDto> sizesQuantitiesDtos = createItemRequestDto.getSizesQuantities();

        if(sizesQuantitiesDtos == null) return false;

        return sizesQuantitiesDtos
                .stream()
                .anyMatch(sqd -> sqd.getQuantity() > 0);
    }

    private boolean conform(List<CreateSizeQuantityRequestDto> dtos, List<SizeQuantity> entities) {

        return dtos
                .stream()
                .allMatch(dto -> entities
                        .stream()
                        .anyMatch(entity -> entity.getSizeId().equals(dto.getSizeId())
                                && entity.getQuantity().equals(dto.getQuantity())));
    }

    private CreateItemRequestDto validCreateItemRequestDto() {

        CreateItemRequestDto requestDto = new CreateItemRequestDto();

        requestDto.setName("Adidas Sneakers");
        requestDto.setDescription("Item description");
        requestDto.setImagesIds(Collections.emptyList());
        requestDto.setCategoriesIds(Collections.emptySet());

        requestDto.setMaterialsIds(new HashSet<>());
        requestDto.setColorsIds(new HashSet<>());
        requestDto.setHasPrint(false);

        requestDto.setPrice(5000);

        requestDto.setSizesQuantities(Collections.emptyList());

        return requestDto;
    }

    private BindingResult validate(@NonNull Item item) {
        DataBinder dataBinder = new DataBinder(item);
        dataBinder.addValidators(itemValidator);
        dataBinder.validate(new Object());
        return dataBinder.getBindingResult();
    }
}
