package com.github.ynovice.felicita.service.impl;

import com.github.ynovice.felicita.exception.InternalServerError;
import com.github.ynovice.felicita.exception.InvalidEntityException;
import com.github.ynovice.felicita.exception.NotFoundException;
import com.github.ynovice.felicita.model.dto.request.ModifyItemRequestDto;
import com.github.ynovice.felicita.model.dto.request.CreateSizeQuantityRequestDto;
import com.github.ynovice.felicita.model.dto.request.ItemFilterParamsDto;
import com.github.ynovice.felicita.model.entity.*;
import com.github.ynovice.felicita.repository.*;
import com.github.ynovice.felicita.service.ImageService;
import com.github.ynovice.felicita.service.ItemService;
import com.github.ynovice.felicita.validator.ItemValidator;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
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
    private final SizeQuantityRepository sizeQuantityRepository;

    @Override
    public Item createItem(@NonNull ModifyItemRequestDto requestDto) {

        Item item = new Item();

        item.setName(requestDto.getName());
        item.setDescription(requestDto.getDescription());

        item.setHasPrint(requestDto.getHasPrint());
        item.setPrice(requestDto.getPrice());

        bidirectionalAttachImagesToItem(requestDto.getImagesIds(), item);
        createAndLinkSizesQuantities(requestDto.getSizesQuantities(), item);

        item.setCategories(getEntitiesReferences(requestDto.getCategoriesIds(), categoryRepository));
        item.setMaterials(getEntitiesReferences(requestDto.getMaterialsIds(), materialRepository));
        item.setColors(getEntitiesReferences(requestDto.getColorsIds(), colorRepository));


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
    @Transactional
    public Item updateItem(Long id, @NonNull ModifyItemRequestDto requestDto) {



        return null;
    }

    @Override
    public void updateItemQuantitiesAfterReserve(@NonNull Reserve reserve) {

        for(ReserveEntry reserveEntry : reserve.getEntries()) {

            Item item = reserveEntry.getItem();

            for(SizeQuantity reserveEntrySQ : reserveEntry.getSizesQuantities()) {

                Size size = reserveEntrySQ.getSize();

                SizeQuantity itemSQ = item.getSizeQuantityBySize(size).orElseThrow(
                        () -> new InternalServerError("Произошла ошибка при попытке зарезервировать товар"));

                itemSQ.updateQuantity(-reserveEntrySQ.getQuantity());

                if(itemSQ.getQuantity() <= 0) {
                    item.getSizesQuantities().remove(itemSQ);
                    sizeQuantityRepository.delete(itemSQ);
                }
            }

            item.setActive(shouldBeActive(item));
            itemRepository.save(item);
        }

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

    private <T> List<T> getEntitiesReferences(Collection<Long> entitiesIds,
                                              @NonNull JpaRepository<T, Long> repository) {

        List<T> entitiesReferences = new ArrayList<>();

        if(entitiesIds == null) return entitiesReferences;

        for(Long id : entitiesIds)
            entitiesReferences.add(repository.getReferenceById(id));

        return entitiesReferences;
    }

    private void bidirectionalAttachImagesToItem(Collection<Long> imagesIds,
                                                 @NonNull Item item) {

        List<Image> imagesReferences = new ArrayList<>();

        if(imagesIds == null) {
            item.setImages(imagesReferences);
            return;
        }

        imagesIds.stream()
                .map(imageRepository::getReferenceById)
                .peek(imageRef -> imageRef.setItem(item))
                .forEach(imagesReferences::add);

        item.setImages(imagesReferences);
    }

    private void createAndLinkSizesQuantities(List<CreateSizeQuantityRequestDto> createSqDtos,
                                              @NonNull Item item) {

        List<SizeQuantity> createdSizesQuantities = new ArrayList<>();

        if(createSqDtos == null) {
            item.setSizesQuantities(createdSizesQuantities);
            return;
        }

        for(CreateSizeQuantityRequestDto createSqDto : createSqDtos) {
            Size size = sizeRepository.getReferenceById(createSqDto.getSizeId());
            Integer quantity = createSqDto.getQuantity();
            createdSizesQuantities.add(new SizeQuantity(size, quantity, item));
        }

        item.setSizesQuantities(createdSizesQuantities);
    }
}
