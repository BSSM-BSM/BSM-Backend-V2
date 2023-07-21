package bssm.bsm.domain.board.lostfound.domain.repository;

import bssm.bsm.domain.board.lostfound.domain.type.State;
import bssm.bsm.domain.board.lostfound.presentation.dto.res.LostFoundCompactRes;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import java.util.List;

import static bssm.bsm.domain.board.lostfound.domain.QLostFound.lostFound;

@RequiredArgsConstructor
public class CustomLostFoundRepositoryImpl implements CustomLostFoundRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<LostFoundCompactRes> findAllByState(State state) {
        return jpaQueryFactory
                .select(Projections.constructor(
                        LostFoundCompactRes.class,
                        lostFound.id,
                        lostFound.objectName,
                        lostFound.imgSrc,
                        lostFound.process,
                        lostFound.state))
                .from(lostFound)
                .where(lostFound.state.eq(state))
                .orderBy(lostFound.createdLocalDateTime.desc())
                .fetch();
    }
}
