package application.Web.Converter;


import application.Core.Model.BaseEntity;
import application.Web.Dto.BaseDto;

public interface ConverterBaseEntity<Model extends BaseEntity<Long>, Dto extends BaseDto>
        extends Converter<Model, Dto> {

}

