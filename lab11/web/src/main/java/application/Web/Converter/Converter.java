package application.Web.Converter;

import application.Core.Model.BaseEntity;
import application.Web.Dto.BaseDto;

public interface Converter<Model, Dto> {

    Model convertDtoToModel(Dto dto);
    Dto convertModelToDto(Model model);

}

