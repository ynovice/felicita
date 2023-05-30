package com.github.ynovice.felicita.validator;

import com.github.ynovice.felicita.model.entity.Article;
import com.github.ynovice.felicita.model.entity.Image;
import com.github.ynovice.felicita.repository.ImageRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class ArticleValidator  implements Validator {

    private final ImageRepository imageRepository;

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return Article.class.equals(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {

        Article article = (Article) target;

        validateName(article, errors);
        validateContent(article, errors);
        validatePreview(article, errors);
    }

    public void validateName(@NonNull Article article, @NonNull Errors errors) {

        String name = article.getName();

        if(!StringUtils.hasText(name)) {
            errors.rejectValue(
                    "name",
                    "article.name.empty",
                    "Название статьи не должно быть пустым"

            );
            return;
        }

        if(name.length() > 200) {
            errors.rejectValue(
                    "name",
                    "article.name.tooLong",
                    "Название статьи не должно быть болльше 50 000 символов"

            );
        }
    }

    public void validateContent(@NonNull Article article, @NonNull Errors errors) {

        String content = article.getContent();

        if(!StringUtils.hasText(content)) {
            errors.rejectValue(
                    "content",
                    "article.content.empty",
                    "Содержимое статьи не должно быть пустым"

            );
            return;
        }

        if(content.length() > 50000) {
            errors.rejectValue(
                    "content",
                    "article.content.tooLong",
                    "Содержимое статьи не должно быть болльше 50 000 символов"

            );
        }
    }

    public void validatePreview(@NonNull Article article, @NonNull Errors errors) {

        Image preview = article.getPreview();

        if(preview == null) return;

        if(!imageRepository.existsById(preview.getId())) {
            errors.rejectValue(
                    "preview",
                    "article.preview.dontExist",
                    "Произошла ошибка при загрузке превью статьи"
            );
        }
    }
}
