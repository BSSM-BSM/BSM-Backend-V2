package bssm.bsm.domain.board.post.domain.type;

import bssm.bsm.domain.board.like.domain.type.LikeType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class PostAnonymousConverter implements AttributeConverter<PostAnonymousType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(PostAnonymousType postAnonymous) {
        return postAnonymous.getValue();
    }

    @Override
    public PostAnonymousType convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }
        for (PostAnonymousType postAnonymous : PostAnonymousType.values()) {
            if (postAnonymous.getValue() == dbData) {
                return postAnonymous;
            }
        }
        throw new IllegalArgumentException("Unknown database value:" + dbData);
    }
}