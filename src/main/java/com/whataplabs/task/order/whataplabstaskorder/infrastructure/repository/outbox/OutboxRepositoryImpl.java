package com.whataplabs.task.order.whataplabstaskorder.infrastructure.repository.outbox;

import com.whataplabs.task.order.whataplabstaskorder.domain.outbox.Outbox;
import com.whataplabs.task.order.whataplabstaskorder.domain.outbox.OutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class OutboxRepositoryImpl implements OutboxRepository {
    private final OutboxJpaRepository jpaRepository;
    private final OutboxHistoryJpaRepository historyJpaRepository;

    @Override
    @Transactional
    public void pushOutbox(Outbox outbox) {
        jpaRepository.save(OutboxEntity.create(outbox));
    }

    @Override
    @Transactional(readOnly = true)
    public Outbox popOutbox() {
        return jpaRepository.findFirstByOrderByIdAsc()
                .map(OutboxEntity::toDomain).orElse(null);
    }

    @Override
    @Transactional
    public void removeOutbox(Outbox outbox) {
        historyJpaRepository.save(OutboxHistoryEntity.create(outbox));
        jpaRepository.deleteById(outbox.getId());
    }
}
