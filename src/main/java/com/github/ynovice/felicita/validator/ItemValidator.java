package com.github.ynovice.felicita.validator;

import com.github.ynovice.felicita.model.entity.*;
import com.github.ynovice.felicita.repository.*;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ItemValidator implements Validator {

    private final ImageRepository imageRepository;
    private final CategoryRepository categoryRepository;
    private final MaterialRepository materialRepository;
    private final ColorRepository colorRepository;
    private final SizeRepository sizeRepository;

    @Override
    public boolean supports(@NonNull Class<?> targetObjectClass) {
        return targetObjectClass.equals(Item.class);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {

        Item item = (Item) target;

        validateName(item, errors);
        validateDescription(item, errors);
        validateImages(item, errors);
        validateCategories(item, errors);
        validateMaterials(item ,errors);
        validateColors(item, errors);
        validateHasPrint(item, errors);
        validateCreatedAt(item, errors);
        validatePrice(item, errors);
        validateSizesQuantities(item, errors);
        validateActive(item, errors);
    }

    public void validateName(@NonNull Item item, @NonNull Errors errors) {

        if(!StringUtils.hasText(item.getName())) {
            errors.rejectValue(
                    "name",
                    "item.name.empty",
                    "Имя товара не может быть пустым"
            );
            return;
        }

        if(item.getName().length() > 100) {
            errors.rejectValue(
                    "name",
                    "item.name.tooLong",
                    "Имя товара должно быть не длиньше 100 символов"
            );
        }
    }

    public void validateDescription(@NonNull Item item, @NonNull Errors errors) {

        String description = item.getDescription();

        if(description == null) {
            errors.rejectValue(
                    "description",
                    "item.description.null",
                    "Описание товара имеет недопустимое значение, свяжитесь с разработчиком"
            );
            return;
        }

        if(description.length() > 0 && description.trim().length() == 0) {
            errors.rejectValue(
                    "description",
                    "item.description.empty",
                    "Описание товара не может состоять только из пробелов"
            );
            return;
        }

        if(description.length() > 1000) {
            errors.rejectValue(
                    "description",
                    "item.description.tooLong",
                    "Описание товара должно быть не длиньше 1000 символов"
            );
        }
    }

    public void validateImages(@NonNull Item item, @NonNull Errors errors) {

        List<Image> images = item.getImages();

        if(images == null) {
            errors.rejectValue(
                    "images",
                    "item.images.null",
                    "Произошла ошибка при привязке изображений к товару, свяжитесь с разработчиком"
            );
            return;
        }

        images
                .stream()
                .filter(image -> !imageRepository.existsById(image.getId()))
                .findAny()
                .ifPresent(value -> errors.rejectValue(
                        "images",
                        "item.images.dontExist",
                        "Произошла ошибка при загрузке одного или нескольких изображений"
                ));

        for (int i = 0; i < images.size() - 1; i++) {
            for (int j = i + 1; j < images.size(); j++) {
                if (images.get(i).getId().equals(images.get(j).getId())) {
                    errors.rejectValue(
                            "images",
                            "item.images.duplicated",
                            "Одна и та же фотография указана несколько раз"
                    );
                    return;
                }
            }
        }
    }

    public void validateCategories(@NonNull Item item, @NonNull Errors errors) {

        List<Category> categories = item.getCategories();

        if(categories == null) {
            errors.rejectValue(
                    "categories",
                    "item.categories.null",
                    "Список категорий товара имеет недопустимое значение, свяжитесь с разработчиком"
            );
            return;
        }

        categories
                .stream()
                .filter(category -> !categoryRepository.existsById(category.getId()))
                .findAny()
                .ifPresent(value -> errors.rejectValue(
                        "categories",
                        "item.categories.dontExist",
                        "Среди выбранных категорий есть одна или несколько несуществующих категорий"
                ));

        for (int i = 0; i < categories.size() - 1; i++) {
            for (int j = i + 1; j < categories.size(); j++) {
                if (categories.get(i).getId().equals(categories.get(j).getId())) {
                    errors.rejectValue(
                            "categories",
                            "item.categories.duplicated",
                            "Одна и та же категория указана несколько раз"
                    );
                    return;
                }
            }
        }

        for(Category category : categories) {

            Optional<Long> parentId = categoryRepository.findParentIdById(category.getId());

            while(parentId.isPresent()) {

                Optional<Long> finalParentId = parentId;
                boolean bothParentAndSubcategoryArePresent = categories
                        .stream()
                        .anyMatch(c -> finalParentId.get().equals(c.getId()));

                if(bothParentAndSubcategoryArePresent) {
                    errors.rejectValue(
                            "categories",
                            "item.categories.duplicated",
                            "Вы не можете указать одновременно и родительскую, " +
                                    "и дочернюю категорию для товара"
                    );
                    return;
                }

                parentId = categoryRepository.findParentIdById(parentId.get());
            }
        }
    }

    public void validateMaterials(@NonNull Item item, @NonNull Errors errors) {

        List<Material> materials = item.getMaterials();

        if(materials == null) {
            errors.rejectValue(
                    "materials",
                    "item.materials.null",
                    "Список материалов товара имеет недопустимое значение, свяжитесь с разработчиком"
            );
            return;
        }

        materials
                .stream()
                .filter(material -> !materialRepository.existsById(material.getId()))
                .findAny()
                .ifPresent(value -> errors.rejectValue(
                        "materials",
                        "item.materials.dontExist",
                        "Среди выбранных материалов есть несуществующие материалы"
                ));

        for (int i = 0; i < materials.size() - 1; i++) {
            for (int j = i + 1; j < materials.size(); j++) {
                if (materials.get(i).getId().equals(materials.get(j).getId())) {
                    errors.rejectValue(
                            "materials",
                            "item.materials.duplicated",
                            "Один и тот же материал указан несколько раз"
                    );
                    return;
                }
            }
        }
    }

    public void validateColors(@NonNull Item item, @NonNull Errors errors) {

        List<Color> colors = item.getColors();

        if(colors == null) {
            errors.rejectValue(
                    "colors",
                    "item.colors.null",
                    "Список цветов товара имеет недопустимое значение, свяжитесь с разработчиком"
            );
            return;
        }

        colors
                .stream()
                .filter(color -> !colorRepository.existsById(color.getId()))
                .findAny()
                .ifPresent(value -> errors.rejectValue(
                        "colors",
                        "item.colors.dontExist",
                        "Среди выбранных цветов есть несуществующие цвета"
                ));

        for (int i = 0; i < colors.size() - 1; i++) {
            for (int j = i + 1; j < colors.size(); j++) {
                if (colors.get(i).getId().equals(colors.get(j).getId())) {
                    errors.rejectValue(
                            "colors",
                            "item.colors.duplicated",
                            "Один и тот же цвет указан несколько раз"
                    );
                    return;
                }
            }
        }
    }

    public void validateHasPrint(@NonNull Item item, @NonNull Errors errors) {

        if(item.getHasPrint() == null)
            errors.rejectValue(
                    "hasPrint",
                    "item.hasPrint.null",
                    "Поле hasPrint имеет недопустимое значение, свяжитесь с разработчиком"
            );
    }

    public void validateCreatedAt(@NonNull Item item, @NonNull Errors errors) {

        if(item.getCreatedAt() == null)
            errors.rejectValue(
                    "createdAt",
                    "item.createdAt.null",
                    "Дата создания товара имеет недопустимое значение, свяжитесь с разработчиком"
            );
    }

    public void validatePrice(@NonNull Item item, @NonNull Errors errors) {

        if(item.getPrice() == null) {
            errors.rejectValue(
                    "price",
                    "item.price.null",
                    "Укажите стоимость товара"
            );
            return;
        }

        if(item.getPrice() <= 0) {
            errors.rejectValue(
                    "price",
                    "price.notPositive",
                    "Цена товара должна быть больше 0"
            );
        }
    }

    public void validateSizesQuantities(@NonNull Item item, @NonNull Errors errors) {

        List<SizeQuantity> sizesQuantities = item.getSizesQuantities();

        if(sizesQuantities == null) {
            errors.rejectValue(
                    "sizesQuantities",
                    "item.sizesQuantities.null",
                    "Произошла ошибка при сохранении информации о" +
                            " количестве товара, свяжитесь с разработчиком"
            );
            return;
        }

        for(SizeQuantity sizeQuantity : sizesQuantities) {

            if(sizeQuantity.getQuantity() == null || sizeQuantity.getQuantity() <= 0) {
                errors.rejectValue(
                        "sizesQuantities",
                        "item.sizesQuantities.invalidQuantity",
                        "Неверно указано количество товара для одного из размеров"
                );
                return;
            }

            if(!sizeRepository.existsById(sizeQuantity.getSizeId())) {
                errors.rejectValue(
                        "sizesQuantities",
                        "item.sizesQuantities.invalidSize",
                        "Для товара указан несуществующий размер"
                );
                return;
            }
        }

        for (int i = 0; i < sizesQuantities.size() - 1; i++) {
            for (int j = i + 1; j < sizesQuantities.size(); j++) {

                if(sizesQuantities.get(i).getSizeId()
                        .equals(sizesQuantities.get(j).getSizeId())) {
                    errors.rejectValue(
                            "sizesQuantities",
                            "item.sizesQuantities.duplicated",
                            "Один и тот же размер товара указан несколько раз"
                    );
                    return;
                }
            }
        }
    }

    public void validateActive(@NonNull Item item, @NonNull Errors errors) {

        if(item.getActive() == null) {
            errors.rejectValue(
                    "active",
                    "item.active.null",
                    "Поле active имеет недопустимое значение, свяжитесь с разработчиком"
            );
            return;
        }

        if(!item.getActive().equals(shouldBeActive(item))) {
            errors.rejectValue(
                    "active",
                    "item.active.invalid",
                    "Произошла ошибка при вычислении значения active, свяжитесь с разработчиком"
            );
        }
    }

    private boolean shouldBeActive(@NonNull Item item) {

        List<SizeQuantity> sizesQuantities = item.getSizesQuantities();

        return sizesQuantities
                .stream()
                .anyMatch(sq -> sq.getQuantity() > 0);
    }
}
