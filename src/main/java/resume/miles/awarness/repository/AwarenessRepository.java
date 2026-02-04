package resume.miles.awarness.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import resume.miles.awarness.entity.AwarenessEntity;

@Repository
public interface AwarenessRepository extends JpaRepository<AwarenessEntity, Long>{
     @Query(value = """
                SELECT 
            ss.id               AS subsidebar_id,
            ss.subsidebar_name,
            a.id                AS awareness_id,
            a.awareness_name,
            a.image,
            a.color_code
        FROM master_sidebars s
        JOIN subsidebars ss
            ON ss.mastersidebar_id = s.id
        LEFT JOIN awareness a
            ON a.subsidebar_id = ss.id
        WHERE s.sidebar_short_name = 'AWARENESS'
        AND s.status = 1
        AND ss.status = 1
        AND a.status = 1
        ORDER BY ss.id, a.id;
        """, nativeQuery = true)
    List<Object[]> fetchAwarenessListing();

}
