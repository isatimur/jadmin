package ru.atc.jadmin.service.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.atc.jadmin.domain.Theme;
import ru.atc.jadmin.service.dto.ThemeAndFaqDTO;
import ru.atc.jadmin.service.dto.ThemeDTO;

/**
 * Mapper for the entity Theme and its DTO ThemeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ThemeMapper {

    ThemeDTO themeToThemeDTO(Theme theme);

    ThemeAndFaqDTO themeToThemeAndFaqDTO(Theme theme);

    List<ThemeDTO> themesToThemeDTOs(List<Theme> themes);

    List<ThemeAndFaqDTO> themesToThemeAndFaqDTOs(List<Theme> themes);

    @Mapping(target = "faqs", ignore = true)
    Theme themeDTOToTheme(ThemeDTO themeDTO);

    @Mapping(target = "faqs", ignore = true)
    Theme themeAndFaqDTOToTheme(ThemeAndFaqDTO themeAndFaqDTO);

    List<Theme> themeDTOsToThemes(List<ThemeDTO> themeDTOs);

    List<Theme> themeAndFaqDTOsToThemes(List<ThemeAndFaqDTO> themeDTOs);
}
