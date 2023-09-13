package ru.company.app.common.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.company.app.test.TestEntity;
import ru.company.app.test.TestEntityRepository;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class JpaEntityTest {

    @Autowired
    TestEntityRepository testEntityRepository;

    @Test
    void When_ObtainJpaEntities_Expect_CorrectEqualsAndHashcode() {
        TestEntity entity = new TestEntity();
        testEntityRepository.save(entity);
        Long generatedId = entity.getId();

        TestEntity testEntity = testEntityRepository.findById(generatedId).orElseThrow();
        TestEntity proxy = testEntityRepository.getReferenceById(generatedId);

        Set<TestEntity> students = new HashSet<>();

        students.add(testEntity);
        students.add(proxy);

        assertThat(students.size()).isEqualTo(1);
    }

}
