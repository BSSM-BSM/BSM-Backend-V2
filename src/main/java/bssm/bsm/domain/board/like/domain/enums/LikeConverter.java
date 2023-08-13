package bssm.bsm.domain.board.like.domain.enums;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class LikeConverter implements AttributeConverter<Like, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Like like) {
        return like.getValue();
    }

    @Override
    public Like convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }
        for (Like like : Like.values()) {
            if (like.getValue() == dbData) {
                return like;
            }
        }
        throw new IllegalArgumentException("Unknown database value:" + dbData);
    }
}