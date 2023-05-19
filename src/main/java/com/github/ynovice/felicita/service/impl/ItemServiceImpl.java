package com.github.ynovice.felicita.service.impl;

import com.github.ynovice.felicita.exception.InvalidEntityException;
import com.github.ynovice.felicita.exception.NotFoundException;
import com.github.ynovice.felicita.model.dto.request.ItemFilterParamsDto;
import com.github.ynovice.felicita.model.entity.Item;
import com.github.ynovice.felicita.model.entity.Size;
import com.github.ynovice.felicita.model.entity.SizeQuantity;
import com.github.ynovice.felicita.model.dto.request.CreateItemRequestDto;
import com.github.ynovice.felicita.model.dto.request.CreateSizeQuantityRequestDto;
import com.github.ynovice.felicita.repository.*;
import com.github.ynovice.felicita.service.ImageService;
import com.github.ynovice.felicita.service.ItemService;
import com.github.ynovice.felicita.validator.ItemValidator;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;

import java.time.ZonedDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final static int ITEMS_PER_PAGE = 20;

    private final ItemValidator itemValidator;

    private final ImageService imageService;

    private final ItemRepository itemRepository;

    private final ImageRepository imageRepository;
    private final CategoryRepository categoryRepository;
    private final MaterialRepository materialRepository;
    private final ColorRepository colorRepository;
    private final SizeRepository sizeRepository;

    @Override
    public Item createItem(@NonNull CreateItemRequestDto requestDto) {

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
                            .toList()
            );
        } else {
            item.setCategories(Collections.emptyList());
        }

        if(requestDto.getMaterialsIds() != null) {
            item.setMaterials(
                    requestDto.getMaterialsIds()
                            .stream()
                            .map(materialRepository::getReferenceById)
                            .toList()
            );
        } else {
            item.setMaterials(Collections.emptyList());
        }

        if(requestDto.getColorsIds() != null) {
            item.setColors(
                    requestDto.getColorsIds()
                            .stream()
                            .map(colorRepository::getReferenceById)
                            .toList()
            );
        } else {
            item.setColors(Collections.emptyList());
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
            throw new InvalidEntityException(validationResult);
        }

        itemRepository.save(item);
        imageRepository.saveAll(item.getImages());

        return item;
    }

    @Override
    public Optional<Item> getById(@NonNull Long id) {
        return itemRepository.findById(id);
    }

    @Override
    public Page<Item> getByFilters(int page, ItemFilterParamsDto filterParams) {
        return itemRepository.findAllByFilterParameters(
                filterParams,
                PageRequest.of(page, ITEMS_PER_PAGE)
        );
    }

    @Override
    public void deleteById(@NonNull Long id) {

        Item item = itemRepository.findById(id).orElseThrow(NotFoundException::new);

        itemRepository.delete(item);
        item.getImages().forEach(imageService::delete);
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
