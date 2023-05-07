package com.github.ynovice.felicita.validator;

import com.github.ynovice.felicita.model.Item;
import com.github.ynovice.felicita.model.SizeQuantity;
import com.github.ynovice.felicita.repository.*;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;

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
                    "Описание товара должно быть не длиньше 100 символов"
            );
        }
    }

    public void validateImages(@NonNull Item item, @NonNull Errors errors) {

        if(item.getImages() == null) {
            errors.rejectValue(
                    "images",
                    "item.images.null",
                    "Произошла ошибка при привязке изображений к товару, свяжитесь с разработчиком"
            );
            return;
        }

        item.getImages()
                .stream()
                .filter(image -> !imageRepository.existsById(image.getId()))
                .findAny()
                .ifPresent(value -> errors.rejectValue(
                        "images",
                        "item.images.dontExist",
                        "Произошла ошибка при загрузке одного или нескольких изображений"
                ));
    }

    public void validateCategories(@NonNull Item item, @NonNull Errors errors) {

        if(item.getCategories() == null) {
            errors.rejectValue(
                    "categories",
                    "item.categories.null",
                    "Список категорий товара имеет недопустимое значение, свяжитесь с разработчиком"
            );
            return;
        }

        item.getCategories()
                .stream()
                .filter(category -> !categoryRepository.existsById(category.getId()))
                .findAny()
                .ifPresent(value -> errors.rejectValue(
                        "categories",
                        "item.categories.dontExist",
                        "Для товара указана несуществующая категория"
                ));
    }

    public void validateMaterials(@NonNull Item item, @NonNull Errors errors) {

        if(item.getMaterials() == null) {
            errors.rejectValue(
                    "materials",
                    "item.materials.null",
                    "Список материалов товара имеет недопустимое значение, свяжитесь с разработчиком"
            );
            return;
        }

        item.getMaterials()
                .stream()
                .filter(material -> !materialRepository.existsById(material.getId()))
                .findAny()
                .ifPresent(value -> errors.rejectValue(
                        "materials",
                        "item.materials.dontExist",
                        "Среди выбранных материалов есть несуществующие материалы"
                ));
    }

    public void validateColors(@NonNull Item item, @NonNull Errors errors) {

        if(item.getColors() == null) {
            errors.rejectValue(
                    "colors",
                    "item.colors.null",
                    "Список цветов товара имеет недопустимое значение, свяжитесь с разработчиком"
            );
            return;
        }

        item.getColors()
                .stream()
                .filter(color -> !colorRepository.existsById(color.getId()))
                .findAny()
                .ifPresent(value -> errors.rejectValue(
                        "colors",
                        "item.colors.dontExist",
                        "Среди выбранных цветов есть несуществующие цвета"
                ));
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

        if(item.getSizesQuantities() == null) {
            errors.rejectValue(
                    "sizesQuantities",
                    "item.sizesQuantities.null",
                    "Произошла ошибка при сохранении информации о" +
                            " количестве товара, свяжитесь с разработчиком"
            );
            return;
        }

        for(SizeQuantity sizeQuantity : item.getSizesQuantities()) {

            if(sizeQuantity.getQuantity() == null || sizeQuantity.getQuantity() < 0) {
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
