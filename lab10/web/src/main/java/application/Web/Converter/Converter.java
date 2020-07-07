package application.Web.Converter;

import application.Core.Domain.BaseEntity;
import application.Web.Dto.BaseDto;

public interface Converter<Model extends BaseEntity<Long>, Dto extends BaseDto> {

    Model convertDtoToModel(Dto dto);

    Dto convertModelToDto(Model model);

}

