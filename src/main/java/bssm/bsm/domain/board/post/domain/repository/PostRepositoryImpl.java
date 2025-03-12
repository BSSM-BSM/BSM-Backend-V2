package bssm.bsm.domain.board.post.domain.repository;

import bssm.bsm.domain.board.board.domain.Board;
import bssm.bsm.domain.board.category.domain.PostCategory;
import bssm.bsm.domain.board.category.service.CategoryProvider;
import bssm.bsm.domain.board.post.domain.Post;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static bssm.bsm.domain.board.post.domain.QPost.post;
import static bssm.bsm.domain.user.domain.QUser.user;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    private final CategoryProvider categoryProvider;

    @Override
    public List<Post> findPostList(Board board, Long startPostId, int limit, String category) {
        return jpaQueryFactory.selectFrom(post)
                .leftJoin(post.writer, user)
                .where(
                        post.isDeleted.isFalse(),
                        post.board.eq(board),
                        categoryEq(board, category),
                        postIdLt(startPostId)
                )
                .limit(limit)
                .orderBy(
                        post.id.desc()
                )
                .fetch();
    }

    private BooleanExpression categoryEq(Board board, String category) {
        if (category.equals("all")) {
            return null;
        }
        PostCategory postCategory = categoryProvider.findCategory(category, board);
        if (postCategory == null) {
            return post.categoryId.isNull();
        }
        return post.category.eq(postCategory);
    }

    private BooleanExpression postIdLt(Long startPostId) {
        if (startPostId == null) {
            return null;
        }
        return post.id.lt(startPostId);
    }

}
