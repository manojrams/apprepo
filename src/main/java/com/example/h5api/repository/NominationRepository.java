package com.example.h5api.repository;

import com.example.h5api.entity.Nomination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface NominationRepository extends JpaRepository<Nomination, Integer> {

    @Query(value = "SELECT count(value_id) as counter, value_id as valueId FROM Nomination WHERE create_at BETWEEN ?1 AND ?2 GROUP BY value_id", nativeQuery = true)
    List<Map<String, Number>> nominationSummary(LocalDate dateFrom, LocalDate dateTo);

    @Query(value = "select * from (SELECT COUNT(value_id) as counter,value_id, user_id FROM nomination where create_at between ?1 and ?2 group by user_id, value_id) as t1 where t1.counter = (SELECT COUNT (t2.value_id) FROM public.nomination as t2 where t2.create_at between ?1 and ?2 and t2.value_id = t1.value_id group by user_id, value_id ORDER BY count (value_id) desc limit 1)", nativeQuery = true)
    List<Map<String, Number>> selectWinners(LocalDate dateFrom, LocalDate dateTo);

    @Query(value = "select * from (select count(m.value_id) as repeat from (select * from (SELECT COUNT(value_id) as counter, value_id, user_id FROM nomination where create_at between ?1 and ?2 group by user_id, value_id) as t1 where t1.counter = (SELECT COUNT (t2.value_id) FROM nomination as t2 where t2.create_at between ?1 and ?2 and t2.value_id = t1.value_id group by user_id, value_id ORDER BY count (value_id) desc limit 1)) as m group by m.value_id) as repeats where repeat > 1", nativeQuery = true)
    List<BigInteger> findTie(LocalDate dateFrom, LocalDate dateTo);
}
