package bssm.bsm.domain.board.like.domain.type;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class LikeConverter implements AttributeConverter<LikeType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(LikeType like) {
        return like.getValue();
    }

    @Override
    public LikeType convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }
        for (LikeType like : LikeType.values()) {
            if (like.getValue() == dbData) {
                return like;
            }
        }
        throw new IllegalArgumentException("Unknown database value:" + dbData);
    }
}