package ru.atc.jadmin.repository;

import ru.atc.jadmin.domain.Faq;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Faq entity.
 */
@SuppressWarnings("unused")
public interface FaqRepository extends JpaRepository<Faq,Long> {

}
