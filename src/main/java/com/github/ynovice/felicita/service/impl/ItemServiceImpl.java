package com.github.ynovice.felicita.service.impl;

import com.github.ynovice.felicita.model.Item;
import com.github.ynovice.felicita.model.Size;
import com.github.ynovice.felicita.model.SizeQuantity;
import com.github.ynovice.felicita.model.request.CreateItemRequestDto;
import com.github.ynovice.felicita.model.request.CreateSizeQuantityRequestDto;
import com.github.ynovice.felicita.repository.*;
import com.github.ynovice.felicita.service.ItemService;
import com.github.ynovice.felicita.validator.ItemValidator;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.web.server.ResponseStatusException;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemValidator itemValidator;

    private final ItemRepository itemRepository;

    private final ImageRepository imageRepository;
    private final CategoryRepository categoryRepository;
    private final MaterialRepository materialRepository;
    private final ColorRepository colorRepository;
    private final SizeRepository sizeRepository;

    @Override
    public Item createItem(CreateItemRequestDto requestDto) {

        Item item = new Item();

        item.setName(requestDto.getName());
        item.setDescription(requestDto.getDescription());

        if(requestDto.getImagesIds() != null) {
            item.setImages(
                    requestDto.getImagesIds()
                            .stream()
                            .map(imageRepository::getReferenceById)
                            .peek(image -> image.setItem(item))
                            .toList()
            );
        } else {
            item.setImages(Collections.emptyList());
        }


        if(requestDto.getCategoriesIds() != null) {
            item.setCategories(
                    requestDto.getCategoriesIds()
                            .stream()
                            .map(categoryRepository::getReferenceById)
                            .collect(Collectors.toSet())
            );
        } else {
            item.setCategories(Collections.emptySet());
        }

        if(requestDto.getMaterialsIds() != null) {
            item.setMaterials(
                    requestDto.getMaterialsIds()
                            .stream()
                            .map(materialRepository::getReferenceById)
                            .collect(Collectors.toSet())
            );
        } else {
            item.setMaterials(Collections.emptySet());
        }

        if(requestDto.getColorsIds() != null) {
            item.setColors(
                    requestDto.getColorsIds()
                            .stream()
                            .map(colorRepository::getReferenceById)
                            .collect(Collectors.toSet())
            );
        } else {
            item.setColors(Collections.emptySet());
        }

        item.setHasPrint(requestDto.getHasPrint());
        item.setPrice(requestDto.getPrice());

        if(requestDto.getSizesQuantities() != null) {

            List<SizeQuantity> sizesQuantities = new ArrayList<>();

            for(CreateSizeQuantityRequestDto sizeQuantityDto : requestDto.getSizesQuantities()) {
                Size size = sizeRepository.getReferenceById(sizeQuantityDto.getSizeId());
                Integer quantity = sizeQuantityDto.getQuantity();
                sizesQuantities.add(new SizeQuantity(size, quantity, item));
            }

            item.setSizesQuantities(sizesQuantities);

        } else {
            item.setSizesQuantities(Collections.emptyList());
        }

        item.setCartEntries(new HashSet<>());

        item.setCreatedAt(ZonedDateTime.now());
        item.setActive(shouldBeActive(item));

        BindingResult validationResult = validate(item);
        if(validationResult.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        itemRepository.save(item);

        return item;
    }

    public BindingResult validate(@NonNull Item item) {
        DataBinder dataBinder = new DataBinder(item);
        dataBinder.addValidators(itemValidator);
        dataBinder.validate();
        return dataBinder.getBindingResult();
    }

    private boolean shouldBeActive(@NonNull Item item) {
        return item.getSizesQuantities()
                .stream()
                .anyMatch(sizeQuantity -> sizeQuantity.getQuantity() > 0);
    }
}
