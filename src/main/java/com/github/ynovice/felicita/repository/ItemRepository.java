package com.github.ynovice.felicita.repository;

import com.github.ynovice.felicita.model.dto.request.ItemFilterParamsDto;
import com.github.ynovice.felicita.model.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("SELECT i FROM Item i " +
            "LEFT JOIN i.sizesQuantities sq " +
            "LEFT JOIN i.materials mrl " +
            "LEFT JOIN i.categories ctg " +
            "LEFT JOIN i.colors clr " +
            "WHERE (i.active = true) " +
            "AND (:#{#filterParams.filterByHasPrint} = false or i.hasPrint = :#{#filterParams.hasPrint}) " +
            "AND (:#{#filterParams.filterByPriceFrom} = false or i.price >= :#{#filterParams.priceFrom}) " +
            "AND (:#{#filterParams.filterByPriceTo} = false or i.price <= :#{#filterParams.priceTo}) " +
            "AND (:#{#filterParams.filterBySizes} = false or sq.size.id IN :#{#filterParams.sizesIds}) " +
            "AND (:#{#filterParams.filterByMaterials} = false or mrl.id IN :#{#filterParams.materialsIds}) " +
            "AND (:#{#filterParams.filterByCategories} = false or ctg.id IN :#{#filterParams.categoriesIds}) " +
            "AND (:#{#filterParams.filterByColors} = false or clr.id IN :#{#filterParams.colorsIds}) " +
            "ORDER BY i.createdAt")
    Page<Item> findAllByFilterParameters(ItemFilterParamsDto filterParams, Pageable pageable);
}
