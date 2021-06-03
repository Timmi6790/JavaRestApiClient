package de.timmi6790.mpstats.api.client.common.board.exceptions;

import de.timmi6790.mpstats.api.client.common.board.models.Board;
import de.timmi6790.mpstats.api.client.exception.BaseRestException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serial;
import java.util.List;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Getter
public class InvalidBoardNameException extends BaseRestException {
    @Serial
    private static final long serialVersionUID = -7584628130995179262L;
    private final List<Board> suggestedBoards;

    public InvalidBoardNameException(final BaseRestException baseRestException,
                                     final List<Board> suggestedBoards) {
        super(baseRestException);

        this.suggestedBoards = suggestedBoards;
    }
}
