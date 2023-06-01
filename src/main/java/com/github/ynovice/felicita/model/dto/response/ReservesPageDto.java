package com.github.ynovice.felicita.model.dto.response;

import com.github.ynovice.felicita.model.dto.entity.PaginationMeta;
import com.github.ynovice.felicita.model.dto.entity.ReserveShortInfoDto;
import com.github.ynovice.felicita.model.entity.Reserve;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
public class ReservesPageDto {

    private List<ReserveShortInfoDto> reserves;
    private PaginationMeta paginationMeta;

    public static ReservesPageDto fromEntity(Page<Reserve> reservesPage) {

        ReservesPageDto dto = new ReservesPageDto();
        dto.setReserves(reservesPage.stream().map(ReserveShortInfoDto::fromEntity).toList());
        dto.setPaginationMeta(PaginationMeta.fromEntity(reservesPage));

        return dto;
    }
}
