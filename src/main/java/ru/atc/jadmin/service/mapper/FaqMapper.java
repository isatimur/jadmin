package ru.atc.jadmin.service.mapper;

import ru.atc.jadmin.domain.*;
import ru.atc.jadmin.service.dto.FaqDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Faq and its DTO FaqDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface FaqMapper {

    @Mapping(source = "theme.id", target = "themeId")
    @Mapping(source = "theme.name", target = "themeName")
    FaqDTO faqToFaqDTO(Faq faq);

    List<FaqDTO> faqsToFaqDTOs(List<Faq> faqs);

    @Mapping(source = "themeId", target = "theme")
    Faq faqDTOToFaq(FaqDTO faqDTO);

    List<Faq> faqDTOsToFaqs(List<FaqDTO> faqDTOs);

    default Theme themeFromId(Long id) {
        if (id == null) {
            return null;
        }
        Theme theme = new Theme();
        theme.setId(id);
        return theme;
    }
}
