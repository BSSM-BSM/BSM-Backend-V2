package bssm.bsm.domain.board.comment.domain.type;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class CommentAnonymousConverter implements AttributeConverter<CommentAnonymousType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(CommentAnonymousType commentAnonymous) {
        return commentAnonymous.getValue();
    }

    @Override
    public CommentAnonymousType convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }
        for (CommentAnonymousType commentAnonymous : CommentAnonymousType.values()) {
            if (commentAnonymous.getValue() == dbData) {
                return commentAnonymous;
            }
        }
        throw new IllegalArgumentException("Unknown database value:" + dbData);
    }
}